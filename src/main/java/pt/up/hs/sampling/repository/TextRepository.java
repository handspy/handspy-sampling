package pt.up.hs.sampling.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import pt.up.hs.sampling.domain.Text;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

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
    Page<Text> findAllByProjectId(@Param("projectId") @NotNull Long projectId, Pageable pageable);

    @Query("select count(distinct text) from Text text where text.projectId = :projectId")
    long countByProjectId(@Param("projectId") @NotNull Long projectId);

    Optional<Text> findByProjectIdAndId(@NotNull Long projectId, @NotNull Long id);

    @Nonnull <S extends Text> List<S> saveAll(@Nonnull Iterable<S> entities);

    void deleteAllByProjectIdAndId(@NotNull Long projectId, @NotNull Long id);
}
