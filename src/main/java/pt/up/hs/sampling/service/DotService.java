package pt.up.hs.sampling.service;

import pt.up.hs.sampling.service.dto.DotDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link pt.up.hs.sampling.domain.Dot}.
 */
public interface DotService {

    /**
     * Save a dot.
     *
     * @param projectId ID of the project to which the dot belongs.
     * @param protocolId  ID of the protocol to which the dot belongs.
     * @param dotDTO the entity to save.
     * @return the persisted entity.
     */
    DotDTO save(Long projectId, Long protocolId, DotDTO dotDTO);

    /**
     * Get all the dots.
     *
     * @param projectId ID of the project to which the dots belong.
     * @param protocolId  ID of the protocol to which the dots belong.
     * @return the list of entities.
     */
    List<DotDTO> findAll(Long projectId, Long protocolId);

    /**
     * Get the "id" dot.
     *
     * @param projectId ID of the project to which the dot belongs.
     * @param protocolId  ID of the protocol to which the dot belongs.
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DotDTO> findOne(Long projectId, Long protocolId, Long id);

    /**
     * Delete the "id" dot.
     *
     * @param projectId ID of the project to which the dot belongs.
     * @param protocolId  ID of the protocol to which the dot belongs.
     * @param id the id of the entity.
     */
    void delete(Long projectId, Long protocolId, Long id);
}
