package pt.up.hs.sampling.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.up.hs.sampling.domain.Stroke;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Stroke entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StrokeRepository extends JpaRepository<Stroke, Long>, JpaSpecificationExecutor<Stroke> {

    @Query(
        value = "select distinct stroke from Stroke stroke where stroke.protocol.id = :protocolId and stroke.protocol.projectId = :projectId",
        countQuery = "select count(distinct stroke) from Stroke stroke where stroke.protocol.id = :protocolId and stroke.protocol.projectId = :projectId"
    )
    List<Stroke> findAllByProjectIdAndProtocolId(
        @Param("projectId") @NotNull Long projectId,
        @Param("protocolId") @NotNull Long protocolId
    );

    @Query("select count(distinct stroke) from Stroke stroke where stroke.protocol.id = :protocolId and stroke.protocol.projectId = :projectId")
    long countByProjectIdAndProtocolId(
        @Param("projectId") @NotNull Long projectId,
        @Param("protocolId") @NotNull Long protocolId
    );

    @Query("select stroke from Stroke stroke where stroke.id = :id and stroke.protocol.id = :protocolId and stroke.protocol.projectId = :projectId")
    Optional<Stroke> findByProjectIdAndProtocolIdAndId(
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
