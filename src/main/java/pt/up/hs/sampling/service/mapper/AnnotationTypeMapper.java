package pt.up.hs.sampling.service.mapper;


import pt.up.hs.sampling.domain.*;
import pt.up.hs.sampling.service.dto.AnnotationTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link AnnotationType} and its DTO {@link AnnotationTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AnnotationTypeMapper extends EntityMapper<AnnotationTypeDTO, AnnotationType> {



    default AnnotationType fromId(Long id) {
        if (id == null) {
            return null;
        }
        AnnotationType annotationType = new AnnotationType();
        annotationType.setId(id);
        return annotationType;
    }
}
