package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.constants.EntityNames;
import pt.up.hs.sampling.service.DotService;
import pt.up.hs.sampling.web.rest.errors.BadRequestAlertException;
import pt.up.hs.sampling.service.dto.DotDTO;
import pt.up.hs.sampling.service.dto.DotCriteria;
import pt.up.hs.sampling.service.DotQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.up.hs.sampling.constants.ErrorKeys;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.up.hs.sampling.domain.Dot}.
 */
@RestController
@RequestMapping("/api/projects/{projectId}/protocols/{protocolId}")
public class DotResource {

    private final Logger log = LoggerFactory.getLogger(DotResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DotService dotService;
    private final DotQueryService dotQueryService;

    public DotResource(DotService dotService, DotQueryService dotQueryService) {
        this.dotService = dotService;
        this.dotQueryService = dotQueryService;
    }

    /**
     * {@code POST  /dots} : Create a new dot.
     *
     * @param projectId ID of the project to which the dot belongs.
     * @param protocolId  ID of the protocol to which the dot belongs.
     * @param dotDTO the dotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dotDTO, or with status {@code 400 (Bad Request)} if the dot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dots")
    public ResponseEntity<DotDTO> createDot(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        @Valid @RequestBody DotDTO dotDTO
    ) throws URISyntaxException {
        log.debug("REST request to save Dot {} in protocol {} of project {}", dotDTO, protocolId, projectId);
        if (dotDTO.getId() != null) {
            throw new BadRequestAlertException("A new dot cannot already have an ID", EntityNames.DOT, ErrorKeys.ERR_ID_EXISTS);
        }
        DotDTO result = dotService.save(projectId, protocolId, dotDTO);
        return ResponseEntity.created(new URI("/api/projects/" + projectId + "/protocols/" + protocolId + "/dots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, EntityNames.DOT, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dots} : Updates an existing dot.
     *
     * @param projectId ID of the project to which the dot belongs.
     * @param protocolId  ID of the protocol to which the dot belongs.
     * @param dotDTO the dotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dotDTO,
     * or with status {@code 400 (Bad Request)} if the dotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dotDTO couldn't be updated.
     */
    @PutMapping("/dots")
    public ResponseEntity<DotDTO> updateDot(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        @Valid @RequestBody DotDTO dotDTO
    ) {
        log.debug("REST request to update Dot {} in protocol {} of project {}", dotDTO, protocolId, projectId);
        if (dotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", EntityNames.DOT, ErrorKeys.ERR_ID_NULL);
        }
        DotDTO result = dotService.save(projectId, protocolId, dotDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, EntityNames.DOT, dotDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /dots} : get all the dots.
     *
     * @param projectId ID of the project to which the dots belong.
     * @param protocolId  ID of the protocol to which the dots belong.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dots in body.
     */
    @GetMapping("/dots")
    public ResponseEntity<List<DotDTO>> getAllDots(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        DotCriteria criteria
    ) {
        log.debug("REST request to get Dots by criteria {} in protocol {} of project {}", criteria, protocolId, projectId);
        List<DotDTO> entityList = dotQueryService.findByCriteria(projectId, protocolId, criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /dots/count} : count all the dots.
     *
     * @param projectId ID of the project to which the dots belong.
     * @param protocolId  ID of the protocol to which the dots belong.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/dots/count")
    public ResponseEntity<Long> countDots(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        DotCriteria criteria
    ) {
        log.debug("REST request to count Dots by criteria {} in protocol {} of project {}", criteria, protocolId, projectId);
        return ResponseEntity.ok()
            .body(dotQueryService.countByCriteria(projectId, protocolId, criteria));
    }

    /**
     * {@code GET  /dots/:id} : get the "id" dot.
     *
     * @param projectId ID of the project to which the dot belongs.
     * @param protocolId  ID of the protocol to which the dot belongs.
     * @param id the id of the dotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dots/{id}")
    public ResponseEntity<DotDTO> getDot(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        @PathVariable Long id
    ) {
        log.debug("REST request to get Dot {} in protocol {} of project {}", id, protocolId, projectId);
        Optional<DotDTO> dotDTO = dotService.findOne(projectId, protocolId, id);
        return ResponseUtil.wrapOrNotFound(dotDTO);
    }

    /**
     * {@code DELETE  /dots/:id} : delete the "id" dot.
     *
     * @param projectId ID of the project to which the dot belongs.
     * @param protocolId  ID of the protocol to which the dot belongs.
     * @param id the id of the dotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dots/{id}")
    public ResponseEntity<Void> deleteDot(
        @PathVariable("projectId") Long projectId,
        @PathVariable("protocolId") Long protocolId,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete Dot {} in protocol {} of project {}", id, protocolId, projectId);
        dotService.delete(projectId, protocolId, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, EntityNames.DOT, id.toString()))
            .build();
    }
}
