package pt.up.hs.sampling.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.up.hs.sampling.domain.Protocol;
import pt.up.hs.sampling.domain.ProtocolData;
import pt.up.hs.sampling.service.dto.ProtocolDataDTO;

/**
 * Mapper for the entity {@link ProtocolData} and its DTO {@link ProtocolDataDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProtocolDataMapper extends EntityMapper<ProtocolDataDTO, ProtocolData> {

    @Mapping(source = "protocol.id", target = "protocolId")
    ProtocolDataDTO toDto(ProtocolData pd);

    @Mapping(source = "protocolId", target = "protocol.id")
    @Mapping(target = "dirtyPreview", constant = "true")
    ProtocolData toEntity(ProtocolDataDTO pdDTO);

    default ProtocolData fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProtocolData pd = new ProtocolData();
        Protocol protocol = new Protocol();
        protocol.setId(id);
        pd.setProtocol(protocol);
        return pd;
    }
}
