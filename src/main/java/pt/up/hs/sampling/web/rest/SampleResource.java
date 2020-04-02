package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.constants.EntityNames;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.up.hs.sampling.constants.ErrorKeys;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link pt.up.hs.sampling.domain.Sample}.
 */
@RestController
@RequestMapping("/api/projects/{projectId}")
public class SampleResource {

    private final Logger log = LoggerFactory.getLogger(SampleResource.class);

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
     * @param projectId ID of the project to which the sample belongs.
     * @param sampleDTO the sampleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sampleDTO, or with status {@code 400 (Bad Request)} if the sample has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/samples")
    public ResponseEntity<SampleDTO> createSample(
        @PathVariable("projectId") Long projectId,
        @Valid @RequestBody SampleDTO sampleDTO
    ) throws URISyntaxException {
        log.debug("REST request to save Sample {} in project {}", sampleDTO, projectId);
        if (sampleDTO.getId() != null) {
            throw new BadRequestAlertException("A new sample cannot already have an ID", EntityNames.SAMPLE, ErrorKeys.ERR_ID_EXISTS);
        }
        SampleDTO result = sampleService.save(projectId, sampleDTO);
        return ResponseEntity.created(new URI("/api/projects/" + projectId + "/samples/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, EntityNames.SAMPLE, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /samples} : Updates an existing sample.
     *
     * @param projectId ID of the project to which the sample belongs.
     * @param sampleDTO the sampleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sampleDTO,
     * or with status {@code 400 (Bad Request)} if the sampleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sampleDTO couldn't be updated.
     */
    @PutMapping("/samples")
    public ResponseEntity<SampleDTO> updateSample(
        @PathVariable("projectId") Long projectId,
        @Valid @RequestBody SampleDTO sampleDTO
    ) {
        log.debug("REST request to update Sample {} in project {}", sampleDTO, projectId);
        if (sampleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", EntityNames.SAMPLE, ErrorKeys.ERR_ID_NULL);
        }
        SampleDTO result = sampleService.save(projectId, sampleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, EntityNames.SAMPLE, sampleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /samples} : get all the samples.
     *
     * @param projectId ID of the project to which the samples belong.
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of samples in body.
     */
    @GetMapping("/samples")
    public ResponseEntity<List<SampleDTO>> getAllSamples(
        @PathVariable("projectId") Long projectId,
        SampleCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get Samples by criteria {} in project {}", criteria, projectId);
        Page<SampleDTO> page = sampleQueryService.findByCriteria(projectId, criteria, pageable);
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /samples/count} : count all the samples.
     *
     * @param projectId ID of the project to which the samples belong.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/samples/count")
    public ResponseEntity<Long> countSamples(
        @PathVariable("projectId") Long projectId,
        SampleCriteria criteria
    ) {
        log.debug("REST request to count Samples by criteria {} in project {}", criteria, projectId);
        return ResponseEntity.ok().body(sampleQueryService.countByCriteria(projectId, criteria));
    }

    /**
     * {@code GET  /samples/:id} : get the "id" sample.
     *
     * @param projectId ID of the project to which the sample belongs.
     * @param id the id of the sampleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sampleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/samples/{id}")
    public ResponseEntity<SampleDTO> getSample(
        @PathVariable("projectId") Long projectId,
        @PathVariable Long id
    ) {
        log.debug("REST request to get Sample {} in project {}", id, projectId);
        Optional<SampleDTO> sampleDTO = sampleService.findOne(projectId, id);
        return ResponseUtil.wrapOrNotFound(sampleDTO);
    }

    /**
     * {@code DELETE  /samples/:id} : delete the "id" sample.
     *
     * @param projectId ID of the project to which the sample belongs.
     * @param id the id of the sampleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/samples/{id}")
    public ResponseEntity<Void> deleteSample(
        @PathVariable("projectId") Long projectId,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete Sample {} from project {}", id, projectId);
        sampleService.delete(projectId, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, EntityNames.SAMPLE, id.toString()))
            .build();
    }
}
