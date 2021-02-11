package pt.up.hs.sampling.processing.cloner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static pt.up.hs.sampling.processing.cloner.ClonerConstants.*;
import static pt.up.hs.sampling.processing.utils.JobParameterUtils.longListToJobParameters;
import static pt.up.hs.sampling.processing.utils.JobParameterUtils.mapToJobParameters;

@Component
public class TextClonerJobLauncher {

    private static final long EXECUTION_DELAY = 500;

    private static final ScheduledExecutorService executorService =
        Executors.newSingleThreadScheduledExecutor();

    private final Logger log = LoggerFactory.getLogger(TextClonerJobLauncher.class);

    private final JobLauncher jobLauncher;
    private final TextClonerJobConfig config;

    public TextClonerJobLauncher(JobLauncher jobLauncher, TextClonerJobConfig config) {
        this.jobLauncher = jobLauncher;
        this.config = config;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void run(
        @Nonnull Long projectId,
        @Nonnull Long newProjectId,
        @Nonnull List<Long> textIds,
        boolean move,
        Map<Long, Long> tasks,
        Map<Long, Long> participants
    ) {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder
            .addLong(UNIQUENESS_PARAMETER, System.nanoTime())
            .addLong(PROJECT_ID_PARAMETER, projectId)
            .addLong(NEW_PROJECT_ID_PARAMETER, newProjectId)
            .addString(MOVE_PARAMETER, Boolean.toString(move))
            .addJobParameters(mapToJobParameters(tasks, TASK_PARAMETER_KEY_PREFIX))
            .addJobParameters(mapToJobParameters(participants, PARTICIPANT_PARAMETER_KEY_PREFIX))
            .addJobParameters(longListToJobParameters(textIds, TEXT_PARAMETER_KEY_PREFIX));

        executorService.schedule(() -> {
            try {
                JobExecution jobExecution = jobLauncher.run(
                    config.textsCloningJob(),
                    jobParametersBuilder.toJobParameters()
                );
                log.info("Launched job: " + jobExecution.toString());
            } catch (Exception e) {
                log.error("Could not launch job: " + e.getMessage());
            }
        }, EXECUTION_DELAY, TimeUnit.MILLISECONDS);
    }
}
