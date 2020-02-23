package pt.up.hs.sampling.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DeviceMapperTest {

    private DeviceMapper deviceMapper;

    @BeforeEach
    public void setUp() {
        deviceMapper = new DeviceMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(deviceMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(deviceMapper.fromId(null)).isNull();
    }
}
