package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.service.AnnotationTypeService;
import pt.up.hs.sampling.web.rest.errors.BadRequestAlertException;
import pt.up.hs.sampling.service.dto.AnnotationTypeDTO;
import pt.up.hs.sampling.service.dto.AnnotationTypeCriteria;
import pt.up.hs.sampling.service.AnnotationTypeQueryService;

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
 * REST controller for managing {@link pt.up.hs.sampling.domain.AnnotationType}.
 */
@RestController
@RequestMapping("/api")
public class AnnotationTypeResource {

    private final Logger log = LoggerFactory.getLogger(AnnotationTypeResource.class);

    private static final String ENTITY_NAME = "samplingAnnotationType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnnotationTypeService annotationTypeService;

    private final AnnotationTypeQueryService annotationTypeQueryService;

    public AnnotationTypeResource(AnnotationTypeService annotationTypeService, AnnotationTypeQueryService annotationTypeQueryService) {
        this.annotationTypeService = annotationTypeService;
        this.annotationTypeQueryService = annotationTypeQueryService;
    }

    /**
     * {@code POST  /annotation-types} : Create a new annotationType.
     *
     * @param annotationTypeDTO the annotationTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new annotationTypeDTO, or with status {@code 400 (Bad Request)} if the annotationType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/annotation-types")
    public ResponseEntity<AnnotationTypeDTO> createAnnotationType(@Valid @RequestBody AnnotationTypeDTO annotationTypeDTO) throws URISyntaxException {
        log.debug("REST request to save AnnotationType : {}", annotationTypeDTO);
        if (annotationTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new annotationType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnnotationTypeDTO result = annotationTypeService.save(annotationTypeDTO);
        return ResponseEntity.created(new URI("/api/annotation-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /annotation-types} : Updates an existing annotationType.
     *
     * @param annotationTypeDTO the annotationTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annotationTypeDTO,
     * or with status {@code 400 (Bad Request)} if the annotationTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the annotationTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/annotation-types")
    public ResponseEntity<AnnotationTypeDTO> updateAnnotationType(@Valid @RequestBody AnnotationTypeDTO annotationTypeDTO) throws URISyntaxException {
        log.debug("REST request to update AnnotationType : {}", annotationTypeDTO);
        if (annotationTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnnotationTypeDTO result = annotationTypeService.save(annotationTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, annotationTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /annotation-types} : get all the annotationTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of annotationTypes in body.
     */
    @GetMapping("/annotation-types")
    public ResponseEntity<List<AnnotationTypeDTO>> getAllAnnotationTypes(AnnotationTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AnnotationTypes by criteria: {}", criteria);
        Page<AnnotationTypeDTO> page = annotationTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /annotation-types/count} : count all the annotationTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/annotation-types/count")
    public ResponseEntity<Long> countAnnotationTypes(AnnotationTypeCriteria criteria) {
        log.debug("REST request to count AnnotationTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(annotationTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /annotation-types/:id} : get the "id" annotationType.
     *
     * @param id the id of the annotationTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the annotationTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/annotation-types/{id}")
    public ResponseEntity<AnnotationTypeDTO> getAnnotationType(@PathVariable Long id) {
        log.debug("REST request to get AnnotationType : {}", id);
        Optional<AnnotationTypeDTO> annotationTypeDTO = annotationTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(annotationTypeDTO);
    }

    /**
     * {@code DELETE  /annotation-types/:id} : delete the "id" annotationType.
     *
     * @param id the id of the annotationTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/annotation-types/{id}")
    public ResponseEntity<Void> deleteAnnotationType(@PathVariable Long id) {
        log.debug("REST request to delete AnnotationType : {}", id);
        annotationTypeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
