package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.constants.EntityNames;
import pt.up.hs.sampling.service.PermissionService;
import pt.up.hs.sampling.web.rest.errors.BadRequestAlertException;
import pt.up.hs.sampling.service.dto.PermissionDTO;

import io.github.jhipster.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.up.hs.sampling.constants.ErrorKeys;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * REST controller for managing {@link pt.up.hs.sampling.domain.Permission}.
 */
@RestController
@RequestMapping("/api")
public class PermissionResource {

    private final Logger log = LoggerFactory.getLogger(PermissionResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PermissionService permissionService;

    public PermissionResource(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * {@code POST  /permissions} : Create a new permission.
     *
     * @param permissionDTO the permissionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new permissionDTO, or with status {@code 400 (Bad Request)} if the permission has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/permissions")
    public ResponseEntity<PermissionDTO> createPermission(@Valid @RequestBody PermissionDTO permissionDTO) throws URISyntaxException {
        log.debug("REST request to save Permission : {}", permissionDTO);
        if (permissionDTO.getId() != null) {
            throw new BadRequestAlertException("A new permission cannot already have an ID", EntityNames.PERMISSION, ErrorKeys.ERR_ID_EXISTS);
        }
        PermissionDTO result = permissionService.save(permissionDTO);
        return ResponseEntity.created(new URI("/api/permissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, EntityNames.PERMISSION, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /permissions} : Updates an existing permission.
     *
     * @param permissionDTO the permissionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissionDTO,
     * or with status {@code 400 (Bad Request)} if the permissionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the permissionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/permissions")
    public ResponseEntity<PermissionDTO> updatePermission(@Valid @RequestBody PermissionDTO permissionDTO) throws URISyntaxException {
        log.debug("REST request to update Permission : {}", permissionDTO);
        if (permissionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", EntityNames.PERMISSION, ErrorKeys.ERR_ID_NULL);
        }
        PermissionDTO result = permissionService.save(permissionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, EntityNames.PERMISSION, permissionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code DELETE  /permissions/:id} : delete the "id" permission.
     *
     * @param id the id of the permissionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/permissions/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        log.debug("REST request to delete Permission : {}", id);
        permissionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, EntityNames.PERMISSION, id.toString()))
            .build();
    }
}
