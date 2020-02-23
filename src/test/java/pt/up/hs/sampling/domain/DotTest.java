package pt.up.hs.sampling.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.up.hs.sampling.web.rest.TestUtil;

public class DotTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dot.class);
        Dot dot1 = new Dot();
        dot1.setId(1L);
        Dot dot2 = new Dot();
        dot2.setId(dot1.getId());
        assertThat(dot1).isEqualTo(dot2);
        dot2.setId(2L);
        assertThat(dot1).isNotEqualTo(dot2);
        dot1.setId(null);
        assertThat(dot1).isNotEqualTo(dot2);
    }
}
