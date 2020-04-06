package pt.up.hs.sampling.processing.preview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.batch.core.BatchStatus.*;

@Component
public class ProtocolPreviewGenerationListener extends JobExecutionListenerSupport {

    private final Logger log = LoggerFactory.getLogger(ProtocolPreviewGenerationListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == STARTED) {
            log.info("PROTOCOL PREVIEW GENERATION BATCH STARTED...!");
        }
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == COMPLETED) {
            log.info("PROTOCOL PREVIEW GENERATION BATCH COMPLETED...!");
        }
    }
}
