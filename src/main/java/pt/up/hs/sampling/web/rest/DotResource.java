package pt.up.hs.sampling.web.rest;

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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.up.hs.sampling.domain.Dot}.
 */
@RestController
@RequestMapping("/api")
public class DotResource {

    private final Logger log = LoggerFactory.getLogger(DotResource.class);

    private static final String ENTITY_NAME = "samplingDot";

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
     * @param dotDTO the dotDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dotDTO, or with status {@code 400 (Bad Request)} if the dot has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dots")
    public ResponseEntity<DotDTO> createDot(@Valid @RequestBody DotDTO dotDTO) throws URISyntaxException {
        log.debug("REST request to save Dot : {}", dotDTO);
        if (dotDTO.getId() != null) {
            throw new BadRequestAlertException("A new dot cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DotDTO result = dotService.save(dotDTO);
        return ResponseEntity.created(new URI("/api/dots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dots} : Updates an existing dot.
     *
     * @param dotDTO the dotDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dotDTO,
     * or with status {@code 400 (Bad Request)} if the dotDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dotDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dots")
    public ResponseEntity<DotDTO> updateDot(@Valid @RequestBody DotDTO dotDTO) throws URISyntaxException {
        log.debug("REST request to update Dot : {}", dotDTO);
        if (dotDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DotDTO result = dotService.save(dotDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dotDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /dots} : get all the dots.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dots in body.
     */
    @GetMapping("/dots")
    public ResponseEntity<List<DotDTO>> getAllDots(DotCriteria criteria) {
        log.debug("REST request to get Dots by criteria: {}", criteria);
        List<DotDTO> entityList = dotQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /dots/count} : count all the dots.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/dots/count")
    public ResponseEntity<Long> countDots(DotCriteria criteria) {
        log.debug("REST request to count Dots by criteria: {}", criteria);
        return ResponseEntity.ok().body(dotQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dots/:id} : get the "id" dot.
     *
     * @param id the id of the dotDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dotDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dots/{id}")
    public ResponseEntity<DotDTO> getDot(@PathVariable Long id) {
        log.debug("REST request to get Dot : {}", id);
        Optional<DotDTO> dotDTO = dotService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dotDTO);
    }

    /**
     * {@code DELETE  /dots/:id} : delete the "id" dot.
     *
     * @param id the id of the dotDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dots/{id}")
    public ResponseEntity<Void> deleteDot(@PathVariable Long id) {
        log.debug("REST request to delete Dot : {}", id);
        dotService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
