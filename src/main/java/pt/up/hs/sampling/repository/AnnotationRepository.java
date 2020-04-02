package pt.up.hs.sampling.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import pt.up.hs.sampling.domain.Annotation;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Spring Data  repository for the Annotation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, Long>, JpaSpecificationExecutor<Annotation> {

    @Query(
        value = "select distinct annotation from Annotation annotation where annotation.text.id = :textId and annotation.text.projectId = :projectId",
        countQuery = "select count(distinct annotation) from Annotation annotation where annotation.text.id = :textId and annotation.text.projectId = :projectId"
    )
    Page<Annotation> findAllByProjectIdAndTextId(
        @Param("projectId") @NotNull Long projectId,
        @Param("textId") @NotNull Long textId,
        Pageable pageable
    );

    @Query("select count(distinct annotation) from Annotation annotation where annotation.text.id = :textId and annotation.text.projectId = :projectId")
    long countByProjectIdAndTextId(
        @Param("projectId") @NotNull Long projectId,
        @Param("textId") @NotNull Long textId
    );

    @Query("select annotation from Annotation annotation where annotation.id = :id and annotation.text.id = :textId and annotation.text.projectId = :projectId")
    Optional<Annotation> findByProjectIdAndTextIdAndId(
        @Param("projectId") @NotNull Long projectId,
        @Param("textId") @NotNull Long textId,
        @Param("id") @NotNull Long id
    );


    //@Query("delete from Annotation annotation where annotation in (select annotation from Annotation annotation left join Text text on annotation.text = text where annotation.id = :id and annotation.text.id = :textId and annotation.text.projectId = :projectId)")
    void deleteAllByTextProjectIdAndTextIdAndId(
        @Param("projectId") @NotNull Long projectId,
        @Param("textId") @NotNull Long textId,
        @Param("id") @NotNull Long id
    );

    //@Query("delete from Annotation annotation where annotation in (select annotation from Annotation annotation left join Text text on annotation.text = text where annotation.text.id = :textId and annotation.text.projectId = :projectId)")
    void deleteAllByTextProjectIdAndTextId(
        @Param("projectId") @NotNull Long projectId,
        @Param("textId") @NotNull Long textId
    );
}
