package pt.up.hs.sampling.service;

import pt.up.hs.sampling.service.dto.PermissionDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link pt.up.hs.sampling.domain.Permission}.
 */
public interface PermissionService {

    /**
     * Save a permission.
     *
     * @param permissionDTO the entity to save.
     * @return the persisted entity.
     */
    PermissionDTO save(PermissionDTO permissionDTO);

    /**
     * Get all the permissions.
     *
     * @return the list of entities.
     */
    List<PermissionDTO> findAll();

    /**
     * Get the "id" permission.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PermissionDTO> findOne(Long id);

    /**
     * Delete the "id" permission.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
