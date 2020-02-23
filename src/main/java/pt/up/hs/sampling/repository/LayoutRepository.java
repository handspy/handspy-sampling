package pt.up.hs.sampling.repository;

import pt.up.hs.sampling.domain.Layout;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Layout entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LayoutRepository extends JpaRepository<Layout, Long>, JpaSpecificationExecutor<Layout> {

}
