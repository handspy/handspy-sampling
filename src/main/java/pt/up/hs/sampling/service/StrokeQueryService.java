package pt.up.hs.sampling.service;

import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.up.hs.sampling.domain.Stroke;
import pt.up.hs.sampling.domain.Stroke_;
import pt.up.hs.sampling.repository.StrokeRepository;
import pt.up.hs.sampling.service.dto.StrokeCriteria;
import pt.up.hs.sampling.service.dto.StrokeDTO;
import pt.up.hs.sampling.service.mapper.StrokeMapper;

import javax.persistence.criteria.JoinType;
import java.util.List;

/**
 * Service for executing complex queries for {@link Stroke} entities in the database.
 * The main input is a {@link StrokeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StrokeDTO} or a {@link Page} of {@link StrokeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StrokeQueryService extends QueryService<Stroke> {

    private final Logger log = LoggerFactory.getLogger(StrokeQueryService.class);

    private final StrokeRepository strokeRepository;
    private final StrokeMapper strokeMapper;

    public StrokeQueryService(StrokeRepository strokeRepository, StrokeMapper strokeMapper) {
        this.strokeRepository = strokeRepository;
        this.strokeMapper = strokeMapper;
    }

    /**
     * Return a {@link List} of {@link StrokeDTO} which matches the criteria from the database.
     *
     * @param projectId ID of the project to which the strokes belong.
     * @param protocolId  ID of the protocol to which the strokes belong.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StrokeDTO> findByCriteria(Long projectId, Long protocolId, StrokeCriteria criteria) {
        log.debug("find by criteria {} in protocol {} of project {}", criteria, protocolId, projectId);
        final Specification<Stroke> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.join("protocol", JoinType.LEFT).get("projectId"), projectId))
            .and(equalsSpecification(root -> root.join("protocol", JoinType.LEFT).get("id"), protocolId));
        return strokeMapper.toDto(strokeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StrokeDTO} which matches the criteria from the database.
     *
     * @param projectId ID of the project to which the strokes belong.
     * @param protocolId  ID of the protocol to which the strokes belong.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StrokeDTO> findByCriteria(Long projectId, Long protocolId, StrokeCriteria criteria, Pageable page) {
        log.debug("find by criteria {}, page {} in protocol {} of project {}", criteria, page, protocolId, projectId);
        final Specification<Stroke> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.join("protocol", JoinType.LEFT).get("projectId"), projectId))
            .and(equalsSpecification(root -> root.join("protocol", JoinType.LEFT).get("id"), protocolId));
        return strokeRepository.findAll(specification, page)
            .map(strokeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param projectId ID of the project to which the strokes belong.
     * @param protocolId  ID of the protocol to which the strokes belong.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(Long projectId, Long protocolId, StrokeCriteria criteria) {
        log.debug("count by criteria {} in protocol {} of project {}", criteria, protocolId, projectId);
        final Specification<Stroke> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.join("protocol", JoinType.LEFT).get("projectId"), projectId))
            .and(equalsSpecification(root -> root.join("protocol", JoinType.LEFT).get("id"), protocolId));
        return strokeRepository.count(specification);
    }

    /**
     * Function to convert {@link StrokeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Stroke> createSpecification(StrokeCriteria criteria) {
        Specification<Stroke> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Stroke_.id));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), Stroke_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), Stroke_.endTime));
            }
        }
        return specification;
    }
}
