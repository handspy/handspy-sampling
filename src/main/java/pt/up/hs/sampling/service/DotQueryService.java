package pt.up.hs.sampling.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import pt.up.hs.sampling.domain.Dot;
import pt.up.hs.sampling.domain.*; // for static metamodels
import pt.up.hs.sampling.repository.DotRepository;
import pt.up.hs.sampling.service.dto.DotCriteria;
import pt.up.hs.sampling.service.dto.DotDTO;
import pt.up.hs.sampling.service.mapper.DotMapper;

import javax.persistence.criteria.JoinType;

/**
 * Service for executing complex queries for {@link Dot} entities in the database.
 * The main input is a {@link DotCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DotDTO} or a {@link Page} of {@link DotDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DotQueryService extends QueryService<Dot> {

    private final Logger log = LoggerFactory.getLogger(DotQueryService.class);

    private final DotRepository dotRepository;
    private final DotMapper dotMapper;

    public DotQueryService(DotRepository dotRepository, DotMapper dotMapper) {
        this.dotRepository = dotRepository;
        this.dotMapper = dotMapper;
    }

    /**
     * Return a {@link List} of {@link DotDTO} which matches the criteria from the database.
     *
     * @param projectId ID of the project to which the dots belong.
     * @param protocolId  ID of the protocol to which the dots belong.
     * @param strokeId  ID of the stroke to which the dot belongs.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DotDTO> findByCriteria(Long projectId, Long protocolId, Long strokeId, DotCriteria criteria) {
        log.debug("find by criteria {} in stroke {} of protocol {} of project {}", criteria, strokeId, protocolId, projectId);
        final Specification<Dot> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.join("stroke", JoinType.LEFT).get("id"), strokeId))
            .and(equalsSpecification(root -> root.join("stroke", JoinType.LEFT).join("protocol", JoinType.LEFT).get("id"), protocolId))
            .and(equalsSpecification(root -> root.join("stroke", JoinType.LEFT).join("protocol", JoinType.LEFT).get("projectId"), projectId));
        return dotMapper.toDto(dotRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DotDTO} which matches the criteria from the database.
     *
     * @param projectId ID of the project to which the dots belong.
     * @param protocolId  ID of the protocol to which the dots belong.
     * @param strokeId  ID of the stroke to which the dot belongs.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DotDTO> findByCriteria(Long projectId, Long protocolId, Long strokeId, DotCriteria criteria, Pageable page) {
        log.debug("find by criteria {}, page {} in stroke {} of protocol {} of project {}", criteria, page, strokeId, protocolId, projectId);
        final Specification<Dot> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.join("stroke", JoinType.LEFT).get("id"), strokeId))
            .and(equalsSpecification(root -> root.join("stroke", JoinType.LEFT).join("protocol", JoinType.LEFT).get("id"), protocolId))
            .and(equalsSpecification(root -> root.join("stroke", JoinType.LEFT).join("protocol", JoinType.LEFT).get("projectId"), projectId));
        return dotRepository.findAll(specification, page)
            .map(dotMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param projectId ID of the project to which the dots belong.
     * @param protocolId  ID of the protocol to which the dots belong.
     * @param strokeId  ID of the stroke to which the dot belongs.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(Long projectId, Long protocolId, Long strokeId, DotCriteria criteria) {
        log.debug("count by criteria {} in stroke {} of protocol {} of project {}", criteria, strokeId, protocolId, projectId);
        final Specification<Dot> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.join("stroke", JoinType.LEFT).get("id"), strokeId))
            .and(equalsSpecification(root -> root.join("stroke", JoinType.LEFT).join("protocol", JoinType.LEFT).get("id"), protocolId))
            .and(equalsSpecification(root -> root.join("stroke", JoinType.LEFT).join("protocol", JoinType.LEFT).get("projectId"), projectId));
        return dotRepository.count(specification);
    }

    /**
     * Function to convert {@link DotCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Dot> createSpecification(DotCriteria criteria) {
        Specification<Dot> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Dot_.id));
            }
            if (criteria.getTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimestamp(), Dot_.timestamp));
            }
            if (criteria.getX() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getX(), Dot_.x));
            }
            if (criteria.getY() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getY(), Dot_.y));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Dot_.type));
            }
            if (criteria.getPressure() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPressure(), Dot_.pressure));
            }
        }
        return specification;
    }
}
