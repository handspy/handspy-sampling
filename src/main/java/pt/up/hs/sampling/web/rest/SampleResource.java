package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.service.SampleService;
import pt.up.hs.sampling.web.rest.errors.BadRequestAlertException;
import pt.up.hs.sampling.service.dto.SampleDTO;
import pt.up.hs.sampling.service.dto.SampleCriteria;
import pt.up.hs.sampling.service.SampleQueryService;

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
 * REST controller for managing {@link pt.up.hs.sampling.domain.Sample}.
 */
@RestController
@RequestMapping("/api")
public class SampleResource {

    private final Logger log = LoggerFactory.getLogger(SampleResource.class);

    private static final String ENTITY_NAME = "samplingSample";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SampleService sampleService;

    private final SampleQueryService sampleQueryService;

    public SampleResource(SampleService sampleService, SampleQueryService sampleQueryService) {
        this.sampleService = sampleService;
        this.sampleQueryService = sampleQueryService;
    }

    /**
     * {@code POST  /samples} : Create a new sample.
     *
     * @param sampleDTO the sampleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sampleDTO, or with status {@code 400 (Bad Request)} if the sample has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/samples")
    public ResponseEntity<SampleDTO> createSample(@Valid @RequestBody SampleDTO sampleDTO) throws URISyntaxException {
        log.debug("REST request to save Sample : {}", sampleDTO);
        if (sampleDTO.getId() != null) {
            throw new BadRequestAlertException("A new sample cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SampleDTO result = sampleService.save(sampleDTO);
        return ResponseEntity.created(new URI("/api/samples/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /samples} : Updates an existing sample.
     *
     * @param sampleDTO the sampleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sampleDTO,
     * or with status {@code 400 (Bad Request)} if the sampleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sampleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/samples")
    public ResponseEntity<SampleDTO> updateSample(@Valid @RequestBody SampleDTO sampleDTO) throws URISyntaxException {
        log.debug("REST request to update Sample : {}", sampleDTO);
        if (sampleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SampleDTO result = sampleService.save(sampleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sampleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /samples} : get all the samples.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of samples in body.
     */
    @GetMapping("/samples")
    public ResponseEntity<List<SampleDTO>> getAllSamples(SampleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Samples by criteria: {}", criteria);
        Page<SampleDTO> page = sampleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /samples/count} : count all the samples.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/samples/count")
    public ResponseEntity<Long> countSamples(SampleCriteria criteria) {
        log.debug("REST request to count Samples by criteria: {}", criteria);
        return ResponseEntity.ok().body(sampleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /samples/:id} : get the "id" sample.
     *
     * @param id the id of the sampleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sampleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/samples/{id}")
    public ResponseEntity<SampleDTO> getSample(@PathVariable Long id) {
        log.debug("REST request to get Sample : {}", id);
        Optional<SampleDTO> sampleDTO = sampleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sampleDTO);
    }

    /**
     * {@code DELETE  /samples/:id} : delete the "id" sample.
     *
     * @param id the id of the sampleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/samples/{id}")
    public ResponseEntity<Void> deleteSample(@PathVariable Long id) {
        log.debug("REST request to delete Sample : {}", id);
        sampleService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
