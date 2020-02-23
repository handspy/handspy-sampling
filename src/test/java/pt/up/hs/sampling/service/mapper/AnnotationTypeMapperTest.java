package pt.up.hs.sampling.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AnnotationTypeMapperTest {

    private AnnotationTypeMapper annotationTypeMapper;

    @BeforeEach
    public void setUp() {
        annotationTypeMapper = new AnnotationTypeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(annotationTypeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(annotationTypeMapper.fromId(null)).isNull();
    }
}
