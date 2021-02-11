package pt.up.hs.sampling.processing.cloner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static pt.up.hs.sampling.processing.cloner.ClonerConstants.PROTOCOL_PARAMETER_KEY_PREFIX;
import static pt.up.hs.sampling.processing.utils.JobParameterUtils.jobParametersToLongList;

@Configuration
public class ProtocolClonerJobConfig {

    private final JobBuilderFactory jobBuilders;
    private final StepBuilderFactory stepBuilders;

    private final ItemReader<Long> protocolsToClone;
    private final ProtocolItemWriter protocolItemWriter;

    @Autowired
    public ProtocolClonerJobConfig(
        JobBuilderFactory jobBuilders,
        StepBuilderFactory stepBuilders,
        ItemReader<Long> protocolsToClone,
        ProtocolItemWriter protocolItemWriter
    ) {
        this.jobBuilders = jobBuilders;
        this.stepBuilders = stepBuilders;
        this.protocolsToClone = protocolsToClone;
        this.protocolItemWriter = protocolItemWriter;
    }

    @Bean
    public Job protocolsCloningJob() {
        SimpleJobBuilder jb = jobBuilders.get("protocolsCloningJob")
            .incrementer(new RunIdIncrementer())
            .start(protocolCloningStep())
            .preventRestart();
        return jb.build();
    }

    @Bean
    public Step protocolCloningStep() {
        return stepBuilders.get("protocolCloningStep")
            .<Long, Long>chunk(1)
            .reader(protocolsToClone)
            .writer(protocolItemWriter)
            .build();
    }

    @Bean
    @StepScope
    public ItemReader<Long> protocolsToClone(@Value("#{jobParameters}") Map<String, Object> jobParameters) {
        return new ListItemReader<>(jobParametersToLongList(jobParameters, PROTOCOL_PARAMETER_KEY_PREFIX));
    }
}
