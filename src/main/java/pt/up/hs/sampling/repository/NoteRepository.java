package pt.up.hs.sampling.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.up.hs.sampling.domain.Note;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Spring Data  repository for the Note entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NoteRepository extends JpaRepository<Note, Long>, JpaSpecificationExecutor<Note> {

    @Query(
        value = "select distinct note from Note note where note.projectId = :projectId and (note.createdBy = ?#{principal} or note.self = false)",
        countProjection = "distinct note.id"
    )
    Page<Note> findAllByProjectId(
        @Param("projectId") @NotNull Long projectId,
        Pageable pageable
    );

    @Query(
        "select count(distinct note) from Note note where note.projectId = :projectId and (note.createdBy = ?#{principal} or note.self = false)"
    )
    long countByProjectId(
        @Param("projectId") @NotNull Long projectId
    );

    @Query(
        value = "select note from Note note where note.projectId = :projectId and note.id = :id and (note.createdBy = ?#{principal} or note.self = false)"
    )
    Optional<Note> findByProjectIdAndId(
        @Param("projectId") @NotNull Long projectId,
        @Param("id") @NotNull Long id
    );
}
