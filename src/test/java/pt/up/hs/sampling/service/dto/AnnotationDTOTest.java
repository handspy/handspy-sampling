package pt.up.hs.sampling.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.up.hs.sampling.web.rest.TestUtil;

public class AnnotationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnnotationDTO.class);
        AnnotationDTO annotationDTO1 = new AnnotationDTO();
        annotationDTO1.setId(1L);
        AnnotationDTO annotationDTO2 = new AnnotationDTO();
        assertThat(annotationDTO1).isNotEqualTo(annotationDTO2);
        annotationDTO2.setId(annotationDTO1.getId());
        assertThat(annotationDTO1).isEqualTo(annotationDTO2);
        annotationDTO2.setId(2L);
        assertThat(annotationDTO1).isNotEqualTo(annotationDTO2);
        annotationDTO1.setId(null);
        assertThat(annotationDTO1).isNotEqualTo(annotationDTO2);
    }
}
