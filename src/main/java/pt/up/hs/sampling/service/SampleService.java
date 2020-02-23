package pt.up.hs.sampling.service;

import pt.up.hs.sampling.service.dto.SampleDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link pt.up.hs.sampling.domain.Sample}.
 */
public interface SampleService {

    /**
     * Save a sample.
     *
     * @param sampleDTO the entity to save.
     * @return the persisted entity.
     */
    SampleDTO save(SampleDTO sampleDTO);

    /**
     * Get all the samples.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SampleDTO> findAll(Pageable pageable);

    /**
     * Get the "id" sample.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SampleDTO> findOne(Long id);

    /**
     * Delete the "id" sample.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
