package pt.up.hs.sampling.processing.preview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static pt.up.hs.sampling.processing.preview.ProtocolPreviewGenerationConstants.PROTOCOL_PREVIEW_GENERATION_UNIQUENESS_PARAMETER;

@Component
public class BatchProtocolPreviewGenerationJobLauncher {
    private static final long EXECUTION_DELAY = 5000;

    private final Logger log = LoggerFactory.getLogger(BatchProtocolPreviewGenerationJobLauncher.class);

    private final ScheduledExecutorService executorService =
        Executors.newSingleThreadScheduledExecutor();

    private final JobLauncher jobLauncher;
    private final BatchProtocolPreviewGenerationConfig config;

    public BatchProtocolPreviewGenerationJobLauncher(
        JobLauncher jobLauncher,
        BatchProtocolPreviewGenerationConfig config
    ) {
        this.jobLauncher = jobLauncher;
        this.config = config;
    }

    @Scheduled(cron = "#{'${application.preview.cron:-}'}")
    public void schedule() {
        newExecution();
    }

    public void newExecution() {

        executorService.schedule(() -> {
            try {
                jobLauncher.run(config.job(), new JobParametersBuilder()
                    .addLong(PROTOCOL_PREVIEW_GENERATION_UNIQUENESS_PARAMETER, System.nanoTime())
                    .toJobParameters());
            } catch (Exception e) {
                log.error("Failed to launch batch protocol preview generation", e);
                // ignore error
            }
        }, EXECUTION_DELAY, TimeUnit.MILLISECONDS);
    }
}
