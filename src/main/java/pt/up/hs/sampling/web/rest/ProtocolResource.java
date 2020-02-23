package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.service.ProtocolService;
import pt.up.hs.sampling.web.rest.errors.BadRequestAlertException;
import pt.up.hs.sampling.service.dto.ProtocolDTO;
import pt.up.hs.sampling.service.dto.ProtocolCriteria;
import pt.up.hs.sampling.service.ProtocolQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.up.hs.sampling.domain.Protocol}.
 */
@RestController
@RequestMapping("/api")
public class ProtocolResource {

    private final Logger log = LoggerFactory.getLogger(ProtocolResource.class);

    private static final String ENTITY_NAME = "samplingProtocol";

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
     * @param protocolDTO the protocolDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new protocolDTO, or with status {@code 400 (Bad Request)} if the protocol has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/protocols")
    public ResponseEntity<ProtocolDTO> createProtocol(@Valid @RequestBody ProtocolDTO protocolDTO) throws URISyntaxException {
        log.debug("REST request to save Protocol : {}", protocolDTO);
        if (protocolDTO.getId() != null) {
            throw new BadRequestAlertException("A new protocol cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProtocolDTO result = protocolService.save(protocolDTO);
        return ResponseEntity.created(new URI("/api/protocols/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /protocols} : Updates an existing protocol.
     *
     * @param protocolDTO the protocolDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated protocolDTO,
     * or with status {@code 400 (Bad Request)} if the protocolDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the protocolDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/protocols")
    public ResponseEntity<ProtocolDTO> updateProtocol(@Valid @RequestBody ProtocolDTO protocolDTO) throws URISyntaxException {
        log.debug("REST request to update Protocol : {}", protocolDTO);
        if (protocolDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProtocolDTO result = protocolService.save(protocolDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, protocolDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /protocols} : get all the protocols.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of protocols in body.
     */
    @GetMapping("/protocols")
    public ResponseEntity<List<ProtocolDTO>> getAllProtocols(ProtocolCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Protocols by criteria: {}", criteria);
        Page<ProtocolDTO> page = protocolQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /protocols/count} : count all the protocols.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/protocols/count")
    public ResponseEntity<Long> countProtocols(ProtocolCriteria criteria) {
        log.debug("REST request to count Protocols by criteria: {}", criteria);
        return ResponseEntity.ok().body(protocolQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /protocols/:id} : get the "id" protocol.
     *
     * @param id the id of the protocolDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the protocolDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/protocols/{id}")
    public ResponseEntity<ProtocolDTO> getProtocol(@PathVariable Long id) {
        log.debug("REST request to get Protocol : {}", id);
        Optional<ProtocolDTO> protocolDTO = protocolService.findOne(id);
        return ResponseUtil.wrapOrNotFound(protocolDTO);
    }

    /**
     * {@code DELETE  /protocols/:id} : delete the "id" protocol.
     *
     * @param id the id of the protocolDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/protocols/{id}")
    public ResponseEntity<Void> deleteProtocol(@PathVariable Long id) {
        log.debug("REST request to delete Protocol : {}", id);
        protocolService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
