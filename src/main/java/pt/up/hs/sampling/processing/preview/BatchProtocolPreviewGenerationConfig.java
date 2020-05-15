package pt.up.hs.sampling.processing.preview;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.up.hs.sampling.domain.ProtocolData;
import pt.up.hs.uhc.models.Page;

import static pt.up.hs.sampling.processing.preview.ProtocolPreviewGenerationConstants.PROTOCOL_PREVIEW_GENERATION_JOB;
import static pt.up.hs.sampling.processing.preview.ProtocolPreviewGenerationConstants.PROTOCOL_PREVIEW_GENERATION_STEP;

@Configuration
public class BatchProtocolPreviewGenerationConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final ProtocolPreviewGenerationReader reader;
    private final ProtocolPreviewGenerationProcessor processor;
    private final ProtocolPreviewGenerationWriter writer;
    private final ProtocolPreviewGenerationListener listener;

    public BatchProtocolPreviewGenerationConfig(
        JobBuilderFactory jobBuilderFactory,
        StepBuilderFactory stepBuilderFactory,
        ProtocolPreviewGenerationReader reader,
        ProtocolPreviewGenerationProcessor processor,
        ProtocolPreviewGenerationWriter writer,
        ProtocolPreviewGenerationListener listener
    ) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.reader = reader;
        this.processor = processor;
        this.writer = writer;
        this.listener = listener;
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get(PROTOCOL_PREVIEW_GENERATION_STEP)
            .<ProtocolData, Page>chunk(1)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get(PROTOCOL_PREVIEW_GENERATION_JOB)
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step())
            .end()
            .build();
    }
}
