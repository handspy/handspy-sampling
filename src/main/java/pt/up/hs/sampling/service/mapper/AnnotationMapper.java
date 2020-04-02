package pt.up.hs.sampling.service.mapper;


import pt.up.hs.sampling.domain.*;
import pt.up.hs.sampling.service.dto.AnnotationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Annotation} and its DTO {@link AnnotationDTO}.
 */
@Mapper(componentModel = "spring", uses = {TextMapper.class, AnnotationTypeMapper.class})
public interface AnnotationMapper extends EntityMapper<AnnotationDTO, Annotation> {

    @Mapping(source = "text.id", target = "textId")
    @Mapping(source = "annotationType.id", target = "annotationTypeId")
    AnnotationDTO toDto(Annotation annotation);

    @Mapping(source = "textId", target = "text")
    @Mapping(source = "annotationTypeId", target = "annotationType")
    Annotation toEntity(AnnotationDTO annotationDTO);

    default Annotation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Annotation annotation = new Annotation();
        annotation.setId(id);
        return annotation;
    }
}
