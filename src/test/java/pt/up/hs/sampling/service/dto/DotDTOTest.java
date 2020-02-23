package pt.up.hs.sampling.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.up.hs.sampling.web.rest.TestUtil;

public class DotDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DotDTO.class);
        DotDTO dotDTO1 = new DotDTO();
        dotDTO1.setId(1L);
        DotDTO dotDTO2 = new DotDTO();
        assertThat(dotDTO1).isNotEqualTo(dotDTO2);
        dotDTO2.setId(dotDTO1.getId());
        assertThat(dotDTO1).isEqualTo(dotDTO2);
        dotDTO2.setId(2L);
        assertThat(dotDTO1).isNotEqualTo(dotDTO2);
        dotDTO1.setId(null);
        assertThat(dotDTO1).isNotEqualTo(dotDTO2);
    }
}
