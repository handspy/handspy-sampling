package pt.up.hs.sampling.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.up.hs.sampling.service.dto.StrokeDTO;

@Mapper(componentModel = "spring", uses = {UhcDotMapper.class})
public interface UhcStrokeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "protocolId", ignore = true)
    StrokeDTO uhcStrokeToStrokeDto(pt.up.hs.uhc.models.Stroke stroke);

    @Mapping(target = "metadata", ignore = true)
    pt.up.hs.uhc.models.Stroke strokeDtoToUhcStroke(StrokeDTO strokeDTO);
}
