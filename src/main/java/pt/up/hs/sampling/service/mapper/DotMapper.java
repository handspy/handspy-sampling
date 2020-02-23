package pt.up.hs.sampling.service.mapper;


import pt.up.hs.sampling.domain.*;
import pt.up.hs.sampling.service.dto.DotDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dot} and its DTO {@link DotDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProtocolMapper.class})
public interface DotMapper extends EntityMapper<DotDTO, Dot> {

    @Mapping(source = "protocol.id", target = "protocolId")
    DotDTO toDto(Dot dot);

    @Mapping(source = "protocolId", target = "protocol")
    Dot toEntity(DotDTO dotDTO);

    default Dot fromId(Long id) {
        if (id == null) {
            return null;
        }
        Dot dot = new Dot();
        dot.setId(id);
        return dot;
    }
}
