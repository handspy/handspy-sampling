package pt.up.hs.sampling.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.up.hs.sampling.web.rest.TestUtil;

public class LayoutDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LayoutDTO.class);
        LayoutDTO layoutDTO1 = new LayoutDTO();
        layoutDTO1.setId(1L);
        LayoutDTO layoutDTO2 = new LayoutDTO();
        assertThat(layoutDTO1).isNotEqualTo(layoutDTO2);
        layoutDTO2.setId(layoutDTO1.getId());
        assertThat(layoutDTO1).isEqualTo(layoutDTO2);
        layoutDTO2.setId(2L);
        assertThat(layoutDTO1).isNotEqualTo(layoutDTO2);
        layoutDTO1.setId(null);
        assertThat(layoutDTO1).isNotEqualTo(layoutDTO2);
    }
}
