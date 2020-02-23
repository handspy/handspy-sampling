package pt.up.hs.sampling.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TextMapperTest {

    private TextMapper textMapper;

    @BeforeEach
    public void setUp() {
        textMapper = new TextMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(textMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(textMapper.fromId(null)).isNull();
    }
}
