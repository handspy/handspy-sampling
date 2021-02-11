package pt.up.hs.sampling.processing.cloner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClonerJobConfig {

    private final JobBuilderFactory jobBuilders;
    private final StepBuilderFactory stepBuilders;

    private final ProtocolItemReader protocolItemReader;
    private final ProtocolItemWriter protocolItemWriter;
    private final TextItemReader textItemReader;
    private final TextItemWriter textItemWriter;

    @Autowired
    public ClonerJobConfig(
        JobBuilderFactory jobBuilders,
        StepBuilderFactory stepBuilders,
        ProtocolItemReader protocolItemReader,
        ProtocolItemWriter protocolItemWriter,
        TextItemReader textItemReader,
        TextItemWriter textItemWriter
    ) {
        this.jobBuilders = jobBuilders;
        this.stepBuilders = stepBuilders;
        this.protocolItemReader = protocolItemReader;
        this.protocolItemWriter = protocolItemWriter;
        this.textItemReader = textItemReader;
        this.textItemWriter = textItemWriter;
    }

    public Job projectCloningJob() {
        SimpleJobBuilder jb = jobBuilders.get("projectCloningJob")
            .incrementer(new RunIdIncrementer())
            .start(protocolCloningStep())
            .next(textCloningStep());
        return jb.build();
    }

    @Bean
    public Step protocolCloningStep() {
        return stepBuilders.get("protocolCloningStep")
            .<Long, Long>chunk(1)
            .reader(protocolItemReader)
            .writer(protocolItemWriter)
            .build();
    }

    @Bean
    public Step textCloningStep() {
        return stepBuilders.get("textCloningStep")
            .<Long, Long>chunk(10)
            .reader(textItemReader)
            .writer(textItemWriter)
            .build();
    }
}
