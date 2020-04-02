package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.constants.EntityNames;
import pt.up.hs.sampling.service.NoteService;
import pt.up.hs.sampling.web.rest.errors.BadRequestAlertException;
import pt.up.hs.sampling.service.dto.NoteDTO;
import pt.up.hs.sampling.service.dto.NoteCriteria;
import pt.up.hs.sampling.service.NoteQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.up.hs.sampling.constants.ErrorKeys;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.up.hs.sampling.domain.Note}.
 */
@RestController
@RequestMapping("/api/projects/{projectId}/samples/{sampleId}")
public class NoteResource {

    private final Logger log = LoggerFactory.getLogger(NoteResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NoteService noteService;

    private final NoteQueryService noteQueryService;

    public NoteResource(NoteService noteService, NoteQueryService noteQueryService) {
        this.noteService = noteService;
        this.noteQueryService = noteQueryService;
    }

    /**
     * {@code POST  /notes} : Create a new note.
     *
     * @param projectId ID of the project to which the note belongs.
     * @param sampleId  ID of the sample to which the note belongs.
     * @param noteDTO   the noteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new noteDTO, or with status {@code 400 (Bad Request)} if the note has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notes")
    public ResponseEntity<NoteDTO> createNote(
        @PathVariable("projectId") Long projectId,
        @PathVariable("sampleId") Long sampleId,
        @Valid @RequestBody NoteDTO noteDTO
    ) throws URISyntaxException {
        log.debug("REST request to save Note {} in sample {} of project {}", noteDTO, sampleId, projectId);
        if (noteDTO.getId() != null) {
            throw new BadRequestAlertException("A new note cannot already have an ID", EntityNames.NOTE, ErrorKeys.ERR_ID_EXISTS);
        }
        NoteDTO result = noteService.save(projectId, sampleId, noteDTO);
        return ResponseEntity.created(new URI("/api/projects/" + projectId + "/samples/" + sampleId + "/notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, EntityNames.NOTE, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notes} : Updates an existing note.
     *
     * @param projectId ID of the project to which the note belongs.
     * @param sampleId  ID of the sample to which the note belongs.
     * @param noteDTO   the noteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated noteDTO,
     * or with status {@code 400 (Bad Request)} if the noteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the noteDTO couldn't be updated.
     */
    @PutMapping("/notes")
    public ResponseEntity<NoteDTO> updateNote(
        @PathVariable("projectId") Long projectId,
        @PathVariable("sampleId") Long sampleId,
        @Valid @RequestBody NoteDTO noteDTO
    ) {
        log.debug("REST request to update Note {} in sample {} of project {}", noteDTO, sampleId, projectId);
        if (noteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", EntityNames.NOTE, ErrorKeys.ERR_ID_NULL);
        }
        NoteDTO result = noteService.save(projectId, sampleId, noteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, EntityNames.NOTE, noteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /notes} : get all the notes.
     *
     * @param projectId ID of the project to which the notes belong.
     * @param sampleId  ID of the sample to which the notes belong.
     * @param pageable  the pagination information.
     * @param criteria  the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notes in body.
     */
    @GetMapping("/notes")
    public ResponseEntity<List<NoteDTO>> getAllNotes(
        @PathVariable("projectId") Long projectId,
        @PathVariable("sampleId") Long sampleId,
        NoteCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get Notes by criteria {} in sample {} of project {}", criteria, sampleId, projectId);
        Page<NoteDTO> page = noteQueryService.findByCriteria(projectId, sampleId, criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /notes/count} : count all the notes.
     *
     * @param projectId ID of the project to which the notes belong.
     * @param sampleId  ID of the sample to which the notes belong.
     * @param criteria  the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/notes/count")
    public ResponseEntity<Long> countNotes(
        @PathVariable("projectId") Long projectId,
        @PathVariable("sampleId") Long sampleId,
        NoteCriteria criteria
    ) {
        log.debug("REST request to count Notes by criteria {} in sample {} of project {}", criteria, sampleId, projectId);
        return ResponseEntity.ok().body(noteQueryService.countByCriteria(projectId, sampleId, criteria));
    }

    /**
     * {@code GET  /notes/:id} : get the "id" note.
     *
     * @param projectId ID of the project to which the note belongs.
     * @param sampleId  ID of the sample to which the note belongs.
     * @param id        the id of the noteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the noteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notes/{id}")
    public ResponseEntity<NoteDTO> getNote(
        @PathVariable("projectId") Long projectId,
        @PathVariable("sampleId") Long sampleId,
        @PathVariable Long id
    ) {
        log.debug("REST request to get Note {} in sample {} of project {}", id, sampleId, projectId);
        Optional<NoteDTO> noteDTO = noteService.findOne(projectId, sampleId, id);
        return ResponseUtil.wrapOrNotFound(noteDTO);
    }

    /**
     * {@code DELETE  /notes/:id} : delete the "id" note.
     *
     * @param projectId ID of the project to which the note belongs.
     * @param sampleId  ID of the sample to which the note belongs.
     * @param id        the id of the noteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notes/{id}")
    public ResponseEntity<Void> deleteNote(
        @PathVariable("projectId") Long projectId,
        @PathVariable("sampleId") Long sampleId,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete Note {} in sample {} of project {}", id, sampleId, projectId);
        noteService.delete(projectId, sampleId, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, EntityNames.NOTE, id.toString()))
            .build();
    }
}
