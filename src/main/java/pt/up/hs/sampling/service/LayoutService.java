package pt.up.hs.sampling.service;

import pt.up.hs.sampling.service.dto.LayoutDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link pt.up.hs.sampling.domain.Layout}.
 */
public interface LayoutService {

    /**
     * Save a layout.
     *
     * @param layoutDTO the entity to save.
     * @return the persisted entity.
     */
    LayoutDTO save(LayoutDTO layoutDTO);

    /**
     * Get all the layouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LayoutDTO> findAll(Pageable pageable);

    /**
     * Get the "id" layout.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LayoutDTO> findOne(Long id);

    /**
     * Delete the "id" layout.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
