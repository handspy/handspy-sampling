package pt.up.hs.sampling.service.mapper;


import pt.up.hs.sampling.domain.*;
import pt.up.hs.sampling.service.dto.SampleDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sample} and its DTO {@link SampleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SampleMapper extends EntityMapper<SampleDTO, Sample> {



    default Sample fromId(Long id) {
        if (id == null) {
            return null;
        }
        Sample sample = new Sample();
        sample.setId(id);
        return sample;
    }
}
