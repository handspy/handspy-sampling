package pt.up.hs.sampling.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pt.up.hs.sampling.processing.preview.BatchProtocolPreviewGenerationJobLauncher;
import pt.up.hs.sampling.repository.ProtocolRepository;

@Component
public class ProtocolPreviewGenerationRunner implements CommandLineRunner {

    private final ProtocolRepository protocolRepository;
    private final BatchProtocolPreviewGenerationJobLauncher previewGenerationJobLauncher;

    public ProtocolPreviewGenerationRunner(
        ProtocolRepository protocolRepository,
        BatchProtocolPreviewGenerationJobLauncher previewGenerationJobLauncher
    ) {
        this.protocolRepository = protocolRepository;
        this.previewGenerationJobLauncher = previewGenerationJobLauncher;
    }

    @Transactional
    @Override
    public void run(String... args) {
        protocolRepository.markAllForPreviewRegenerate();
        previewGenerationJobLauncher.newExecution();
    }
}
