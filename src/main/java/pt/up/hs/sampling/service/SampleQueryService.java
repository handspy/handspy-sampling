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

import pt.up.hs.sampling.domain.Sample;
import pt.up.hs.sampling.domain.*; // for static metamodels
import pt.up.hs.sampling.repository.SampleRepository;
import pt.up.hs.sampling.service.dto.SampleCriteria;
import pt.up.hs.sampling.service.dto.SampleDTO;
import pt.up.hs.sampling.service.mapper.SampleMapper;

/**
 * Service for executing complex queries for {@link Sample} entities in the database.
 * The main input is a {@link SampleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SampleDTO} or a {@link Page} of {@link SampleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SampleQueryService extends QueryService<Sample> {

    private final Logger log = LoggerFactory.getLogger(SampleQueryService.class);

    private final SampleRepository sampleRepository;

    private final SampleMapper sampleMapper;

    public SampleQueryService(SampleRepository sampleRepository, SampleMapper sampleMapper) {
        this.sampleRepository = sampleRepository;
        this.sampleMapper = sampleMapper;
    }

    /**
     * Return a {@link List} of {@link SampleDTO} which matches the criteria from the database.
     *
     * @param projectId the ID of the project the samples belongs to.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SampleDTO> findByCriteria(Long projectId, SampleCriteria criteria) {
        log.debug("find by criteria {} in project {}", criteria, projectId);
        final Specification<Sample> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.get("projectId"), projectId));
        return sampleMapper.toDto(sampleRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SampleDTO} which matches the criteria from the database.
     *
     * @param projectId the ID of the project the samples belongs to.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SampleDTO> findByCriteria(Long projectId, SampleCriteria criteria, Pageable page) {
        log.debug("find by criteria {}, page {} in project {}", criteria, page, projectId);
        final Specification<Sample> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.get("projectId"), projectId));
        return sampleRepository.findAll(specification, page)
            .map(sampleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param projectId the ID of the project the samples belongs to.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(Long projectId, SampleCriteria criteria) {
        log.debug("count by criteria {} in project {}", criteria, projectId);
        final Specification<Sample> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.get("projectId"), projectId));
        return sampleRepository.count(specification);
    }

    /**
     * Function to convert {@link SampleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Sample> createSpecification(SampleCriteria criteria) {
        Specification<Sample> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Sample_.id));
            }
            if (criteria.getTask() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTask(), Sample_.task));
            }
            if (criteria.getParticipant() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getParticipant(), Sample_.participant));
            }
            if (criteria.getTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimestamp(), Sample_.timestamp));
            }
            if (criteria.getLanguage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLanguage(), Sample_.language));
            }
        }
        return specification;
    }
}
