package pt.up.hs.sampling.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LayoutMapperTest {

    private LayoutMapper layoutMapper;

    @BeforeEach
    public void setUp() {
        layoutMapper = new LayoutMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(layoutMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(layoutMapper.fromId(null)).isNull();
    }
}
