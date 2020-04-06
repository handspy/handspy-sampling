package pt.up.hs.sampling.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import pt.up.hs.sampling.domain.Protocol;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Protocol entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProtocolRepository extends JpaRepository<Protocol, Long>, JpaSpecificationExecutor<Protocol> {

    @Query(
        value = "select distinct protocol from Protocol protocol where protocol.projectId = :projectId",
        countQuery = "select count(distinct protocol) from Protocol protocol where protocol.projectId = :projectId"
    )
    Page<Protocol> findAllByProjectId(@Param("projectId") @NotNull Long projectId, Pageable pageable);

    @Query("select count(distinct protocol) from Protocol protocol where protocol.projectId = :projectId")
    long countByProjectId(@Param("projectId") @NotNull Long projectId);

    Optional<Protocol> findByProjectIdAndId(@NotNull Long projectId, @NotNull Long id);

    @Nonnull <S extends Protocol> List<S> saveAll(@Nonnull Iterable<S> entities);

    void deleteAllByProjectIdAndId(@NotNull Long projectId, @NotNull Long id);

    @Modifying
    @Query("update Protocol protocol set protocol.dirtyPreview = false where protocol.projectId = :projectId and protocol.id = :id")
    void cleanPreview(@Param("projectId") @NotNull Long projectId, @Param("id") @NotNull Long id);

    @Modifying
    @Query("update Protocol protocol set protocol.dirtyPreview = true")
    void markAllForPreviewRegenerate();

    Optional<Protocol> findFirstByDirtyPreviewTrue();

    Page<Protocol> findAllByDirtyPreviewTrue(Pageable pageable);
}
