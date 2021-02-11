package pt.up.hs.sampling.processing.importing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.up.hs.sampling.config.ApplicationProperties;
import pt.up.hs.sampling.domain.ProtocolData;
import pt.up.hs.sampling.processing.preview.ProtocolPreviewGenerationListener;
import pt.up.hs.sampling.processing.preview.ProtocolPreviewGenerationProcessor;
import pt.up.hs.sampling.processing.preview.ProtocolPreviewGenerationReader;
import pt.up.hs.sampling.processing.preview.ProtocolPreviewGenerationWriter;
import pt.up.hs.uhc.models.Page;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static pt.up.hs.sampling.processing.importing.BatchImportProtocolConstants.IMPORT_PROTOCOL_JOB;
import static pt.up.hs.sampling.processing.importing.BatchImportProtocolConstants.IMPORT_PROTOCOL_STEP;

@Configuration
public class BatchImportProtocolConfig {

    /*private final ApplicationProperties applicationProperties;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final ProtocolPreviewGenerationReader reader;
    private final ProtocolPreviewGenerationProcessor processor;
    private final ProtocolPreviewGenerationWriter writer;
    private final ProtocolPreviewGenerationListener listener;

    public BatchImportProtocolConfig(
        ApplicationProperties applicationProperties,
        JobBuilderFactory jobBuilderFactory,
        StepBuilderFactory stepBuilderFactory,
        Import reader,
        ProtocolPreviewGenerationProcessor processor,
        ProtocolPreviewGenerationWriter writer,
        ProtocolPreviewGenerationListener listener
    ) {
        this.applicationProperties = applicationProperties;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.reader = reader;
        this.processor = processor;
        this.writer = writer;
        this.listener = listener;
    }

    @Bean
    public ItemReader<File> itemReader() throws IOException {
        List<File> files = Files.walk(Paths.get(applicationProperties.getImporting().getPath(), ""))
            .filter(Files::isRegularFile)
            .map(Path::toFile)
            .collect(Collectors.toList());
        return new IteratorItemReader<>(files);
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get(IMPORT_PROTOCOL_STEP)
            .<ProtocolData, Page>chunk(1)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get(IMPORT_PROTOCOL_JOB)
            .incrementer(new RunIdIncrementer())
            .listener(listener)
            .flow(step())
            .end()
            .build();
    }*/
}
