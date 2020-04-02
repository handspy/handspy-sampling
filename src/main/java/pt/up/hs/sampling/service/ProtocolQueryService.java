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

import pt.up.hs.sampling.domain.Protocol;
import pt.up.hs.sampling.domain.*; // for static metamodels
import pt.up.hs.sampling.repository.ProtocolRepository;
import pt.up.hs.sampling.service.dto.ProtocolCriteria;
import pt.up.hs.sampling.service.dto.ProtocolDTO;
import pt.up.hs.sampling.service.mapper.ProtocolMapper;

/**
 * Service for executing complex queries for {@link Protocol} entities in the database.
 * The main input is a {@link ProtocolCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProtocolDTO} or a {@link Page} of {@link ProtocolDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProtocolQueryService extends QueryService<Protocol> {

    private final Logger log = LoggerFactory.getLogger(ProtocolQueryService.class);

    private final ProtocolRepository protocolRepository;
    private final ProtocolMapper protocolMapper;

    public ProtocolQueryService(ProtocolRepository protocolRepository, ProtocolMapper protocolMapper) {
        this.protocolRepository = protocolRepository;
        this.protocolMapper = protocolMapper;
    }

    /**
     * Return a {@link List} of {@link ProtocolDTO} which matches the criteria from the database.
     *
     * @param projectId the ID of the project the protocols belongs to.v
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProtocolDTO> findByCriteria(Long projectId, ProtocolCriteria criteria) {
        log.debug("find by criteria {} in project {}", criteria, projectId);
        final Specification<Protocol> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.get("projectId"), projectId));
        return protocolMapper.toDto(protocolRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProtocolDTO} which matches the criteria from the database.
     *
     * @param projectId the ID of the project the protocols belongs to.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProtocolDTO> findByCriteria(Long projectId, ProtocolCriteria criteria, Pageable page) {
        log.debug("find by criteria {}, page {} in project {}", criteria, page, projectId);
        final Specification<Protocol> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.get("projectId"), projectId));
        return protocolRepository.findAll(specification, page)
            .map(protocolMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param projectId the ID of the project the protocols belongs to.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(Long projectId, ProtocolCriteria criteria) {
        log.debug("count by criteria {} in project {}", criteria, projectId);
        final Specification<Protocol> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.get("projectId"), projectId));
        return protocolRepository.count(specification);
    }

    /**
     * Function to convert {@link ProtocolCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Protocol> createSpecification(ProtocolCriteria criteria) {
        Specification<Protocol> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Protocol_.id));
            }
            if (criteria.getLayout() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLayout(), Protocol_.layout));
            }
            if (criteria.getPageNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPageNumber(), Protocol_.pageNumber));
            }
            if (criteria.getSampleId() != null) {
                specification = specification.and(buildSpecification(criteria.getSampleId(),
                    root -> root.join(Protocol_.sample, JoinType.LEFT).get(Sample_.id)));
            }
        }
        return specification;
    }
}
