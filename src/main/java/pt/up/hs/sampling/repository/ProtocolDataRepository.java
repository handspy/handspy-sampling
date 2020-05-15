package pt.up.hs.sampling.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pt.up.hs.sampling.domain.ProtocolData;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Spring Data  repository for the Protocol entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProtocolDataRepository extends JpaRepository<ProtocolData, Long> {

    Optional<ProtocolData> findByProtocolProjectIdAndProtocolId(
        @NotNull Long projectId,
        Long protocolId
    );

    Optional<ProtocolData> findFirstByDirtyPreviewTrue();

    @Modifying(clearAutomatically = true)
    @Query("update ProtocolData pd set pd.dirtyPreview = false where pd.protocolId = :protocolId")
    void cleanPreview(@Param("protocolId") @NotNull Long protocolId);

    @Modifying(clearAutomatically = true)
    @Query("update ProtocolData pd set pd.dirtyPreview = true")
    void markAllForPreviewRegenerate();
}
