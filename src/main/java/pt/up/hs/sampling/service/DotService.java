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
     * @param dotDTO the entity to save.
     * @return the persisted entity.
     */
    DotDTO save(DotDTO dotDTO);

    /**
     * Get all the dots.
     *
     * @return the list of entities.
     */
    List<DotDTO> findAll();

    /**
     * Get the "id" dot.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DotDTO> findOne(Long id);

    /**
     * Delete the "id" dot.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
