package pt.up.hs.sampling.processing.cloner;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pt.up.hs.sampling.service.TextService;
import pt.up.hs.sampling.service.dto.TextDTO;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pt.up.hs.sampling.processing.cloner.ClonerConstants.*;
import static pt.up.hs.sampling.processing.utils.JobParameterUtils.jobParametersToMap;

@Component
@StepScope
public class TextItemWriter implements ItemWriter<Long> {

    private final TextService textService;

    @Value("#{jobParameters['" + PROJECT_ID_PARAMETER + "']}")
    private Long projectId;
    @Value("#{jobParameters['" + NEW_PROJECT_ID_PARAMETER + "']}")
    private Long newProjectId;
    @Value("#{jobParameters['" + MOVE_PARAMETER + "']}")
    private boolean move;

    @Value("#{jobParameters}")
    private Map<String, Object> jobParameters;

    private StepExecution stepExecution;

    public TextItemWriter(final TextService textService) {
        this.textService = textService;
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

        Map<Long, Long> texts;
        if (executionContext.containsKey(TEXT_MAPPING_PARAMETER)) {
            texts = (Map<Long, Long>) executionContext.get(TEXT_MAPPING_PARAMETER);
        } else {
            texts = new HashMap<>();
        }

        for (Long textId: items) {
            TextDTO savedText = textService.copy(
                projectId,
                textId,
                newProjectId,
                move,
                tasks,
                participants
            );
            texts.put(textId, savedText.getId());
        }
        executionContext.put(TEXT_MAPPING_PARAMETER, texts);
    }
}
