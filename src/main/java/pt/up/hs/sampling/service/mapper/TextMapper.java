package pt.up.hs.sampling.service.mapper;

import pt.up.hs.sampling.domain.*;
import pt.up.hs.sampling.service.dto.TextDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Text} and its DTO {@link TextDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProtocolMapper.class, AnnotationMapper.class})
public interface TextMapper extends EntityMapper<TextDTO, Text> {

    @Mapping(source = "annotations", target = "annotations")
    TextDTO toDto(Text text);

    @Mapping(source = "annotations", target = "annotations")
    Text toEntity(TextDTO textDTO);

    default Text fromId(Long id) {
        if (id == null) {
            return null;
        }
        Text text = new Text();
        text.setId(id);
        return text;
    }
}
