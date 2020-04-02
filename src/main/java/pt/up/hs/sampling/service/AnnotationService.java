package pt.up.hs.sampling.service;

import pt.up.hs.sampling.service.dto.AnnotationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link pt.up.hs.sampling.domain.Annotation}.
 */
public interface AnnotationService {

    /**
     * Save a annotation.
     *
     * @param projectId     ID of the project to which the annotation belongs.
     * @param textId        ID of the text to which the annotation belongs.
     * @param annotationDTO the entity to save.
     * @return the persisted entity.
     */
    AnnotationDTO save(Long projectId, Long textId, AnnotationDTO annotationDTO);

    /**
     * Get all the annotations.
     *
     * @param projectId ID of the project to which the annotations belong.
     * @param textId    ID of the text to which the annotations belong.
     * @param pageable  the pagination information.
     * @return the list of entities.
     */
    Page<AnnotationDTO> findAll(Long projectId, Long textId, Pageable pageable);

    /**
     * Get the "id" annotation.
     *
     * @param projectId ID of the project to which the annotation belongs.
     * @param textId    ID of the text to which the annotation belongs.
     * @param id        the id of the entity.
     * @return the entity.
     */
    Optional<AnnotationDTO> findOne(Long projectId, Long textId, Long id);

    /**
     * Delete the "id" annotation.
     *
     * @param projectId ID of the project to which the annotation belongs.
     * @param textId    ID of the text to which the annotation belongs.
     * @param id        the id of the entity.
     */
    void delete(Long projectId, Long textId, Long id);
}
