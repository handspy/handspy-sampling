package pt.up.hs.sampling.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.up.hs.sampling.web.rest.TestUtil;

public class ProtocolDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProtocolDTO.class);
        ProtocolDTO protocolDTO1 = new ProtocolDTO();
        protocolDTO1.setId(1L);
        ProtocolDTO protocolDTO2 = new ProtocolDTO();
        assertThat(protocolDTO1).isNotEqualTo(protocolDTO2);
        protocolDTO2.setId(protocolDTO1.getId());
        assertThat(protocolDTO1).isEqualTo(protocolDTO2);
        protocolDTO2.setId(2L);
        assertThat(protocolDTO1).isNotEqualTo(protocolDTO2);
        protocolDTO1.setId(null);
        assertThat(protocolDTO1).isNotEqualTo(protocolDTO2);
    }
}
