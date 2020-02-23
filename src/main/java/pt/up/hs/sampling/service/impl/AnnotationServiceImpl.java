package pt.up.hs.sampling.service.impl;

import pt.up.hs.sampling.service.AnnotationService;
import pt.up.hs.sampling.domain.Annotation;
import pt.up.hs.sampling.repository.AnnotationRepository;
import pt.up.hs.sampling.service.dto.AnnotationDTO;
import pt.up.hs.sampling.service.mapper.AnnotationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param annotationDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AnnotationDTO save(AnnotationDTO annotationDTO) {
        log.debug("Request to save Annotation : {}", annotationDTO);
        Annotation annotation = annotationMapper.toEntity(annotationDTO);
        annotation = annotationRepository.save(annotation);
        return annotationMapper.toDto(annotation);
    }

    /**
     * Get all the annotations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AnnotationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Annotations");
        return annotationRepository.findAll(pageable)
            .map(annotationMapper::toDto);
    }

    /**
     * Get one annotation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AnnotationDTO> findOne(Long id) {
        log.debug("Request to get Annotation : {}", id);
        return annotationRepository.findById(id)
            .map(annotationMapper::toDto);
    }

    /**
     * Delete the annotation by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Annotation : {}", id);
        annotationRepository.deleteById(id);
    }
}
