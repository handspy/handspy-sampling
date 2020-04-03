package pt.up.hs.sampling.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.up.hs.sampling.constants.EntityNames;
import pt.up.hs.sampling.constants.ErrorKeys;
import pt.up.hs.sampling.service.StrokeQueryService;
import pt.up.hs.sampling.service.StrokeService;
import pt.up.hs.sampling.service.dto.StrokeCriteria;
import pt.up.hs.sampling.service.dto.StrokeDTO;
import pt.up.hs.sampling.web.rest.errors.BadRequestAlertException;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.up.hs.sampling.domain.Stroke}.
 */
@RestController
@RequestMapping("/api/projects/{projectId}/protocols/{protocolId}")
public class StrokeResource {

    private final Logger log = LoggerFactory.getLogger(StrokeResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StrokeService strokeService;
    private final StrokeQueryService strokeQueryService;

    public StrokeResource(StrokeService strokeService, StrokeQueryService strokeQueryService) {
        this.strokeService = strokeService;
        this.strokeQueryService = strokeQueryService;
    }

    /**
     * {@code POST  /strokes} : Create a new stroke.
     *
     * @param projectId  ID of the project to which the stroke belongs.
     * @param protocolId ID of the protocol to which the stroke belongs.
     * @param strokeDTO  the strokeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new strokeDTO, or with status {@code 400 (Bad Request)} if the stroke has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/strokes")
    public ResponseEntity<StrokeDTO> createStroke(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        @Valid @RequestBody StrokeDTO strokeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save Stroke {} in protocol {} of project {}", strokeDTO, protocolId, projectId);
        if (strokeDTO.getId() != null) {
            throw new BadRequestAlertException("A new stroke cannot already have an ID", EntityNames.DOT, ErrorKeys.ERR_ID_EXISTS);
        }
        StrokeDTO result = strokeService.save(projectId, protocolId, strokeDTO);
        return ResponseEntity.created(new URI("/api/projects/" + projectId + "/protocols/" + protocolId + "/strokes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, EntityNames.DOT, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /strokes} : Updates an existing stroke.
     *
     * @param projectId  ID of the project to which the stroke belongs.
     * @param protocolId ID of the protocol to which the stroke belongs.
     * @param strokeDTO  the strokeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated strokeDTO,
     * or with status {@code 400 (Bad Request)} if the strokeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the strokeDTO couldn't be updated.
     */
    @PutMapping("/strokes")
    public ResponseEntity<StrokeDTO> updateStroke(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        @Valid @RequestBody StrokeDTO strokeDTO
    ) {
        log.debug("REST request to update Stroke {} in protocol {} of project {}", strokeDTO, protocolId, projectId);
        if (strokeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", EntityNames.DOT, ErrorKeys.ERR_ID_NULL);
        }
        StrokeDTO result = strokeService.save(projectId, protocolId, strokeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, EntityNames.DOT, strokeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /strokes} : get all the strokes.
     *
     * @param projectId  ID of the project to which the strokes belong.
     * @param protocolId ID of the protocol to which the strokes belong.
     * @param criteria   the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of strokes in body.
     */
    @GetMapping("/strokes")
    public ResponseEntity<List<StrokeDTO>> getAllStrokes(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        StrokeCriteria criteria
    ) {
        log.debug("REST request to get Strokes by criteria {} in protocol {} of project {}", criteria, protocolId, projectId);
        List<StrokeDTO> entityList = strokeQueryService.findByCriteria(projectId, protocolId, criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /strokes/count} : count all the strokes.
     *
     * @param projectId  ID of the project to which the strokes belong.
     * @param protocolId ID of the protocol to which the strokes belong.
     * @param criteria   the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/strokes/count")
    public ResponseEntity<Long> countStrokes(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        StrokeCriteria criteria
    ) {
        log.debug("REST request to count Strokes by criteria {} in protocol {} of project {}", criteria, protocolId, projectId);
        return ResponseEntity.ok()
            .body(strokeQueryService.countByCriteria(projectId, protocolId, criteria));
    }

    /**
     * {@code GET  /strokes/:id} : get the "id" stroke.
     *
     * @param projectId  ID of the project to which the stroke belongs.
     * @param protocolId ID of the protocol to which the stroke belongs.
     * @param id         the id of the strokeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the strokeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/strokes/{id}")
    public ResponseEntity<StrokeDTO> getStroke(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        @PathVariable Long id
    ) {
        log.debug("REST request to get Stroke {} in protocol {} of project {}", id, protocolId, projectId);
        Optional<StrokeDTO> strokeDTO = strokeService.findOne(projectId, protocolId, id);
        return ResponseUtil.wrapOrNotFound(strokeDTO);
    }

    /**
     * {@code DELETE  /strokes/:id} : delete the "id" stroke.
     *
     * @param projectId  ID of the project to which the stroke belongs.
     * @param protocolId ID of the protocol to which the stroke belongs.
     * @param id         the id of the strokeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/strokes/{id}")
    public ResponseEntity<Void> deleteStroke(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete Stroke {} in protocol {} of project {}", id, protocolId, projectId);
        strokeService.delete(projectId, protocolId, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, EntityNames.DOT, id.toString()))
            .build();
    }
}
