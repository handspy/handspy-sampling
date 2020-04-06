package pt.up.hs.sampling.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.up.hs.sampling.service.dto.DotDTO;

@Mapper(componentModel = "spring", uses = {UhcDotTypeMapper.class})
public interface UhcDotMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "strokeId", ignore = true)
    DotDTO uhcDotToDotDto(pt.up.hs.uhc.models.Dot dot);

    @Mapping(target = "metadata", ignore = true)
    pt.up.hs.uhc.models.Dot dotDtoUhcDot(DotDTO dotDTO);
}
