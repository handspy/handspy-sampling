package pt.up.hs.sampling.processing.preview;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import pt.up.hs.sampling.config.ApplicationProperties;
import pt.up.hs.sampling.repository.ProtocolDataRepository;
import pt.up.hs.uhc.UniversalHandwritingConverter;
import pt.up.hs.uhc.models.Format;
import pt.up.hs.uhc.models.Page;

import java.awt.geom.Rectangle2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static pt.up.hs.sampling.processing.preview.ProtocolPreviewGenerationConstants.*;

/**
 * Generate preview for a single {@link pt.up.hs.sampling.domain.ProtocolData}.
 *
 * @author José Carlos Paiva
 */
@Component
@StepScope
public class ProtocolPreviewGenerationWriter implements ItemWriter<Page> {

    private final ApplicationProperties properties;
    private final ProtocolDataRepository protocolDataRepository;

    private StepExecution stepExecution;

    public ProtocolPreviewGenerationWriter(
        ApplicationProperties properties,
        ProtocolDataRepository protocolDataRepository
    ) {
        this.properties = properties;
        this.protocolDataRepository = protocolDataRepository;
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
        )).resolve(id + ".png");

        byte[] svgBytes;

        // protocol page to SVG
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            new UniversalHandwritingConverter()
                .page(pages.get(0))
                .center()
                .outputFormat(Format.SVG)
                .write(baos);

            svgBytes = baos.toByteArray();
        }

        // SVG to PNG
        try (OutputStream os = Files.newOutputStream(path)) {
            transcodeToPng(
                new ByteArrayInputStream(svgBytes),
                pages.get(0).getWidth().intValue(),
                pages.get(0).getHeight().intValue(),
                PROTOCOL_PREVIEW_WIDTH,
                PROTOCOL_PREVIEW_HEIGHT,
                os
            );
        }

        protocolDataRepository.cleanPreview(id);
    }

    private void transcodeToPng(
        InputStream is,
        int originalWidth, int originalHeight,
        int width, int height,
        OutputStream os
    ) throws TranscoderException {

        PNGTranscoder t = new PNGTranscoder();
        t.addTranscodingHint(PNGTranscoder.KEY_WIDTH, (float) width);
        t.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, (float) height);
        t.addTranscodingHint(PNGTranscoder.KEY_AOI, new Rectangle2D.Float(0, 0, originalWidth, originalWidth));
        t.addTranscodingHint(PNGTranscoder.KEY_FORCE_TRANSPARENT_WHITE, true);

        TranscoderInput transcoderInput = new TranscoderInput(is);
        TranscoderOutput transcoderOutput = new TranscoderOutput(os);

        // Save the image.
        t.transcode(transcoderInput, transcoderOutput);
    }
}
