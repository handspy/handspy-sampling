package pt.up.hs.sampling.service.impl;

import pt.up.hs.sampling.service.PermissionService;
import pt.up.hs.sampling.domain.Permission;
import pt.up.hs.sampling.repository.PermissionRepository;
import pt.up.hs.sampling.service.dto.PermissionDTO;
import pt.up.hs.sampling.service.mapper.PermissionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Permission}.
 */
@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    private final Logger log = LoggerFactory.getLogger(PermissionServiceImpl.class);

    private final PermissionRepository permissionRepository;

    private final PermissionMapper permissionMapper;

    public PermissionServiceImpl(PermissionRepository permissionRepository, PermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    /**
     * Save a permission.
     *
     * @param permissionDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public PermissionDTO save(PermissionDTO permissionDTO) {
        log.debug("Request to save Permission : {}", permissionDTO);
        Permission permission = permissionMapper.toEntity(permissionDTO);
        permission = permissionRepository.save(permission);
        return permissionMapper.toDto(permission);
    }

    /**
     * Get all the permissions.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<PermissionDTO> findAll() {
        log.debug("Request to get all Permissions");
        return permissionRepository.findAll().stream()
            .map(permissionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one permission by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<PermissionDTO> findOne(Long id) {
        log.debug("Request to get Permission : {}", id);
        return permissionRepository.findById(id)
            .map(permissionMapper::toDto);
    }

    /**
     * Delete the permission by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Permission : {}", id);
        permissionRepository.deleteById(id);
    }
}
