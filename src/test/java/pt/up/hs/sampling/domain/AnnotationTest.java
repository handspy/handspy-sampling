package pt.up.hs.sampling.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.up.hs.sampling.web.rest.TestUtil;

public class AnnotationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Annotation.class);
        Annotation annotation1 = new Annotation();
        annotation1.setId(1L);
        Annotation annotation2 = new Annotation();
        annotation2.setId(annotation1.getId());
        assertThat(annotation1).isEqualTo(annotation2);
        annotation2.setId(2L);
        assertThat(annotation1).isNotEqualTo(annotation2);
        annotation1.setId(null);
        assertThat(annotation1).isNotEqualTo(annotation2);
    }
}
