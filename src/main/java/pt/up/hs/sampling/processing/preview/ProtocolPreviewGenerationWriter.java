package pt.up.hs.sampling.processing.preview;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import pt.up.hs.sampling.config.ApplicationProperties;
import pt.up.hs.sampling.domain.Protocol;
import pt.up.hs.sampling.repository.ProtocolRepository;
import pt.up.hs.uhc.UniversalHandwritingConverter;
import pt.up.hs.uhc.models.Format;
import pt.up.hs.uhc.models.Page;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static pt.up.hs.sampling.processing.preview.ProtocolPreviewGenerationConstants.*;

/**
 * Generate preview for a single {@link Protocol}.
 *
 * @author José Carlos Paiva
 */
@Component
@StepScope
public class ProtocolPreviewGenerationWriter implements ItemWriter<Page> {

    private final ApplicationProperties properties;
    private final ProtocolRepository protocolRepository;

    private StepExecution stepExecution;

    public ProtocolPreviewGenerationWriter(
        ApplicationProperties properties,
        ProtocolRepository protocolRepository
    ) {
        this.properties = properties;
        this.protocolRepository = protocolRepository;
    }

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(List<? extends Page> pages) throws Exception {

        if (pages.isEmpty()) {
            return;
        }

        long projectId = stepExecution
            .getJobExecution()
            .getExecutionContext()
            .getLong(PROTOCOL_PREVIEW_GENERATION_PROJECT_ID_PARAMETER);

        long id = stepExecution
            .getJobExecution()
            .getExecutionContext()
            .getLong(PROTOCOL_PREVIEW_GENERATION_ID_PARAMETER);

        Path path = Files.createDirectories(Paths.get(
            properties.getPreview().getPath(),
            Long.toString(projectId)
        )).resolve(id + ".svg");

        try (OutputStream os = Files.newOutputStream(path)) {
            new UniversalHandwritingConverter()
                .page(pages.get(0))
                .center()
                .outputFormat(Format.SVG)
                .write(os);
        }

        protocolRepository.cleanPreview(projectId, id);
    }
}
