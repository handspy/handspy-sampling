package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.constants.EntityNames;
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
 * REST controller for managing {@link pt.up.hs.sampling.domain.AnnotationType}.
 */
@RestController
@RequestMapping("/api/projects/{projectId}")
public class AnnotationTypeResource {

    private final Logger log = LoggerFactory.getLogger(AnnotationTypeResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnnotationTypeService annotationTypeService;
    private final AnnotationTypeQueryService annotationTypeQueryService;

    public AnnotationTypeResource(
        AnnotationTypeService annotationTypeService,
        AnnotationTypeQueryService annotationTypeQueryService
    ) {
        this.annotationTypeService = annotationTypeService;
        this.annotationTypeQueryService = annotationTypeQueryService;
    }

    /**
     * {@code POST  /annotation-types} : Create a new annotationType.
     *
     * @param projectId         ID of the project to which the annotation type belongs.
     * @param annotationTypeDTO the annotationTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new annotationTypeDTO, or with status {@code 400 (Bad Request)} if the annotationType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/annotation-types")
    public ResponseEntity<AnnotationTypeDTO> createAnnotationType(
        @PathVariable("projectId") Long projectId,
        @Valid @RequestBody AnnotationTypeDTO annotationTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AnnotationType {} in project {}", annotationTypeDTO, projectId);
        if (annotationTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new annotationType cannot already have an ID", EntityNames.ANNOTATION_TYPE, ErrorKeys.ERR_ID_EXISTS);
        }
        AnnotationTypeDTO result = annotationTypeService.save(projectId, annotationTypeDTO);
        return ResponseEntity.created(new URI("/api/annotation-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, EntityNames.ANNOTATION_TYPE, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /annotation-types} : Updates an existing annotationType.
     *
     * @param projectId         ID of the project to which the annotation types belong.
     * @param annotationTypeDTO the annotationTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annotationTypeDTO,
     * or with status {@code 400 (Bad Request)} if the annotationTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the annotationTypeDTO couldn't be updated.
     */
    @PutMapping("/annotation-types")
    public ResponseEntity<AnnotationTypeDTO> updateAnnotationType(
        @PathVariable("projectId") Long projectId,
        @Valid @RequestBody AnnotationTypeDTO annotationTypeDTO
    ) {
        log.debug("REST request to update AnnotationType {} in project {}", annotationTypeDTO, projectId);
        if (annotationTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", EntityNames.ANNOTATION_TYPE, ErrorKeys.ERR_ID_NULL);
        }
        AnnotationTypeDTO result = annotationTypeService.save(projectId, annotationTypeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, EntityNames.ANNOTATION_TYPE, annotationTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /annotation-types} : get all the annotationTypes.
     *
     * @param projectId ID of the project to which the annotation types belong.
     * @param pageable  the pagination information.
     * @param criteria  the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of annotationTypes in body.
     */
    @GetMapping("/annotation-types")
    public ResponseEntity<List<AnnotationTypeDTO>> getAllAnnotationTypes(
        @PathVariable("projectId") Long projectId,
        AnnotationTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get AnnotationTypes by criteria {} in project {}", criteria, projectId);
        Page<AnnotationTypeDTO> page = annotationTypeQueryService.findByCriteria(projectId, criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /annotation-types/count} : count all the annotationTypes.
     *
     * @param projectId ID of the project to which the annotation types belong.
     * @param criteria  the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/annotation-types/count")
    public ResponseEntity<Long> countAnnotationTypes(
        @PathVariable("projectId") Long projectId,
        AnnotationTypeCriteria criteria
    ) {
        log.debug("REST request to count AnnotationTypes by criteria {} in project {}", criteria, projectId);
        return ResponseEntity.ok().body(annotationTypeQueryService.countByCriteria(projectId, criteria));
    }

    /**
     * {@code GET  /annotation-types/:id} : get the "id" annotationType.
     *
     * @param projectId ID of the project to which the annotation type belongs.
     * @param id        the id of the annotationTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the annotationTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/annotation-types/{id}")
    public ResponseEntity<AnnotationTypeDTO> getAnnotationType(
        @PathVariable("projectId") Long projectId,
        @PathVariable Long id
    ) {
        log.debug("REST request to get AnnotationType {} in project {}", id, projectId);
        Optional<AnnotationTypeDTO> annotationTypeDTO = annotationTypeService.findOne(projectId, id);
        return ResponseUtil.wrapOrNotFound(annotationTypeDTO);
    }

    /**
     * {@code DELETE  /annotation-types/:id} : delete the "id" annotationType.
     *
     * @param projectId ID of the project to which the annotation type belongs.
     * @param id        the id of the annotationTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/annotation-types/{id}")
    public ResponseEntity<Void> deleteAnnotationType(
        @PathVariable("projectId") Long projectId,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete AnnotationType {} in project {}", id, projectId);
        annotationTypeService.delete(projectId, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, EntityNames.ANNOTATION_TYPE, id.toString()))
            .build();
    }
}
