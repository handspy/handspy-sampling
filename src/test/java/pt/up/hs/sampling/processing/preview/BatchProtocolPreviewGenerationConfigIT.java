package pt.up.hs.sampling.processing.preview;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pt.up.hs.sampling.SamplingApp;
import pt.up.hs.sampling.config.ApplicationProperties;
import pt.up.hs.sampling.domain.Protocol;
import pt.up.hs.sampling.processing.BatchTestConfiguration;
import pt.up.hs.sampling.repository.ProtocolDataRepository;
import pt.up.hs.sampling.repository.ProtocolRepository;
import pt.up.hs.sampling.service.ProtocolService;
import pt.up.hs.sampling.service.mapper.ProtocolDataMapper;
import pt.up.hs.sampling.service.mapper.ProtocolMapper;
import pt.up.hs.sampling.service.mapper.UhcPageMapper;
import pt.up.hs.sampling.web.rest.TestUtil;
import pt.up.hs.uhc.UniversalHandwritingConverter;

import javax.persistence.EntityManager;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.io.FileMatchers.anExistingFile;
import static pt.up.hs.sampling.processing.preview.ProtocolPreviewGenerationConstants.PROTOCOL_PREVIEW_GENERATION_STEP;


@SpringBootTest(classes = {
    SamplingApp.class,
    BatchTestConfiguration.class
})
public class BatchProtocolPreviewGenerationConfigIT {

    private static final Long DEFAULT_PROJECT_ID = 1L;

    @Autowired
    private JobLauncherTestUtils testUtils;

    @Autowired
    private BatchProtocolPreviewGenerationConfig config;

    @Autowired
    ApplicationProperties properties;

    @Autowired
    private ProtocolService protocolService;

    @Autowired
    private UhcPageMapper uhcPageMapper;
    @Autowired
    private ProtocolMapper protocolMapper;
    @Autowired
    private ProtocolDataMapper protocolDataMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private ProtocolRepository protocolRepository;
    @Autowired
    private ProtocolDataRepository protocolDataRepository;

    private List<Protocol> protocols;

    @BeforeEach
    @Transactional
    public void setup() throws Exception {
        protocols = new UniversalHandwritingConverter()
            .file("data/protocols/page_full.data", new ByteArrayInputStream(TestUtil.readFileFromResourcesFolder("data/protocols/page_full.data")))
            .file("data/protocols/page_empty.data", new ByteArrayInputStream(TestUtil.readFileFromResourcesFolder("data/protocols/page_empty.data")))
            .getPages()
            .stream()
            .map(uhcPageMapper::uhcPageToProtocolData)
            .map(pd -> {
                Protocol protocol = new Protocol().projectId(DEFAULT_PROJECT_ID);

                pd.setProtocol(protocol);
                protocolDataRepository.saveAndFlush(pd);

                return pd.getProtocol();
            })
            .collect(Collectors.toList());
    }

    @AfterEach
    @Transactional
    public void cleanup() {
        for (Protocol protocol: protocols) {
            protocolDataRepository.deleteById(protocol.getId());
            protocolRepository.deleteByProjectIdAndId(protocol.getProjectId(), protocol.getId());
        }
        deleteFolder(Paths.get(
            properties.getPreview().getPath(), DEFAULT_PROJECT_ID.toString()).toFile());
    }

    @Test
    public void testEntireJob() throws Exception {
        final JobExecution result = testUtils.getJobLauncher()
            .run(config.job(), testUtils.getUniqueJobParameters());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(BatchStatus.COMPLETED, result.getStatus());
        for (Protocol protocol: protocols) {
            Assertions.assertTrue(anExistingFile().matches(
                Paths.get(
                    properties.getPreview().getPath(),
                    protocol.getProjectId().toString(),
                    protocol.getId().toString() + ".png"
                ).toFile()));
        }
    }

    @Test
    public void testSpecificStep() {
        final JobExecution result = testUtils.launchStep(
            PROTOCOL_PREVIEW_GENERATION_STEP
        );
        Assertions.assertNotNull(result);
        Assertions.assertEquals(BatchStatus.COMPLETED, result.getStatus());
        for (Protocol protocol: protocols) {
            Assertions.assertTrue(anExistingFile().matches(
                Paths.get(
                    properties.getPreview().getPath(),
                    protocol.getProjectId().toString(),
                    protocol.getId().toString() + ".png"
                ).toFile()));
        }
    }

    private boolean deleteFolder(File file) {
        File[] allContents = file.listFiles();
        if (allContents != null) {
            for (File f : allContents) {
                deleteFolder(f);
            }
        }
        return file.delete();
    }
}
