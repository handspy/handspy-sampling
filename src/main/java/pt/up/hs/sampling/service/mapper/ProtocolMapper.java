package pt.up.hs.sampling.service.mapper;


import pt.up.hs.sampling.domain.*;
import pt.up.hs.sampling.service.dto.ProtocolDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Protocol} and its DTO {@link ProtocolDTO}.
 */
@Mapper(componentModel = "spring", uses = {SampleMapper.class})
public interface ProtocolMapper extends EntityMapper<ProtocolDTO, Protocol> {

    @Mapping(source = "sample.id", target = "sampleId")
    ProtocolDTO toDto(Protocol protocol);

    @Mapping(source = "sampleId", target = "sample")
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
