package pt.up.hs.sampling.service.impl;

import pt.up.hs.sampling.service.AnnotationTypeService;
import pt.up.hs.sampling.domain.AnnotationType;
import pt.up.hs.sampling.repository.AnnotationTypeRepository;
import pt.up.hs.sampling.service.dto.AnnotationTypeDTO;
import pt.up.hs.sampling.service.mapper.AnnotationTypeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link AnnotationType}.
 */
@Service
@Transactional
public class AnnotationTypeServiceImpl implements AnnotationTypeService {

    private final Logger log = LoggerFactory.getLogger(AnnotationTypeServiceImpl.class);

    private final AnnotationTypeRepository annotationTypeRepository;

    private final AnnotationTypeMapper annotationTypeMapper;

    public AnnotationTypeServiceImpl(AnnotationTypeRepository annotationTypeRepository, AnnotationTypeMapper annotationTypeMapper) {
        this.annotationTypeRepository = annotationTypeRepository;
        this.annotationTypeMapper = annotationTypeMapper;
    }

    /**
     * Save a annotationType.
     *
     * @param annotationTypeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public AnnotationTypeDTO save(AnnotationTypeDTO annotationTypeDTO) {
        log.debug("Request to save AnnotationType : {}", annotationTypeDTO);
        AnnotationType annotationType = annotationTypeMapper.toEntity(annotationTypeDTO);
        annotationType = annotationTypeRepository.save(annotationType);
        return annotationTypeMapper.toDto(annotationType);
    }

    /**
     * Get all the annotationTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AnnotationTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AnnotationTypes");
        return annotationTypeRepository.findAll(pageable)
            .map(annotationTypeMapper::toDto);
    }

    /**
     * Get one annotationType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<AnnotationTypeDTO> findOne(Long id) {
        log.debug("Request to get AnnotationType : {}", id);
        return annotationTypeRepository.findById(id)
            .map(annotationTypeMapper::toDto);
    }

    /**
     * Delete the annotationType by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete AnnotationType : {}", id);
        annotationTypeRepository.deleteById(id);
    }
}
