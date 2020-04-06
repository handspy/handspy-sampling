package pt.up.hs.sampling.processing.preview;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;
import pt.up.hs.sampling.repository.ProtocolRepository;
import pt.up.hs.sampling.service.dto.ProtocolDTO;
import pt.up.hs.sampling.service.mapper.ProtocolMapper;

import static pt.up.hs.sampling.processing.preview.ProtocolPreviewGenerationConstants.PROTOCOL_PREVIEW_GENERATION_ID_PARAMETER;
import static pt.up.hs.sampling.processing.preview.ProtocolPreviewGenerationConstants.PROTOCOL_PREVIEW_GENERATION_PROJECT_ID_PARAMETER;

/**
 * Read {@link ProtocolDTO} protocols requiring preview generation from database.
 *
 * @author José Carlos Paiva
 */
@Component
@StepScope
public class ProtocolPreviewGenerationReader implements ItemReader<ProtocolDTO> {

    private final ProtocolRepository protocolRepository;
    private final ProtocolMapper protocolMapper;

    private StepExecution stepExecution;

    public ProtocolPreviewGenerationReader(
        ProtocolRepository protocolRepository,
        ProtocolMapper protocolMapper
    ) {
        this.protocolRepository = protocolRepository;
        this.protocolMapper = protocolMapper;
    }

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public ProtocolDTO read() {
        ProtocolDTO protocolDTO = protocolRepository
            .findFirstByDirtyPreviewTrue()
            .map(protocolMapper::toDto)
            .orElse(null);
        if (protocolDTO == null) {
            return null;
        }
        stepExecution
            .getJobExecution()
            .getExecutionContext()
            .put(PROTOCOL_PREVIEW_GENERATION_PROJECT_ID_PARAMETER, protocolDTO.getProjectId());
        stepExecution
            .getJobExecution()
            .getExecutionContext()
            .put(PROTOCOL_PREVIEW_GENERATION_ID_PARAMETER, protocolDTO.getId());
        return protocolDTO;
    }
}
