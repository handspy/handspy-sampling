package pt.up.hs.sampling.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import pt.up.hs.sampling.domain.Sample;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Sample entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SampleRepository extends JpaRepository<Sample, Long>, JpaSpecificationExecutor<Sample> {

    @Query(
        value = "select distinct sample from Sample sample where sample.projectId = :projectId",
        countQuery = "select count(distinct sample) from Sample sample where sample.projectId = :projectId"
    )
    Page<Sample> findAllByProjectId(@Param("projectId") @NotNull Long projectId, Pageable pageable);

    @Query("select count(distinct sample) from Sample sample where sample.projectId = :projectId")
    long countByProjectId(@Param("projectId") @NotNull Long projectId);

    Optional<Sample> findByProjectIdAndId(@NotNull Long projectId, @NotNull Long id);

    void deleteAllByProjectIdAndId(@NotNull Long projectId, @NotNull Long id);
}
