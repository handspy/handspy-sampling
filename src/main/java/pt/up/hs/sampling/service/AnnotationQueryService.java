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

import pt.up.hs.sampling.domain.Annotation;
import pt.up.hs.sampling.domain.*; // for static metamodels
import pt.up.hs.sampling.repository.AnnotationRepository;
import pt.up.hs.sampling.service.dto.AnnotationCriteria;
import pt.up.hs.sampling.service.dto.AnnotationDTO;
import pt.up.hs.sampling.service.mapper.AnnotationMapper;

/**
 * Service for executing complex queries for {@link Annotation} entities in the database.
 * The main input is a {@link AnnotationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AnnotationDTO} or a {@link Page} of {@link AnnotationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AnnotationQueryService extends QueryService<Annotation> {

    private final Logger log = LoggerFactory.getLogger(AnnotationQueryService.class);

    private final AnnotationRepository annotationRepository;

    private final AnnotationMapper annotationMapper;

    public AnnotationQueryService(AnnotationRepository annotationRepository, AnnotationMapper annotationMapper) {
        this.annotationRepository = annotationRepository;
        this.annotationMapper = annotationMapper;
    }

    /**
     * Return a {@link List} of {@link AnnotationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AnnotationDTO> findByCriteria(AnnotationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Annotation> specification = createSpecification(criteria);
        return annotationMapper.toDto(annotationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AnnotationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AnnotationDTO> findByCriteria(AnnotationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Annotation> specification = createSpecification(criteria);
        return annotationRepository.findAll(specification, page)
            .map(annotationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AnnotationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Annotation> specification = createSpecification(criteria);
        return annotationRepository.count(specification);
    }

    /**
     * Function to convert {@link AnnotationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Annotation> createSpecification(AnnotationCriteria criteria) {
        Specification<Annotation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Annotation_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getType(), Annotation_.type));
            }
            if (criteria.getStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStart(), Annotation_.start));
            }
            if (criteria.getSize() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSize(), Annotation_.size));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), Annotation_.note));
            }
            if (criteria.getTextId() != null) {
                specification = specification.and(buildSpecification(criteria.getTextId(),
                    root -> root.join(Annotation_.text, JoinType.LEFT).get(Text_.id)));
            }
        }
        return specification;
    }
}
