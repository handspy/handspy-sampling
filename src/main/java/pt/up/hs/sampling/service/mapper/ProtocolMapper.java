package pt.up.hs.sampling.service.mapper;

import org.mapstruct.Mapper;
import pt.up.hs.sampling.domain.Protocol;
import pt.up.hs.sampling.service.dto.ProtocolDTO;

/**
 * Mapper for the entity {@link Protocol} and its DTO {@link ProtocolDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProtocolMapper extends EntityMapper<ProtocolDTO, Protocol> {

    ProtocolDTO toDto(Protocol protocol);

    Protocol toEntity(ProtocolDTO protocolDTO);

    default Protocol fromId(Long id) {
        if (id == null) {
            return null;
        }
        Protocol protocol = new Protocol();
        protocol.setId(id);
        return protocol;
    }
}
