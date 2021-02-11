package pt.up.hs.sampling.processing.importing;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pt.up.hs.sampling.config.ApplicationProperties;
import pt.up.hs.sampling.domain.ProtocolData;
import pt.up.hs.sampling.repository.ProtocolDataRepository;
import pt.up.hs.sampling.service.mapper.ProtocolDataMapper;

import static pt.up.hs.sampling.processing.preview.ProtocolPreviewGenerationConstants.PROTOCOL_PREVIEW_GENERATION_ID_PARAMETER;
import static pt.up.hs.sampling.processing.preview.ProtocolPreviewGenerationConstants.PROTOCOL_PREVIEW_GENERATION_PROJECT_ID_PARAMETER;

/**
 * Import protocol into database.
 *
 * @author José Carlos Paiva
 */
@Component
@StepScope
public class ImportProtocolReader implements ItemReader<ProtocolData> {

    private final ApplicationProperties applicationProperties;
    private int nextIndex;

    private StepExecution stepExecution;

    public ImportProtocolReader(
        ApplicationProperties applicationProperties
    ) {
        this.applicationProperties = applicationProperties;
        this.nextIndex = 0;
    }

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public ProtocolData read() {

        /*ProtocolData pd = protocolDataRepository
            .findFirstByDirtyPreviewTrue()
            .orElse(null);

        if (pd == null) {*/
            return null;
        /*}

        stepExecution
            .getJobExecution()
            .getExecutionContext()
            .put(PROTOCOL_PREVIEW_GENERATION_PROJECT_ID_PARAMETER, pd.getProtocol().getProjectId());

        stepExecution
            .getJobExecution()
            .getExecutionContext()
            .put(PROTOCOL_PREVIEW_GENERATION_ID_PARAMETER, pd.getProtocolId());

        return pd;*/
    }
}
