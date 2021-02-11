package pt.up.hs.sampling.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pt.up.hs.sampling.domain.Protocol;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pt.up.hs.sampling.domain.ProtocolData;

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
    List<Protocol> findAllByProjectId(@Param("projectId") @NotNull Long projectId);

    @Query("select count(distinct protocol) from Protocol protocol where protocol.projectId = :projectId")
    long countByProjectId(@Param("projectId") @NotNull Long projectId);

    Optional<Protocol> findByProjectIdAndId(@NotNull Long projectId, @NotNull Long id);

    @Transactional
    void deleteByProjectIdAndId(@NotNull Long projectId, @NotNull Long id);

    void deleteAllByProjectIdAndIdIn(@NotNull Long projectId, @NotNull List<Long> ids);
}
