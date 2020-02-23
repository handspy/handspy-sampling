package pt.up.hs.sampling.service.mapper;


import pt.up.hs.sampling.domain.*;
import pt.up.hs.sampling.service.dto.LayoutDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Layout} and its DTO {@link LayoutDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface LayoutMapper extends EntityMapper<LayoutDTO, Layout> {



    default Layout fromId(Long id) {
        if (id == null) {
            return null;
        }
        Layout layout = new Layout();
        layout.setId(id);
        return layout;
    }
}
