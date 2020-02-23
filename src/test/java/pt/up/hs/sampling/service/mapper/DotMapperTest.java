package pt.up.hs.sampling.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DotMapperTest {

    private DotMapper dotMapper;

    @BeforeEach
    public void setUp() {
        dotMapper = new DotMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(dotMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(dotMapper.fromId(null)).isNull();
    }
}
