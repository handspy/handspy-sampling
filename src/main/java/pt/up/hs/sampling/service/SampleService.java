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
     * @param projectId ID of the project to which the sample belongs.
     * @param sampleDTO the entity to save.
     * @return the persisted entity.
     */
    SampleDTO save(Long projectId, SampleDTO sampleDTO);

    /**
     * Get all the samples.
     *
     * @param projectId ID of the project to which the samples belong.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SampleDTO> findAll(Long projectId, Pageable pageable);

    /**
     * Get the "id" sample.
     *
     * @param projectId ID of the project to which the sample belongs.
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SampleDTO> findOne(Long projectId, Long id);

    /**
     * Delete the "id" sample.
     *
     * @param projectId ID of the project to which the sample belongs.
     * @param id the id of the entity.
     */
    void delete(Long projectId, Long id);
}
