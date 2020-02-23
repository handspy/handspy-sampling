package pt.up.hs.sampling.service;

import pt.up.hs.sampling.service.dto.TextDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link pt.up.hs.sampling.domain.Text}.
 */
public interface TextService {

    /**
     * Save a text.
     *
     * @param textDTO the entity to save.
     * @return the persisted entity.
     */
    TextDTO save(TextDTO textDTO);

    /**
     * Get all the texts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TextDTO> findAll(Pageable pageable);

    /**
     * Get the "id" text.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TextDTO> findOne(Long id);

    /**
     * Delete the "id" text.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
