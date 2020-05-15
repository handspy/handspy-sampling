package pt.up.hs.sampling.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.up.hs.sampling.domain.pojo.Stroke;

@Mapper(componentModel = "spring", uses = {UhcDotMapper.class})
public interface UhcStrokeMapper {

    Stroke uhcStrokeToStroke(pt.up.hs.uhc.models.Stroke stroke);

    pt.up.hs.uhc.models.Stroke strokeToUhcStroke(Stroke stroke);
}
