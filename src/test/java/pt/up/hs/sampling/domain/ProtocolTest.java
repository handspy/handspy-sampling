package pt.up.hs.sampling.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import pt.up.hs.sampling.web.rest.TestUtil;

public class ProtocolTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Protocol.class);
        Protocol protocol1 = new Protocol();
        protocol1.setId(1L);
        Protocol protocol2 = new Protocol();
        protocol2.setId(protocol1.getId());
        assertThat(protocol1).isEqualTo(protocol2);
        protocol2.setId(2L);
        assertThat(protocol1).isNotEqualTo(protocol2);
        protocol1.setId(null);
        assertThat(protocol1).isNotEqualTo(protocol2);
    }
}
