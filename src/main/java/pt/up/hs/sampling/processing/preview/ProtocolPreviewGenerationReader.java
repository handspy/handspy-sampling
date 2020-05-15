package pt.up.hs.sampling.processing.preview;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pt.up.hs.sampling.domain.ProtocolData;
import pt.up.hs.sampling.repository.ProtocolDataRepository;
import pt.up.hs.sampling.service.mapper.ProtocolDataMapper;

import static pt.up.hs.sampling.processing.preview.ProtocolPreviewGenerationConstants.PROTOCOL_PREVIEW_GENERATION_ID_PARAMETER;
import static pt.up.hs.sampling.processing.preview.ProtocolPreviewGenerationConstants.PROTOCOL_PREVIEW_GENERATION_PROJECT_ID_PARAMETER;

/**
 * Read {@link ProtocolData} protocols' data requiring preview generation from database.
 *
 * @author José Carlos Paiva
 */
@Component
@StepScope
public class ProtocolPreviewGenerationReader implements ItemReader<ProtocolData> {

    private final ProtocolDataRepository protocolDataRepository;
    private final ProtocolDataMapper protocolDataMapper;

    private StepExecution stepExecution;

    public ProtocolPreviewGenerationReader(
        ProtocolDataRepository protocolDataRepository,
        ProtocolDataMapper protocolDataMapper
    ) {
        this.protocolDataRepository = protocolDataRepository;
        this.protocolDataMapper = protocolDataMapper;
    }

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    @Override
    public ProtocolData read() {

        ProtocolData pd = protocolDataRepository
            .findFirstByDirtyPreviewTrue()
            .orElse(null);

        if (pd == null) {
            return null;
        }

        stepExecution
            .getJobExecution()
            .getExecutionContext()
            .put(PROTOCOL_PREVIEW_GENERATION_PROJECT_ID_PARAMETER, pd.getProtocol().getProjectId());

        stepExecution
            .getJobExecution()
            .getExecutionContext()
            .put(PROTOCOL_PREVIEW_GENERATION_ID_PARAMETER, pd.getProtocolId());

        return pd;
    }
}
