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

import static pt.up.hs.sampling.processing.cloner.ClonerConstants.TEXT_PARAMETER_KEY_PREFIX;
import static pt.up.hs.sampling.processing.utils.JobParameterUtils.jobParametersToLongList;

@Configuration
public class TextClonerJobConfig {

    private final JobBuilderFactory jobBuilders;
    private final StepBuilderFactory stepBuilders;

    private final ItemReader<Long> textsToClone;
    private final TextItemWriter textItemWriter;

    @Autowired
    public TextClonerJobConfig(
        JobBuilderFactory jobBuilders,
        StepBuilderFactory stepBuilders,
        ItemReader<Long> textsToClone,
        TextItemWriter textItemWriter
    ) {
        this.jobBuilders = jobBuilders;
        this.stepBuilders = stepBuilders;
        this.textsToClone = textsToClone;
        this.textItemWriter = textItemWriter;
    }

    @Bean
    public Job textsCloningJob() {
        SimpleJobBuilder jb = jobBuilders.get("textsCloningJob")
            .incrementer(new RunIdIncrementer())
            .start(textCloningStep())
            .preventRestart();
        return jb.build();
    }

    @Bean
    public Step textCloningStep() {
        return stepBuilders.get("textCloningStep")
            .<Long, Long>chunk(1)
            .reader(textsToClone)
            .writer(textItemWriter)
            .build();
    }

    @Bean
    @StepScope
    public ItemReader<Long> textsToClone(@Value("#{jobParameters}") Map<String, Object> jobParameters) {
        return new ListItemReader<>(jobParametersToLongList(jobParameters, TEXT_PARAMETER_KEY_PREFIX));
    }
}
