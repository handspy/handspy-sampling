package pt.up.hs.sampling.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SampleMapperTest {

    private SampleMapper sampleMapper;

    @BeforeEach
    public void setUp() {
        sampleMapper = new SampleMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(sampleMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(sampleMapper.fromId(null)).isNull();
    }
}
