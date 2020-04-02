package pt.up.hs.sampling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.up.hs.sampling.domain.Dot;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Dot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DotRepository extends JpaRepository<Dot, Long>, JpaSpecificationExecutor<Dot> {

    @Query(
        value = "select distinct dot from Dot dot where dot.protocol.id = :protocolId and dot.protocol.projectId = :projectId",
        countQuery = "select count(distinct dot) from Dot dot where dot.protocol.id = :protocolId and dot.protocol.projectId = :projectId"
    )
    List<Dot> findAllByProjectIdAndProtocolId(
        @Param("projectId") @NotNull Long projectId,
        @Param("protocolId") @NotNull Long protocolId
    );

    @Query("select count(distinct dot) from Dot dot where dot.protocol.id = :protocolId and dot.protocol.projectId = :projectId")
    long countByProjectIdAndProtocolId(
        @Param("projectId") @NotNull Long projectId,
        @Param("protocolId") @NotNull Long protocolId
    );

    @Query("select dot from Dot dot where dot.id = :id and dot.protocol.id = :protocolId and dot.protocol.projectId = :projectId")
    Optional<Dot> findByProjectIdAndProtocolIdAndId(
        @Param("projectId") @NotNull Long projectId,
        @Param("protocolId") @NotNull Long protocolId,
        @Param("id") @NotNull Long id
    );

    void deleteAllByProtocolProjectIdAndProtocolIdAndId(
        @Param("projectId") @NotNull Long projectId,
        @Param("protocolId") @NotNull Long protocolId,
        @Param("id") @NotNull Long id
    );

    void deleteAllByProtocolProjectIdAndProtocolId(
        @Param("projectId") @NotNull Long projectId,
        @Param("protocolId") @NotNull Long protocolId
    );
}
