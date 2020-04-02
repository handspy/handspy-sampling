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
        value = "select distinct note from Note note where note.sample.id = :sampleId and note.sample.projectId = :projectId",
        countQuery = "select count(distinct note) from Note note where note.sample.id = :sampleId and note.sample.projectId = :projectId"
    )
    Page<Note> findAllByProjectIdAndSampleId(
        @Param("projectId") @NotNull Long projectId,
        @Param("sampleId") @NotNull Long sampleId,
        Pageable pageable
    );

    @Query("select count(distinct note) from Note note where note.sample.id = :sampleId and note.sample.projectId = :projectId")
    long countByProjectIdAndSampleId(
        @Param("projectId") @NotNull Long projectId,
        @Param("sampleId") @NotNull Long sampleId
    );

    Optional<Note> findBySampleProjectIdAndSampleIdAndId(
        @Param("projectId") @NotNull Long projectId,
        @Param("sampleId") @NotNull Long sampleId,
        @Param("id") @NotNull Long id
    );

    void deleteAllBySampleProjectIdAndSampleIdAndId(
        @Param("projectId") @NotNull Long projectId,
        @Param("sampleId") @NotNull Long sampleId,
        @Param("id") @NotNull Long id
    );

    void deleteAllBySampleProjectIdAndSampleId(
        @Param("projectId") @NotNull Long projectId,
        @Param("sampleId") @NotNull Long sampleId
    );
}
