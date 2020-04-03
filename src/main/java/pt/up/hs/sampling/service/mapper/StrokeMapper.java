package pt.up.hs.sampling.service.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import pt.up.hs.sampling.domain.Dot;
import pt.up.hs.sampling.domain.Stroke;
import pt.up.hs.sampling.service.dto.StrokeDTO;

/**
 * Mapper for the entity {@link Stroke} and its DTO {@link StrokeDTO}.
 */
@Mapper(componentModel = "spring", uses = {DotMapper.class, ProtocolMapper.class})
public interface StrokeMapper extends EntityMapper<StrokeDTO, Stroke> {

    @Mapping(source = "protocol.id", target = "protocolId")
    @Mapping(source = "dots", target = "dots")
    StrokeDTO toDto(Stroke stroke);

    @Mapping(source = "protocolId", target = "protocol")
    @Mapping(source = "dots", target = "dots")
    Stroke toEntity(StrokeDTO strokeDTO);

    @AfterMapping
    default void setDotParent(@MappingTarget Stroke stroke) {
        for (Dot dot : stroke.getDots()) {
            dot.setStroke(stroke);
        }
    }

    default Stroke fromId(Long id) {
        if (id == null) {
            return null;
        }
        Stroke stroke = new Stroke();
        stroke.setId(id);
        return stroke;
    }
}
