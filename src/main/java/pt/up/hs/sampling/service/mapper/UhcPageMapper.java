package pt.up.hs.sampling.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.up.hs.sampling.domain.ProtocolData;
import pt.up.hs.sampling.service.dto.ProtocolDTO;
import pt.up.hs.sampling.service.dto.ProtocolDataDTO;
import pt.up.hs.uhc.models.Page;

@Mapper(componentModel = "spring", uses = {UhcStrokeMapper.class})
public interface UhcPageMapper {

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "protocol", ignore = true)
    @Mapping(target = "dirtyPreview", constant = "true")
    ProtocolData uhcPageToProtocolData(Page page);

    @Mapping(target = "metadata", ignore = true)
    Page protocolDataToUhcPage(ProtocolData pd);
}
