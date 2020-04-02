package pt.up.hs.sampling.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.up.hs.sampling.domain.Annotation;
import pt.up.hs.sampling.repository.AnnotationRepository;
import pt.up.hs.sampling.service.AnnotationService;
import pt.up.hs.sampling.service.dto.AnnotationDTO;
import pt.up.hs.sampling.service.mapper.AnnotationMapper;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Annotation}.
 */
@Service
@Transactional
public class AnnotationServiceImpl implements AnnotationService {

    private final Logger log = LoggerFactory.getLogger(AnnotationServiceImpl.class);

    private final AnnotationRepository annotationRepository;

    private final AnnotationMapper annotationMapper;

    public AnnotationServiceImpl(AnnotationRepository annotationRepository, AnnotationMapper annotationMapper) {
        this.annotationRepository = annotationRepository;
        this.annotationMapper = annotationMapper;
    }

    /**
     * Save a annotation.
     *
     * @param projectId ID of the project to which the annotation belongs.
     * @param textId    ID of the text to which the annotation belongs.
     * @param annotationDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AnnotationDTO save(Long projectId, Long textId, AnnotationDTO annotationDTO) {
        log.debug("Request to save Annotation {} in text {} of project {}", annotationDTO, textId, projectId);
        annotationDTO.setTextId(textId);
        Annotation annotation = annotationMapper.toEntity(annotationDTO);
        annotation = annotationRepository.save(annotation);
        return annotationMapper.toDto(annotation);
    }

    /**
     * Get all the annotations.
     *
     * @param projectId ID of the project to which the annotations belong.
     * @param textId    ID of the text to which the annotations belong.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AnnotationDTO> findAll(Long projectId, Long textId, Pageable pageable) {
        log.debug("Request to get all Annotations in text {} of project {}", textId, projectId);
        return annotationRepository.findAllByProjectIdAndTextId(projectId, textId, pageable)
            .map(annotationMapper::toDto);
    }

    /**
     * Get the "id" annotation.
     *
     * @param projectId ID of the project to which the annotation belongs.
     * @param textId    ID of the text to which the annotation belongs.
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AnnotationDTO> findOne(Long projectId, Long textId, Long id) {
        log.debug("Request to get Annotation {} in text {} of project {}", id, textId, projectId);
        return annotationRepository.findByProjectIdAndTextIdAndId(projectId, textId, id)
            .map(annotationMapper::toDto);
    }

    /**
     * Delete the "id" annotation.
     *
     * @param projectId ID of the project to which the annotation belongs.
     * @param textId    ID of the text to which the annotation belongs.
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long projectId, Long textId, Long id) {
        log.debug("Request to delete Annotation {} in text {} of project {}", id, textId, projectId);
        annotationRepository.deleteAllByTextProjectIdAndTextIdAndId(projectId, textId, id);
    }
}
