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

import pt.up.hs.sampling.domain.Note;
import pt.up.hs.sampling.domain.*; // for static metamodels
import pt.up.hs.sampling.repository.NoteRepository;
import pt.up.hs.sampling.service.dto.NoteCriteria;
import pt.up.hs.sampling.service.dto.NoteDTO;
import pt.up.hs.sampling.service.mapper.NoteMapper;

/**
 * Service for executing complex queries for {@link Note} entities in the database.
 * The main input is a {@link NoteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NoteDTO} or a {@link Page} of {@link NoteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NoteQueryService extends QueryService<Note> {

    private final Logger log = LoggerFactory.getLogger(NoteQueryService.class);

    private final NoteRepository noteRepository;
    private final NoteMapper noteMapper;

    public NoteQueryService(NoteRepository noteRepository, NoteMapper noteMapper) {
        this.noteRepository = noteRepository;
        this.noteMapper = noteMapper;
    }

    /**
     * Return a {@link List} of {@link NoteDTO} which matches the criteria from the database.
     *
     * @param projectId ID of the project to which the notes belong.
     * @param sampleId  ID of the sample to which the notes belong.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NoteDTO> findByCriteria(Long projectId, Long sampleId, NoteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Note> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.join("sample", JoinType.LEFT).get("projectId"), projectId))
            .and(equalsSpecification(root -> root.join("sample", JoinType.LEFT).get("id"), sampleId));
        return noteMapper.toDto(noteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NoteDTO} which matches the criteria from the database.
     *
     * @param projectId ID of the project to which the notes belong.
     * @param sampleId  ID of the sample to which the notes belong.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NoteDTO> findByCriteria(Long projectId, Long sampleId, NoteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Note> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.join("sample", JoinType.LEFT).get("projectId"), projectId))
            .and(equalsSpecification(root -> root.join("sample", JoinType.LEFT).get("id"), sampleId));
        return noteRepository.findAll(specification, page)
            .map(noteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     *
     * @param projectId ID of the project to which the notes belong.
     * @param sampleId  ID of the sample to which the notes belong.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(Long projectId, Long sampleId, NoteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Note> specification = createSpecification(criteria)
            .and(equalsSpecification(root -> root.join("sample", JoinType.LEFT).get("projectId"), projectId))
            .and(equalsSpecification(root -> root.join("sample", JoinType.LEFT).get("id"), sampleId));
        return noteRepository.count(specification);
    }

    /**
     * Function to convert {@link NoteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Note> createSpecification(NoteCriteria criteria) {
        Specification<Note> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Note_.id));
            }
            if (criteria.getText() != null) {
                specification = specification.and(buildStringSpecification(criteria.getText(), Note_.text));
            }
            if (criteria.getSelf() != null) {
                specification = specification.and(buildSpecification(criteria.getSelf(), Note_.self));
            }
        }
        return specification;
    }
}
