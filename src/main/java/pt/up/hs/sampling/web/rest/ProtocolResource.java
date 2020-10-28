package pt.up.hs.sampling.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pt.up.hs.sampling.constants.EntityNames;
import pt.up.hs.sampling.constants.ErrorKeys;
import pt.up.hs.sampling.service.ProtocolQueryService;
import pt.up.hs.sampling.service.ProtocolService;
import pt.up.hs.sampling.service.dto.BulkImportResultDTO;
import pt.up.hs.sampling.service.dto.ProtocolCriteria;
import pt.up.hs.sampling.service.dto.ProtocolDTO;
import pt.up.hs.sampling.service.dto.ProtocolDataDTO;
import pt.up.hs.sampling.web.rest.errors.BadRequestAlertException;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link pt.up.hs.sampling.domain.Protocol}.
 */
@RestController
@RequestMapping("/api/projects/{projectId}")
public class ProtocolResource {

    private final Logger log = LoggerFactory.getLogger(ProtocolResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProtocolService protocolService;
    private final ProtocolQueryService protocolQueryService;

    public ProtocolResource(ProtocolService protocolService, ProtocolQueryService protocolQueryService) {
        this.protocolService = protocolService;
        this.protocolQueryService = protocolQueryService;
    }

    /**
     * {@code POST  /protocols} : Create a new protocol.
     *
     * @param projectId   ID of the project to which this protocol belongs.
     * @param protocolDTO the protocolDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new protocolDTO, or with status {@code 400 (Bad Request)} if the protocol has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/protocols")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'WRITE')"
    )
    public ResponseEntity<ProtocolDTO> createProtocol(
        @PathVariable("projectId") Long projectId,
        @Valid @RequestBody ProtocolDTO protocolDTO
    ) throws URISyntaxException {
        log.debug("REST request to save Protocol {} in project {}", protocolDTO, projectId);
        if (protocolDTO.getId() != null) {
            throw new BadRequestAlertException("A new protocol cannot already have an ID", EntityNames.PROTOCOL, ErrorKeys.ERR_ID_EXISTS);
        }
        ProtocolDTO result = protocolService.save(projectId, protocolDTO);
        return ResponseEntity.created(new URI("/api/projects/" + projectId + "/protocols/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, EntityNames.PROTOCOL, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST /protocols/import} : import protocols sent in multipart/form-data.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param type      Type of protocols uploaded.
     * @param files     {@link MultipartFile[]} files from multipart/form-data.
     * @return {@link ResponseEntity} with status {@code 200 (OK)} and with
     * body the {@link BulkImportResultDTO}.
     */
    @PostMapping(value = "/protocols/import", consumes = "multipart/form-data")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'WRITE')"
    )
    public ResponseEntity<BulkImportResultDTO<ProtocolDTO>> importProtocols(
        @PathVariable("projectId") Long projectId,
        @RequestParam(value = "type", required = false) String type,
        @RequestParam("file") MultipartFile[] files
    ) {
        log.debug("REST request to import Protocol sent in multipart/form-data in project {}", projectId);
        BulkImportResultDTO<ProtocolDTO> result = protocolService.bulkImportProtocols(projectId, type, files);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code PUT  /protocols} : Updates an existing protocol.
     *
     * @param projectId   ID of the project to which this protocol belongs.
     * @param protocolDTO the protocolDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated protocolDTO,
     * or with status {@code 400 (Bad Request)} if the protocolDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the protocolDTO couldn't be updated.
     */
    @PutMapping("/protocols")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'WRITE')"
    )
    public ResponseEntity<ProtocolDTO> updateProtocol(
        @PathVariable("projectId") Long projectId,
        @Valid @RequestBody ProtocolDTO protocolDTO
    ) {
        log.debug("REST request to update Protocol {} in project {}", protocolDTO, projectId);
        if (protocolDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", EntityNames.PROTOCOL, ErrorKeys.ERR_ID_NULL);
        }
        ProtocolDTO result = protocolService.save(projectId, protocolDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, EntityNames.PROTOCOL, protocolDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /protocols/:id/data} : Updates an existing protocol's data.
     *
     * @param projectId   ID of the project to which this protocol belongs.
     * @param pdDTO the protocolDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated protocolDTO,
     * or with status {@code 400 (Bad Request)} if the protocolDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the protocolDTO couldn't be updated.
     */
    @PostMapping("/protocols/{id}/data")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'WRITE')"
    )
    public ResponseEntity<ProtocolDTO> updateProtocol(
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @Valid @RequestBody ProtocolDataDTO pdDTO
    ) {
        log.debug("REST request to update Protocol {}'s data in project {}", id, projectId);
        ProtocolDTO result = protocolService.saveData(projectId, pdDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, EntityNames.PROTOCOL_DATA, pdDTO.getProtocolId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /protocols} : get all the protocols.
     *
     * @param projectId ID of the project to which the protocols belong.
     * @param pageable  the pagination information.
     * @param criteria  the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of protocols in body.
     */
    @GetMapping("/protocols")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'READ')"
    )
    public ResponseEntity<List<ProtocolDTO>> getAllProtocols(
        @PathVariable("projectId") Long projectId,
        ProtocolCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get Protocols by criteria {} in project {}", criteria, projectId);
        /*Page<ProtocolDTO> page = protocolQueryService.findByCriteria(projectId, criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());*/
        List<ProtocolDTO> protocolDTOs = protocolQueryService.findByCriteria(projectId, criteria);
        return ResponseEntity.ok().body(protocolDTOs);
    }

    /**
     * {@code GET  /protocols/count} : count all the protocols.
     *
     * @param projectId ID of the project to which the protocols belong.
     * @param criteria  the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/protocols/count")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'READ')"
    )
    public ResponseEntity<Long> countProtocols(
        @PathVariable("projectId") Long projectId,
        ProtocolCriteria criteria
    ) {
        log.debug("REST request to count Protocols by criteria {} in project {}", criteria, projectId);
        return ResponseEntity.ok().body(protocolQueryService.countByCriteria(projectId, criteria));
    }

    /**
     * {@code GET  /protocols/:id} : get the "id" protocol.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param id        the id of the protocolDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the protocolDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/protocols/{id}")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'READ')"
    )
    public ResponseEntity<ProtocolDTO> getProtocol(
        @PathVariable("projectId") Long projectId,
        @PathVariable Long id
    ) {
        log.debug("REST request to get Protocol {} in project {}", id, projectId);
        Optional<ProtocolDTO> protocolDTO = protocolService.findOne(projectId, id);
        return ResponseUtil.wrapOrNotFound(protocolDTO);
    }

    /**
     * {@code GET  /protocols/:id/strokes} : get the "id" protocol's strokes.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param id        the id of the protocolDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the protocolDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/protocols/{id}/data")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'READ')"
    )
    public ResponseEntity<ProtocolDataDTO> getProtocolData(
        @PathVariable("projectId") Long projectId,
        @PathVariable Long id
    ) {
        log.debug("REST request to get Protocol {} data in project {}", id, projectId);
        Optional<ProtocolDataDTO> pdDTO = protocolService.findOneData(projectId, id);
        return ResponseUtil.wrapOrNotFound(pdDTO);
    }

    /**
     * {@code GET  /protocols/:id/preview} : get the "id" protocol's preview.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param id        the id of the protocolDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the protocolDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping(value = "/protocols/{id}/preview", produces = {
        MediaType.IMAGE_PNG_VALUE,
        MediaType.APPLICATION_OCTET_STREAM_VALUE
    })
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'READ')"
    )
    public ResponseEntity<byte[]> getProtocolPreview(
        @PathVariable("projectId") Long projectId,
        @PathVariable Long id
    ) {
        log.debug("REST request to get preview for Protocol {} in project {}", id, projectId);
        return ResponseUtil.wrapOrNotFound(protocolService.getPreview(projectId, id));
    }

    /**
     * {@code DELETE  /protocols/:id} : delete the "id" protocol.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param id        the id of the protocolDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/protocols/{id}")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'MANAGE')"
    )
    public ResponseEntity<Void> deleteProtocol(
        @PathVariable("projectId") Long projectId,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete Protocol {} in project {}", id, projectId);
        protocolService.delete(projectId, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, EntityNames.PROTOCOL, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /protocols} : delete many protocols.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param ids       the id of the protocols to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/protocols")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'MANAGE')"
    )
    public ResponseEntity<Void> deleteProtocol(
        @PathVariable("projectId") Long projectId,
        @RequestParam("ids") Long[] ids
    ) {
        log.debug("REST request to delete Protocols {} in project {}", ids, projectId);
        protocolService.deleteMany(projectId, ids);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, EntityNames.PROTOCOL, Arrays.stream(ids).map(String::valueOf).collect(Collectors.joining(","))))
            .build();
    }
}
