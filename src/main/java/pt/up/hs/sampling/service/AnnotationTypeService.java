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
     * @param annotationTypeDTO the entity to save.
     * @return the persisted entity.
     */
    AnnotationTypeDTO save(AnnotationTypeDTO annotationTypeDTO);

    /**
     * Get all the annotationTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AnnotationTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" annotationType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AnnotationTypeDTO> findOne(Long id);

    /**
     * Delete the "id" annotationType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
