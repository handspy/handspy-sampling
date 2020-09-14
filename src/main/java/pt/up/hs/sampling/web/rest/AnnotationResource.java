package pt.up.hs.sampling.web.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import pt.up.hs.sampling.constants.EntityNames;
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
 * REST controller for managing {@link pt.up.hs.sampling.domain.Annotation}.
 */
@RestController
@RequestMapping("/api/projects/{projectId}/texts/{textId}")
public class AnnotationResource {

    private final Logger log = LoggerFactory.getLogger(AnnotationResource.class);

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
     * @param projectId     ID of the project to which the annotation belongs.
     * @param textId        ID of the text to which the annotation belongs.
     * @param annotationDTO the annotationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new annotationDTO, or with status {@code 400 (Bad Request)} if the annotation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/annotations")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
        "hasPermission(#projectId, 'Project', 'WRITE')"
    )
    public ResponseEntity<AnnotationDTO> createAnnotation(
        @PathVariable("projectId") Long projectId,
        @PathVariable("textId") Long textId,
        @Valid @RequestBody AnnotationDTO annotationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save Annotation {} in text {} of project {}", annotationDTO, textId, projectId);
        if (annotationDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new annotation cannot already have an ID",
                EntityNames.ANNOTATION, ErrorKeys.ERR_ID_EXISTS);
        }
        AnnotationDTO result = annotationService.save(projectId, textId, annotationDTO);
        return ResponseEntity.created(new URI("/api/projects/" + projectId + "/texts/" + textId + "/annotations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, EntityNames.ANNOTATION, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /annotations} : Updates an existing annotation.
     *
     * @param projectId     ID of the project to which the annotation belongs.
     * @param textId        ID of the text to which the annotation belongs.
     * @param annotationDTO the annotationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annotationDTO,
     * or with status {@code 400 (Bad Request)} if the annotationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the annotationDTO couldn't be updated.
     */
    @PutMapping("/annotations")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
        "hasPermission(#projectId, 'Project', 'WRITE')"
    )
    public ResponseEntity<AnnotationDTO> updateAnnotation(
        @PathVariable("projectId") Long projectId,
        @PathVariable("textId") Long textId,
        @Valid @RequestBody AnnotationDTO annotationDTO
    ) {
        log.debug("REST request to update Annotation {} in text {} of project {}", annotationDTO, textId, projectId);
        if (annotationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", EntityNames.ANNOTATION, ErrorKeys.ERR_ID_NULL);
        }
        AnnotationDTO result = annotationService.save(projectId, textId, annotationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, EntityNames.ANNOTATION, annotationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /annotations} : get all the annotations.
     *
     * @param projectId ID of the project to which the annotations belong.
     * @param textId    ID of the text to which the annotations belong.
     * @param pageable  the pagination information.
     * @param criteria  the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of annotations in body.
     */
    @GetMapping("/annotations")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'READ')"
    )
    public ResponseEntity<List<AnnotationDTO>> getAllAnnotations(
        @PathVariable("projectId") Long projectId,
        @PathVariable("textId") Long textId,
        AnnotationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get Annotations by criteria {} in text {} of project {}", criteria, textId, projectId);
        Page<AnnotationDTO> page = annotationQueryService.findByCriteria(projectId, textId, criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /annotations/count} : count all the annotations.
     *
     * @param projectId ID of the project to which the annotations belong.
     * @param textId    ID of the text to which the annotations belong.
     * @param criteria  the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/annotations/count")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'READ')"
    )
    public ResponseEntity<Long> countAnnotations(
        @PathVariable("projectId") Long projectId,
        @PathVariable("textId") Long textId,
        AnnotationCriteria criteria
    ) {
        log.debug("REST request to count Annotations by criteria {} in text {} of project {}", criteria, textId, projectId);
        return ResponseEntity.ok().body(annotationQueryService.countByCriteria(projectId, textId, criteria));
    }

    /**
     * {@code GET  /annotations/:id} : get the "id" annotation.
     *
     * @param projectId ID of the project to which the annotation belongs.
     * @param textId    ID of the text to which the annotation belongs.
     * @param id        the id of the annotationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the annotationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/annotations/{id}")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'READ')"
    )
    public ResponseEntity<AnnotationDTO> getAnnotation(
        @PathVariable("projectId") Long projectId,
        @PathVariable("textId") Long textId,
        @PathVariable Long id
    ) {
        log.debug("REST request to get Annotation {} in text {} of project {}", id, textId, projectId);
        Optional<AnnotationDTO> annotationDTO = annotationService.findOne(projectId, textId, id);
        return ResponseUtil.wrapOrNotFound(annotationDTO);
    }

    /**
     * {@code DELETE  /annotations/:id} : delete the "id" annotation.
     *
     * @param projectId ID of the project to which the annotation belongs.
     * @param textId    ID of the text to which the annotation belongs.
     * @param id        the id of the annotationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/annotations/{id}")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
        "hasPermission(#projectId, 'Project', 'WRITE')"
    )
    public ResponseEntity<Void> deleteAnnotation(
        @PathVariable("projectId") Long projectId,
        @PathVariable("textId") Long textId,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete Annotation {} in text {} of project {}", id, textId, projectId);
        annotationService.delete(projectId, textId, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, EntityNames.ANNOTATION, id.toString()))
            .build();
    }
}
