package pt.up.hs.sampling.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.up.hs.sampling.processing.preview.BatchProtocolPreviewGenerationJobLauncher;
import pt.up.hs.sampling.repository.ProtocolDataRepository;

@Component
public class CleanupProtocolPreviewRunner implements CommandLineRunner {

    private final ApplicationProperties applicationProperties;
    private final ProtocolDataRepository protocolDataRepository;
    private final BatchProtocolPreviewGenerationJobLauncher previewGenerationJobLauncher;

    public CleanupProtocolPreviewRunner(
        ApplicationProperties applicationProperties,
        ProtocolDataRepository protocolDataRepository,
        BatchProtocolPreviewGenerationJobLauncher previewGenerationJobLauncher
    ) {
        this.applicationProperties = applicationProperties;
        this.protocolDataRepository = protocolDataRepository;
        this.previewGenerationJobLauncher = previewGenerationJobLauncher;
    }

    @Transactional
    @Override
    public void run(String... args) {
        if (applicationProperties.getPreview().isCleanOnStartup()) {
            protocolDataRepository.markAllForPreviewRegenerate();
        }
        previewGenerationJobLauncher.newExecution();
    }
}
