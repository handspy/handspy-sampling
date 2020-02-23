package pt.up.hs.sampling.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import pt.up.hs.sampling.domain.AnnotationType;
import pt.up.hs.sampling.domain.*; // for static metamodels
import pt.up.hs.sampling.repository.AnnotationTypeRepository;
import pt.up.hs.sampling.service.dto.AnnotationTypeCriteria;
import pt.up.hs.sampling.service.dto.AnnotationTypeDTO;
import pt.up.hs.sampling.service.mapper.AnnotationTypeMapper;

/**
 * Service for executing complex queries for {@link AnnotationType} entities in the database.
 * The main input is a {@link AnnotationTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AnnotationTypeDTO} or a {@link Page} of {@link AnnotationTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnnotationTypeQueryService extends QueryService<AnnotationType> {

    private final Logger log = LoggerFactory.getLogger(AnnotationTypeQueryService.class);

    private final AnnotationTypeRepository annotationTypeRepository;

    private final AnnotationTypeMapper annotationTypeMapper;

    public AnnotationTypeQueryService(AnnotationTypeRepository annotationTypeRepository, AnnotationTypeMapper annotationTypeMapper) {
        this.annotationTypeRepository = annotationTypeRepository;
        this.annotationTypeMapper = annotationTypeMapper;
    }

    /**
     * Return a {@link List} of {@link AnnotationTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AnnotationTypeDTO> findByCriteria(AnnotationTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AnnotationType> specification = createSpecification(criteria);
        return annotationTypeMapper.toDto(annotationTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AnnotationTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AnnotationTypeDTO> findByCriteria(AnnotationTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AnnotationType> specification = createSpecification(criteria);
        return annotationTypeRepository.findAll(specification, page)
            .map(annotationTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnnotationTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AnnotationType> specification = createSpecification(criteria);
        return annotationTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link AnnotationTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AnnotationType> createSpecification(AnnotationTypeCriteria criteria) {
        Specification<AnnotationType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AnnotationType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), AnnotationType_.name));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), AnnotationType_.label));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AnnotationType_.description));
            }
            if (criteria.getEmotional() != null) {
                specification = specification.and(buildSpecification(criteria.getEmotional(), AnnotationType_.emotional));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), AnnotationType_.weight));
            }
            if (criteria.getColor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getColor(), AnnotationType_.color));
            }
        }
        return specification;
    }
}
