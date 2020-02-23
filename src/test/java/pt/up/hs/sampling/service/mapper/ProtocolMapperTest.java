package pt.up.hs.sampling.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ProtocolMapperTest {

    private ProtocolMapper protocolMapper;

    @BeforeEach
    public void setUp() {
        protocolMapper = new ProtocolMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(protocolMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(protocolMapper.fromId(null)).isNull();
    }
}
