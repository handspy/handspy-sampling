package pt.up.hs.sampling.service;

import pt.up.hs.sampling.service.dto.AnnotationTypeDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link pt.up.hs.sampling.domain.AnnotationType}.
 */
public interface AnnotationTypeService {

    /**
     * Save a annotationType.
     *
     * @param projectId ID of the project to which the annotation type belongs.
     * @param annotationTypeDTO the entity to save.
     * @return the persisted entity.
     */
    AnnotationTypeDTO save(Long projectId, AnnotationTypeDTO annotationTypeDTO);

    /**
     * Get all the annotationTypes.
     *
     * @param projectId ID of the project to which the annotation types belong.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnnotationTypeDTO> findAll(Long projectId, Pageable pageable);

    /**
     * Get the "id" annotationType.
     *
     * @param projectId ID of the project to which the annotation type belongs.
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnnotationTypeDTO> findOne(Long projectId, Long id);

    /**
     * Delete the "id" annotationType.
     *
     * @param projectId ID of the project to which the annotation type belongs.
     * @param id the id of the entity.
     */
    void delete(Long projectId, Long id);
}
