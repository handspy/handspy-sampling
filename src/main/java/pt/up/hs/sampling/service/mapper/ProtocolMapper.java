package pt.up.hs.sampling.service.mapper;

import pt.up.hs.sampling.domain.*;
import pt.up.hs.sampling.service.dto.ProtocolDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Protocol} and its DTO {@link ProtocolDTO}.
 */
@Mapper(componentModel = "spring", uses = {DotMapper.class, SampleMapper.class})
public interface ProtocolMapper extends EntityMapper<ProtocolDTO, Protocol> {

    @Mapping(source = "sample.id", target = "sampleId")
    @Mapping(source = "dots", target = "dots")
    ProtocolDTO toDto(Protocol protocol);

    @Mapping(source = "sampleId", target = "sample")
    @Mapping(source = "dots", target = "dots")
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
