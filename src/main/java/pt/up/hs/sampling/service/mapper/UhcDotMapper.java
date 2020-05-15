package pt.up.hs.sampling.service.mapper;

import org.mapstruct.Mapper;
import pt.up.hs.sampling.domain.pojo.Dot;

@Mapper(componentModel = "spring", uses = {UhcDotTypeMapper.class})
public interface UhcDotMapper {

    Dot uhcDotToDotDto(pt.up.hs.uhc.models.Dot dot);

    pt.up.hs.uhc.models.Dot dotDtoUhcDot(Dot dot);
}
