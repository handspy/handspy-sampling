package pt.up.hs.sampling.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationMapperTest {

    private AnnotationMapper annotationMapper;

    @BeforeEach
    public void setUp() {
        annotationMapper = new AnnotationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(annotationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(annotationMapper.fromId(null)).isNull();
    }
}
