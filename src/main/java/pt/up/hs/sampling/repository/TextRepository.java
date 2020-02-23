package pt.up.hs.sampling.repository;

import pt.up.hs.sampling.domain.Text;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Text entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TextRepository extends JpaRepository<Text, Long>, JpaSpecificationExecutor<Text> {

}
