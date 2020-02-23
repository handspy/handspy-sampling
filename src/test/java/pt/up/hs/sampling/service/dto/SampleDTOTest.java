package pt.up.hs.sampling.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.up.hs.sampling.web.rest.TestUtil;

public class SampleDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SampleDTO.class);
        SampleDTO sampleDTO1 = new SampleDTO();
        sampleDTO1.setId(1L);
        SampleDTO sampleDTO2 = new SampleDTO();
        assertThat(sampleDTO1).isNotEqualTo(sampleDTO2);
        sampleDTO2.setId(sampleDTO1.getId());
        assertThat(sampleDTO1).isEqualTo(sampleDTO2);
        sampleDTO2.setId(2L);
        assertThat(sampleDTO1).isNotEqualTo(sampleDTO2);
        sampleDTO1.setId(null);
        assertThat(sampleDTO1).isNotEqualTo(sampleDTO2);
    }
}
