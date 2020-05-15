package pt.up.hs.sampling.service;

import io.github.jhipster.service.QueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.up.hs.sampling.domain.Text;
import pt.up.hs.sampling.domain.Text_;
import pt.up.hs.sampling.repository.TextRepository;
import pt.up.hs.sampling.service.dto.TextCriteria;
import pt.up.hs.sampling.service.dto.TextDTO;
import pt.up.hs.sampling.service.mapper.TextMapper;

import java.util.List;

/**
 * Service for executing complex queries for {@link Text} entities in the database.
 * The main input is a {@link TextCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TextDTO} or a {@link Page} of {@link TextDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TextQueryService extends QueryService<Text> {

    private final Logger log = LoggerFactory.getLogger(TextQueryService.class);

    private final TextRepository textRepository;
    private final TextMapper textMapper;

    public TextQueryService(TextRepository textRepository, TextMapper textMapper) {
        this.textRepository = textRepository;
        this.textMapper = textMapper;
    }

    /**
     * Return a {@link List} of {@link TextDTO} which matches the criteria from the database.
     *
     * @param projectId the ID of the project the texts belongs to.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TextDTO> findByCriteria(Long projectId, TextCriteria criteria) {
        log.debug("find by criteria {} in project {}", criteria, projectId);
        final Specification<Text> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.get("projectId"), projectId));
        return textMapper.toDto(textRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TextDTO} which matches the criteria from the database.
     *
     * @param projectId the ID of the project the texts belongs to.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TextDTO> findByCriteria(Long projectId, TextCriteria criteria, Pageable page) {
        log.debug("find by criteria {}, page {} in project {}", criteria, page, projectId);
        final Specification<Text> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.get("projectId"), projectId));
        return textRepository.findAll(specification, page)
            .map(textMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param projectId the ID of the project the texts belongs to.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(Long projectId, TextCriteria criteria) {
        log.debug("count by criteria {} in project {}", criteria, projectId);
        final Specification<Text> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.get("projectId"), projectId));
        return textRepository.count(specification);
    }

    /**
     * Function to convert {@link TextCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Text> createSpecification(TextCriteria criteria) {
        Specification<Text> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Text_.id));
            }
            if (criteria.getTaskId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTaskId(), Text_.taskId));
            }
            if (criteria.getParticipantId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getParticipantId(), Text_.participantId));
            }
            if (criteria.getLanguage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLanguage(), Text_.language));
            }
        }
        return specification;
    }
}
