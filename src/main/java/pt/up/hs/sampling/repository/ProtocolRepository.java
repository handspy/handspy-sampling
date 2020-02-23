package pt.up.hs.sampling.repository;

import pt.up.hs.sampling.domain.Protocol;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Protocol entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProtocolRepository extends JpaRepository<Protocol, Long>, JpaSpecificationExecutor<Protocol> {

}
