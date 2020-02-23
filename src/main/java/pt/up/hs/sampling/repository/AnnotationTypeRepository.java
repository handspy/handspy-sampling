package pt.up.hs.sampling.repository;

import pt.up.hs.sampling.domain.AnnotationType;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AnnotationType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnnotationTypeRepository extends JpaRepository<AnnotationType, Long>, JpaSpecificationExecutor<AnnotationType> {

}
