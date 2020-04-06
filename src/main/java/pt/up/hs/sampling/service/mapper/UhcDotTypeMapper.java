package pt.up.hs.sampling.service.mapper;

import org.mapstruct.Mapper;
import pt.up.hs.sampling.domain.enumeration.DotType;

@Mapper(componentModel = "spring")
public interface UhcDotTypeMapper {

    pt.up.hs.uhc.models.DotType dotTypeToUhcDotType(DotType dotType);

    DotType uhcDotTypeToDotType(pt.up.hs.uhc.models.DotType dotType);
}
