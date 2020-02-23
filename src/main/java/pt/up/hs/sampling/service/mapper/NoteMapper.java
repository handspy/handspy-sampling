package pt.up.hs.sampling.service.mapper;


import pt.up.hs.sampling.domain.*;
import pt.up.hs.sampling.service.dto.NoteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Note} and its DTO {@link NoteDTO}.
 */
@Mapper(componentModel = "spring", uses = {SampleMapper.class})
public interface NoteMapper extends EntityMapper<NoteDTO, Note> {

    @Mapping(source = "sample.id", target = "sampleId")
    NoteDTO toDto(Note note);

    @Mapping(source = "sampleId", target = "sample")
    Note toEntity(NoteDTO noteDTO);

    default Note fromId(Long id) {
        if (id == null) {
            return null;
        }
        Note note = new Note();
        note.setId(id);
        return note;
    }
}
