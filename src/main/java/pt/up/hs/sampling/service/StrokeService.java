package pt.up.hs.sampling.service;

import pt.up.hs.sampling.service.dto.StrokeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link pt.up.hs.sampling.domain.Stroke}.
 */
public interface StrokeService {

    /**
     * Save a stroke.
     *
     * @param projectId ID of the project to which the stroke belongs.
     * @param protocolId  ID of the protocol to which the stroke belongs.
     * @param strokeDTO the entity to save.
     * @return the persisted entity.
     */
    StrokeDTO save(Long projectId, Long protocolId, StrokeDTO strokeDTO);

    /**
     * Get all the strokes.
     *
     * @param projectId ID of the project to which the strokes belong.
     * @param protocolId  ID of the protocol to which the strokes belong.
     * @return the list of entities.
     */
    List<StrokeDTO> findAll(Long projectId, Long protocolId);

    /**
     * Get the "id" stroke.
     *
     * @param projectId ID of the project to which the stroke belongs.
     * @param protocolId  ID of the protocol to which the stroke belongs.
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<StrokeDTO> findOne(Long projectId, Long protocolId, Long id);

    /**
     * Delete the "id" stroke.
     *
     * @param projectId ID of the project to which the stroke belongs.
     * @param protocolId  ID of the protocol to which the stroke belongs.
     * @param id the id of the entity.
     */
    void delete(Long projectId, Long protocolId, Long id);
}
