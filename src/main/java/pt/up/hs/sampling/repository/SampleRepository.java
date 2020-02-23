package pt.up.hs.sampling.repository;

import pt.up.hs.sampling.domain.Sample;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Sample entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SampleRepository extends JpaRepository<Sample, Long>, JpaSpecificationExecutor<Sample> {

}
