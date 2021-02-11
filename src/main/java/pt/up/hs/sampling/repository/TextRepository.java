package pt.up.hs.sampling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.up.hs.sampling.domain.Text;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Text entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TextRepository extends JpaRepository<Text, Long>, JpaSpecificationExecutor<Text> {

    @Query(
        value = "select distinct text from Text text where text.projectId = :projectId",
        countQuery = "select count(distinct text) from Text text where text.projectId = :projectId"
    )
    List<Text> findAllByProjectId(@Param("projectId") @NotNull Long projectId);

    @Query("select count(distinct text) from Text text where text.projectId = :projectId")
    long countByProjectId(@Param("projectId") @NotNull Long projectId);

    Optional<Text> findByProjectIdAndId(@NotNull Long projectId, @NotNull Long id);

    Optional<Text> findByProjectIdAndTaskIdAndParticipantId(@NotNull Long projectId, Long taskId, Long participantId);

    @Nonnull <S extends Text> List<S> saveAll(@Nonnull Iterable<S> entities);

    void deleteAllByProjectIdAndId(@NotNull Long projectId, @NotNull Long id);

    void deleteAllByProjectIdAndIdIn(@NotNull Long projectId, @NotNull List<Long> ids);
}
