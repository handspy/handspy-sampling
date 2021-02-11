package pt.up.hs.sampling.processing.cloner;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pt.up.hs.sampling.processing.utils.JobParameterUtils;
import pt.up.hs.sampling.service.ProtocolService;
import pt.up.hs.sampling.service.dto.ProtocolDTO;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pt.up.hs.sampling.processing.cloner.ClonerConstants.*;
import static pt.up.hs.sampling.processing.utils.JobParameterUtils.jobParametersToMap;

@Component
@StepScope
public class ProtocolItemWriter implements ItemWriter<Long> {

    private final ProtocolService protocolService;

    @Value("#{jobParameters['" + PROJECT_ID_PARAMETER + "']}")
    private Long projectId;
    @Value("#{jobParameters['" + NEW_PROJECT_ID_PARAMETER + "']}")
    private Long newProjectId;
    @Value("#{jobParameters['" + MOVE_PARAMETER + "']}")
    private boolean move;

    @Value("#{jobParameters}")
    private Map<String, Object> jobParameters;

    private StepExecution stepExecution;

    public ProtocolItemWriter(final ProtocolService protocolService) {
        this.protocolService = protocolService;
    }

    @BeforeStep
    public void beforeStep(final StepExecution stepExecution) {
        this.stepExecution = stepExecution;
    }

    @Override
    public void write(@Nonnull List<? extends Long> items) throws Exception {
        ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();

        Map<Long, Long> tasks = jobParametersToMap(jobParameters, TASK_PARAMETER_KEY_PREFIX);
        Map<Long, Long> participants = jobParametersToMap(jobParameters, PARTICIPANT_PARAMETER_KEY_PREFIX);

        Map<Long, Long> protocols;
        if (executionContext.containsKey(PROTOCOL_MAPPING_PARAMETER)) {
            protocols = (Map<Long, Long>) executionContext.get(PROTOCOL_MAPPING_PARAMETER);
        } else {
            protocols = new HashMap<>();
        }

        for (Long protocolId: items) {
            ProtocolDTO savedProtocol = protocolService.copy(
                projectId,
                protocolId,
                newProjectId,
                move,
                tasks,
                participants
            );
            protocols.put(protocolId, savedProtocol.getId());
        }
        executionContext.put(PROTOCOL_MAPPING_PARAMETER, protocols);
    }
}
