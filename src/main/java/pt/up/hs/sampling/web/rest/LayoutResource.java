package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.service.LayoutService;
import pt.up.hs.sampling.web.rest.errors.BadRequestAlertException;
import pt.up.hs.sampling.service.dto.LayoutDTO;
import pt.up.hs.sampling.service.dto.LayoutCriteria;
import pt.up.hs.sampling.service.LayoutQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.up.hs.sampling.domain.Layout}.
 */
@RestController
@RequestMapping("/api")
public class LayoutResource {

    private final Logger log = LoggerFactory.getLogger(LayoutResource.class);

    private static final String ENTITY_NAME = "samplingLayout";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LayoutService layoutService;

    private final LayoutQueryService layoutQueryService;

    public LayoutResource(LayoutService layoutService, LayoutQueryService layoutQueryService) {
        this.layoutService = layoutService;
        this.layoutQueryService = layoutQueryService;
    }

    /**
     * {@code POST  /layouts} : Create a new layout.
     *
     * @param layoutDTO the layoutDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new layoutDTO, or with status {@code 400 (Bad Request)} if the layout has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/layouts")
    public ResponseEntity<LayoutDTO> createLayout(@Valid @RequestBody LayoutDTO layoutDTO) throws URISyntaxException {
        log.debug("REST request to save Layout : {}", layoutDTO);
        if (layoutDTO.getId() != null) {
            throw new BadRequestAlertException("A new layout cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LayoutDTO result = layoutService.save(layoutDTO);
        return ResponseEntity.created(new URI("/api/layouts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /layouts} : Updates an existing layout.
     *
     * @param layoutDTO the layoutDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated layoutDTO,
     * or with status {@code 400 (Bad Request)} if the layoutDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the layoutDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/layouts")
    public ResponseEntity<LayoutDTO> updateLayout(@Valid @RequestBody LayoutDTO layoutDTO) throws URISyntaxException {
        log.debug("REST request to update Layout : {}", layoutDTO);
        if (layoutDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LayoutDTO result = layoutService.save(layoutDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, layoutDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /layouts} : get all the layouts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of layouts in body.
     */
    @GetMapping("/layouts")
    public ResponseEntity<List<LayoutDTO>> getAllLayouts(LayoutCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Layouts by criteria: {}", criteria);
        Page<LayoutDTO> page = layoutQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /layouts/count} : count all the layouts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/layouts/count")
    public ResponseEntity<Long> countLayouts(LayoutCriteria criteria) {
        log.debug("REST request to count Layouts by criteria: {}", criteria);
        return ResponseEntity.ok().body(layoutQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /layouts/:id} : get the "id" layout.
     *
     * @param id the id of the layoutDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the layoutDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/layouts/{id}")
    public ResponseEntity<LayoutDTO> getLayout(@PathVariable Long id) {
        log.debug("REST request to get Layout : {}", id);
        Optional<LayoutDTO> layoutDTO = layoutService.findOne(id);
        return ResponseUtil.wrapOrNotFound(layoutDTO);
    }

    /**
     * {@code DELETE  /layouts/:id} : delete the "id" layout.
     *
     * @param id the id of the layoutDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/layouts/{id}")
    public ResponseEntity<Void> deleteLayout(@PathVariable Long id) {
        log.debug("REST request to delete Layout : {}", id);
        layoutService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
