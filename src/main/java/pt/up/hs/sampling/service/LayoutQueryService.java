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

import pt.up.hs.sampling.domain.Layout;
import pt.up.hs.sampling.domain.*; // for static metamodels
import pt.up.hs.sampling.repository.LayoutRepository;
import pt.up.hs.sampling.service.dto.LayoutCriteria;
import pt.up.hs.sampling.service.dto.LayoutDTO;
import pt.up.hs.sampling.service.mapper.LayoutMapper;

/**
 * Service for executing complex queries for {@link Layout} entities in the database.
 * The main input is a {@link LayoutCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LayoutDTO} or a {@link Page} of {@link LayoutDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LayoutQueryService extends QueryService<Layout> {

    private final Logger log = LoggerFactory.getLogger(LayoutQueryService.class);

    private final LayoutRepository layoutRepository;

    private final LayoutMapper layoutMapper;

    public LayoutQueryService(LayoutRepository layoutRepository, LayoutMapper layoutMapper) {
        this.layoutRepository = layoutRepository;
        this.layoutMapper = layoutMapper;
    }

    /**
     * Return a {@link List} of {@link LayoutDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LayoutDTO> findByCriteria(LayoutCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Layout> specification = createSpecification(criteria);
        return layoutMapper.toDto(layoutRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LayoutDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LayoutDTO> findByCriteria(LayoutCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Layout> specification = createSpecification(criteria);
        return layoutRepository.findAll(specification, page)
            .map(layoutMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LayoutCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Layout> specification = createSpecification(criteria);
        return layoutRepository.count(specification);
    }

    /**
     * Function to convert {@link LayoutCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Layout> createSpecification(LayoutCriteria criteria) {
        Specification<Layout> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Layout_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Layout_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Layout_.description));
            }
            if (criteria.getWidth() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWidth(), Layout_.width));
            }
            if (criteria.getHeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHeight(), Layout_.height));
            }
            if (criteria.getMarginLeft() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMarginLeft(), Layout_.marginLeft));
            }
            if (criteria.getMarginRight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMarginRight(), Layout_.marginRight));
            }
            if (criteria.getMarginTop() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMarginTop(), Layout_.marginTop));
            }
            if (criteria.getMarginBottom() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMarginBottom(), Layout_.marginBottom));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Layout_.url));
            }
        }
        return specification;
    }
}
