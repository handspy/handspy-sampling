package pt.up.hs.sampling.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.up.hs.sampling.constants.EntityNames;
import pt.up.hs.sampling.service.ProtocolQueryService;
import pt.up.hs.sampling.service.ProtocolService;
import pt.up.hs.sampling.service.dto.BulkImportResultDTO;
import pt.up.hs.sampling.service.dto.ProtocolCriteria;
import pt.up.hs.sampling.service.dto.ProtocolDTO;
import pt.up.hs.sampling.service.exceptions.ServiceException;
import pt.up.hs.sampling.web.rest.errors.BadRequestAlertException;
import pt.up.hs.sampling.constants.ErrorKeys;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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
     * @param file      {@link MultipartFile} file from multipart/form-data.
     * @return {@link ResponseEntity} with status {@code 200 (OK)} and with
     * body the {@link BulkImportResultDTO}.
     */
    @PostMapping(value = "/protocols/import", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and hasPermission(#projectId, 'pt.up.hs.sampling.domain.Protocol', 'WRITE')")
    public ResponseEntity<BulkImportResultDTO<ProtocolDTO>> importMultipart(
        @PathVariable("projectId") Long projectId,
        @RequestParam(value = "type", required = false) String type,
        @RequestParam("file") MultipartFile file
    ) {
        log.debug("REST request to import Protocol sent in multipart/form-data in project {}", projectId);
        BulkImportResultDTO<ProtocolDTO> result = protocolService.importProtocol(projectId, type, file);
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
     * {@code GET  /protocols} : get all the protocols.
     *
     * @param projectId ID of the project to which the protocols belong.
     * @param pageable  the pagination information.
     * @param criteria  the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of protocols in body.
     */
    @GetMapping("/protocols")
    public ResponseEntity<List<ProtocolDTO>> getAllProtocols(
        @PathVariable("projectId") Long projectId,
        ProtocolCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get Protocols by criteria {} in project {}", criteria, projectId);
        Page<ProtocolDTO> page = protocolQueryService.findByCriteria(projectId, criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /protocols/count} : count all the protocols.
     *
     * @param projectId ID of the project to which the protocols belong.
     * @param criteria  the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/protocols/count")
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
    public ResponseEntity<ProtocolDTO> getProtocol(
        @PathVariable("projectId") Long projectId,
        @PathVariable Long id
    ) {
        log.debug("REST request to get Protocol {} in project {}", id, projectId);
        Optional<ProtocolDTO> protocolDTO = protocolService.findOne(projectId, id);
        return ResponseUtil.wrapOrNotFound(protocolDTO);
    }

    /**
     * {@code DELETE  /protocols/:id} : delete the "id" protocol.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param id        the id of the protocolDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/protocols/{id}")
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
}
