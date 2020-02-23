package pt.up.hs.sampling.repository;

import pt.up.hs.sampling.domain.Dot;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Dot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DotRepository extends JpaRepository<Dot, Long>, JpaSpecificationExecutor<Dot> {

}
