package pt.up.hs.sampling.service;

import pt.up.hs.sampling.service.dto.ProtocolDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link pt.up.hs.sampling.domain.Protocol}.
 */
public interface ProtocolService {

    /**
     * Save a protocol.
     *
     * @param protocolDTO the entity to save.
     * @return the persisted entity.
     */
    ProtocolDTO save(ProtocolDTO protocolDTO);

    /**
     * Get all the protocols.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProtocolDTO> findAll(Pageable pageable);

    /**
     * Get the "id" protocol.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProtocolDTO> findOne(Long id);

    /**
     * Delete the "id" protocol.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
