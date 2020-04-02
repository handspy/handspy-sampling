package pt.up.hs.sampling.service.impl;

import pt.up.hs.sampling.service.NoteService;
import pt.up.hs.sampling.domain.Note;
import pt.up.hs.sampling.repository.NoteRepository;
import pt.up.hs.sampling.service.dto.NoteDTO;
import pt.up.hs.sampling.service.mapper.NoteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Note}.
 */
@Service
@Transactional
public class NoteServiceImpl implements NoteService {

    private final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);

    private final NoteRepository noteRepository;

    private final NoteMapper noteMapper;

    public NoteServiceImpl(NoteRepository noteRepository, NoteMapper noteMapper) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
    }

    /**
     * Save a note.
     *
     * @param projectId ID of the project to which the note belongs.
     * @param sampleId  ID of the sample to which the note belongs.
     * @param noteDTO   the entity to save.
     * @return the persisted entity.
     */
    @Override
    public NoteDTO save(Long projectId, Long sampleId, NoteDTO noteDTO) {
        log.debug("Request to save Note {} in sample {} of project {}", noteDTO, sampleId, projectId);
        noteDTO.setSampleId(sampleId);
        Note note = noteMapper.toEntity(noteDTO);
        note = noteRepository.save(note);
        return noteMapper.toDto(note);
    }

    /**
     * Get all the notes.
     *
     * @param projectId ID of the project to which the note belongs.
     * @param sampleId  ID of the sample to which the note belongs.
     * @param pageable  the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<NoteDTO> findAll(Long projectId, Long sampleId, Pageable pageable) {
        log.debug("Request to get all Notes in sample {} of project {}", sampleId, projectId);
        return noteRepository.findAllByProjectIdAndSampleId(projectId, sampleId, pageable)
            .map(noteMapper::toDto);
    }

    /**
     * Get the "id" note.
     *
     * @param projectId ID of the project to which the note belongs.
     * @param sampleId  ID of the sample to which the note belongs.
     * @param id        the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<NoteDTO> findOne(Long projectId, Long sampleId, Long id) {
        log.debug("Request to get Note {} in sample {} of project {}", id, sampleId, projectId);
        return noteRepository.findBySampleProjectIdAndSampleIdAndId(projectId, sampleId, id)
            .map(noteMapper::toDto);
    }

    /**
     * Delete the "id" note.
     *
     * @param projectId ID of the project to which the note belongs.
     * @param sampleId  ID of the sample to which the note belongs.
     * @param id        the id of the entity.
     */
    @Override
    public void delete(Long projectId, Long sampleId, Long id) {
        log.debug("Request to delete Note {} in sample {} of project {}", id, sampleId, projectId);
        noteRepository.deleteAllBySampleProjectIdAndSampleIdAndId(projectId, sampleId, id);
    }
}
