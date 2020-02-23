package pt.up.hs.sampling.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.up.hs.sampling.web.rest.TestUtil;

public class AnnotationTypeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnnotationTypeDTO.class);
        AnnotationTypeDTO annotationTypeDTO1 = new AnnotationTypeDTO();
        annotationTypeDTO1.setId(1L);
        AnnotationTypeDTO annotationTypeDTO2 = new AnnotationTypeDTO();
        assertThat(annotationTypeDTO1).isNotEqualTo(annotationTypeDTO2);
        annotationTypeDTO2.setId(annotationTypeDTO1.getId());
        assertThat(annotationTypeDTO1).isEqualTo(annotationTypeDTO2);
        annotationTypeDTO2.setId(2L);
        assertThat(annotationTypeDTO1).isNotEqualTo(annotationTypeDTO2);
        annotationTypeDTO1.setId(null);
        assertThat(annotationTypeDTO1).isNotEqualTo(annotationTypeDTO2);
    }
}
