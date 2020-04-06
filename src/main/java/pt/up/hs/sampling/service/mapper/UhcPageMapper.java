package pt.up.hs.sampling.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pt.up.hs.sampling.service.dto.ProtocolDTO;
import pt.up.hs.uhc.models.Page;

@Mapper(componentModel = "spring", uses = {UhcStrokeMapper.class})
public interface UhcPageMapper {

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "projectId", ignore = true)
    @Mapping(target = "sampleId", ignore = true)
    @Mapping(target = "pageNumber", ignore = true)
    ProtocolDTO uhcPageToProtocolDto(Page page);

    @Mapping(target = "metadata", ignore = true)
    Page protocolDtoToUhcPage(ProtocolDTO protocolDTO);
}
