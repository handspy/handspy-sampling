package pt.up.hs.sampling.web.rest;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/projects/{projectId}")
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
     * @param noteDTO   the noteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new noteDTO, or with status {@code 400 (Bad Request)} if the note has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notes")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'WRITE')"
    )
    public ResponseEntity<NoteDTO> createNote(
        @PathVariable("projectId") Long projectId,
        @Valid @RequestBody NoteDTO noteDTO
    ) throws URISyntaxException {
        log.debug("REST request to save Note {} of project {}", noteDTO, projectId);
        if (noteDTO.getId() != null) {
            throw new BadRequestAlertException("A new note cannot already have an ID", EntityNames.NOTE, ErrorKeys.ERR_ID_EXISTS);
        }
        NoteDTO result = noteService.save(projectId, noteDTO);
        return ResponseEntity.created(new URI("/api/projects/" + projectId + "/notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, EntityNames.NOTE, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notes} : Updates an existing note.
     *
     * @param projectId ID of the project to which the note belongs.
     * @param noteDTO   the noteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated noteDTO,
     * or with status {@code 400 (Bad Request)} if the noteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the noteDTO couldn't be updated.
     */
    @PutMapping("/notes")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'WRITE')"
    )
    public ResponseEntity<NoteDTO> updateNote(
        @PathVariable("projectId") Long projectId,
        @Valid @RequestBody NoteDTO noteDTO
    ) {
        log.debug("REST request to update Note {} of project {}", noteDTO, projectId);
        if (noteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", EntityNames.NOTE, ErrorKeys.ERR_ID_NULL);
        }
        NoteDTO result = noteService.save(projectId, noteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, EntityNames.NOTE, noteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /notes} : get all the notes.
     *
     * @param projectId ID of the project to which the notes belong.
     * @param criteria  the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notes in body.
     */
    @GetMapping("/notes")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'READ')"
    )
    @PostAuthorize("@noteServiceImpl.filterAnonymous(returnObject.getBody())")
    public ResponseEntity<List<NoteDTO>> getAllNotes(
        @PathVariable("projectId") Long projectId,
        NoteCriteria criteria
    ) {
        log.debug("REST request to get Notes by criteria {} of project {}", criteria, projectId);
        List<NoteDTO> notes = noteQueryService.findByCriteria(projectId, criteria);
        return ResponseEntity.ok().body(notes);
    }

    /**
     * {@code GET  /notes/count} : count all the notes.
     *
     * @param projectId ID of the project to which the notes belong.
     * @param criteria  the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/notes/count")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'READ')"
    )
    public ResponseEntity<Long> countNotes(
        @PathVariable("projectId") Long projectId,
        NoteCriteria criteria
    ) {
        log.debug("REST request to count Notes by criteria {} of project {}", criteria, projectId);
        return ResponseEntity.ok().body(noteQueryService.countByCriteria(projectId, criteria));
    }

    /**
     * {@code GET  /notes/:id} : get the "id" note.
     *
     * @param projectId ID of the project to which the note belongs.
     * @param id        the id of the noteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the noteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notes/{id}")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'READ')"
    )
    public ResponseEntity<NoteDTO> getNote(
        @PathVariable("projectId") Long projectId,
        @PathVariable Long id
    ) {
        log.debug("REST request to get Note {} of project {}", id, projectId);
        Optional<NoteDTO> noteDTO = noteService.findOne(projectId, id);
        return ResponseUtil.wrapOrNotFound(noteDTO);
    }

    /**
     * {@code DELETE  /notes/:id} : delete the "id" note.
     *
     * @param projectId ID of the project to which the note belongs.
     * @param id        the id of the noteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notes/{id}")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'WRITE')"
    )
    public ResponseEntity<Void> deleteNote(
        @PathVariable("projectId") Long projectId,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete Note {} of project {}", id, projectId);
        noteService.delete(projectId, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, EntityNames.NOTE, id.toString()))
            .build();
    }
}
