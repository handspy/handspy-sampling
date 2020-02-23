package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.service.AnnotationService;
import pt.up.hs.sampling.web.rest.errors.BadRequestAlertException;
import pt.up.hs.sampling.service.dto.AnnotationDTO;
import pt.up.hs.sampling.service.dto.AnnotationCriteria;
import pt.up.hs.sampling.service.AnnotationQueryService;

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
 * REST controller for managing {@link pt.up.hs.sampling.domain.Annotation}.
 */
@RestController
@RequestMapping("/api")
public class AnnotationResource {

    private final Logger log = LoggerFactory.getLogger(AnnotationResource.class);

    private static final String ENTITY_NAME = "samplingAnnotation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnnotationService annotationService;

    private final AnnotationQueryService annotationQueryService;

    public AnnotationResource(AnnotationService annotationService, AnnotationQueryService annotationQueryService) {
        this.annotationService = annotationService;
        this.annotationQueryService = annotationQueryService;
    }

    /**
     * {@code POST  /annotations} : Create a new annotation.
     *
     * @param annotationDTO the annotationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new annotationDTO, or with status {@code 400 (Bad Request)} if the annotation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/annotations")
    public ResponseEntity<AnnotationDTO> createAnnotation(@Valid @RequestBody AnnotationDTO annotationDTO) throws URISyntaxException {
        log.debug("REST request to save Annotation : {}", annotationDTO);
        if (annotationDTO.getId() != null) {
            throw new BadRequestAlertException("A new annotation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnnotationDTO result = annotationService.save(annotationDTO);
        return ResponseEntity.created(new URI("/api/annotations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /annotations} : Updates an existing annotation.
     *
     * @param annotationDTO the annotationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annotationDTO,
     * or with status {@code 400 (Bad Request)} if the annotationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the annotationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/annotations")
    public ResponseEntity<AnnotationDTO> updateAnnotation(@Valid @RequestBody AnnotationDTO annotationDTO) throws URISyntaxException {
        log.debug("REST request to update Annotation : {}", annotationDTO);
        if (annotationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AnnotationDTO result = annotationService.save(annotationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, annotationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /annotations} : get all the annotations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of annotations in body.
     */
    @GetMapping("/annotations")
    public ResponseEntity<List<AnnotationDTO>> getAllAnnotations(AnnotationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Annotations by criteria: {}", criteria);
        Page<AnnotationDTO> page = annotationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /annotations/count} : count all the annotations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/annotations/count")
    public ResponseEntity<Long> countAnnotations(AnnotationCriteria criteria) {
        log.debug("REST request to count Annotations by criteria: {}", criteria);
        return ResponseEntity.ok().body(annotationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /annotations/:id} : get the "id" annotation.
     *
     * @param id the id of the annotationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the annotationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/annotations/{id}")
    public ResponseEntity<AnnotationDTO> getAnnotation(@PathVariable Long id) {
        log.debug("REST request to get Annotation : {}", id);
        Optional<AnnotationDTO> annotationDTO = annotationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(annotationDTO);
    }

    /**
     * {@code DELETE  /annotations/:id} : delete the "id" annotation.
     *
     * @param id the id of the annotationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/annotations/{id}")
    public ResponseEntity<Void> deleteAnnotation(@PathVariable Long id) {
        log.debug("REST request to delete Annotation : {}", id);
        annotationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
