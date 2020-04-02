package pt.up.hs.sampling.service;

import pt.up.hs.sampling.service.dto.NoteDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link pt.up.hs.sampling.domain.Note}.
 */
public interface NoteService {

    /**
     * Save a note.
     *
     * @param projectId ID of the project to which the note belongs.
     * @param sampleId  ID of the sample to which the note belongs.
     * @param noteDTO the entity to save.
     * @return the persisted entity.
     */
    NoteDTO save(Long projectId, Long sampleId, NoteDTO noteDTO);

    /**
     * Get all the notes.
     *
     * @param projectId ID of the project to which the notes belong.
     * @param sampleId  ID of the sample to which the notes belong.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<NoteDTO> findAll(Long projectId, Long sampleId, Pageable pageable);

    /**
     * Get the "id" note.
     *
     * @param projectId ID of the project to which the note belongs.
     * @param sampleId  ID of the sample to which the note belongs.
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NoteDTO> findOne(Long projectId, Long sampleId, Long id);

    /**
     * Delete the "id" note.
     *
     * @param projectId ID of the project to which the note belongs.
     * @param sampleId  ID of the sample to which the note belongs.
     * @param id the id of the entity.
     */
    void delete(Long projectId, Long sampleId, Long id);
}
