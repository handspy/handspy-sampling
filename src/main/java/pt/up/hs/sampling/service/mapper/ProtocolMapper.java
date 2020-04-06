package pt.up.hs.sampling.service.mapper;

import pt.up.hs.sampling.domain.*;
import pt.up.hs.sampling.service.dto.ProtocolDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Protocol} and its DTO {@link ProtocolDTO}.
 */
@Mapper(componentModel = "spring", uses = {StrokeMapper.class, SampleMapper.class})
public interface ProtocolMapper extends EntityMapper<ProtocolDTO, Protocol> {

    @Mapping(source = "sample.id", target = "sampleId")
    @Mapping(source = "strokes", target = "strokes")
    ProtocolDTO toDto(Protocol protocol);

    @Mapping(target = "dirtyPreview", constant = "true")
    @Mapping(source = "sampleId", target = "sample")
    @Mapping(source = "strokes", target = "strokes")
    Protocol toEntity(ProtocolDTO protocolDTO);

    @AfterMapping
    default void setStrokeParent(@MappingTarget Protocol protocol) {
        for (Stroke stroke : protocol.getStrokes()) {
            stroke.setProtocol(protocol);
        }
    }

    default Protocol fromId(Long id) {
        if (id == null) {
            return null;
        }
        Protocol protocol = new Protocol();
        protocol.setId(id);
        return protocol;
    }
}
