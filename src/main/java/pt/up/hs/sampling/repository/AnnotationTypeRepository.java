package pt.up.hs.sampling.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import pt.up.hs.sampling.domain.AnnotationType;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Spring Data  repository for the AnnotationType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnnotationTypeRepository extends JpaRepository<AnnotationType, Long>, JpaSpecificationExecutor<AnnotationType> {

    @Query(
        value = "select distinct annotationType from AnnotationType annotationType where annotationType.projectId = :projectId",
        countQuery = "select count(distinct annotationType) from AnnotationType annotationType where annotationType.projectId = :projectId"
    )
    Page<AnnotationType> findAllByProjectId(@Param("projectId") @NotNull Long projectId, Pageable pageable);

    @Query("select count(distinct annotationType) from AnnotationType annotationType where annotationType.projectId = :projectId")
    long countByProjectId(@Param("projectId") @NotNull Long projectId);

    Optional<AnnotationType> findByProjectIdAndId(@NotNull Long projectId, @NotNull Long id);

    void deleteAllByProjectIdAndId(@NotNull Long projectId, @NotNull Long id);
}
