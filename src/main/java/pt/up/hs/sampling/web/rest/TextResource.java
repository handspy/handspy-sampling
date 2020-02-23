package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.service.TextService;
import pt.up.hs.sampling.web.rest.errors.BadRequestAlertException;
import pt.up.hs.sampling.service.dto.TextDTO;
import pt.up.hs.sampling.service.dto.TextCriteria;
import pt.up.hs.sampling.service.TextQueryService;

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
 * REST controller for managing {@link pt.up.hs.sampling.domain.Text}.
 */
@RestController
@RequestMapping("/api")
public class TextResource {

    private final Logger log = LoggerFactory.getLogger(TextResource.class);

    private static final String ENTITY_NAME = "samplingText";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TextService textService;

    private final TextQueryService textQueryService;

    public TextResource(TextService textService, TextQueryService textQueryService) {
        this.textService = textService;
        this.textQueryService = textQueryService;
    }

    /**
     * {@code POST  /texts} : Create a new text.
     *
     * @param textDTO the textDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new textDTO, or with status {@code 400 (Bad Request)} if the text has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/texts")
    public ResponseEntity<TextDTO> createText(@Valid @RequestBody TextDTO textDTO) throws URISyntaxException {
        log.debug("REST request to save Text : {}", textDTO);
        if (textDTO.getId() != null) {
            throw new BadRequestAlertException("A new text cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TextDTO result = textService.save(textDTO);
        return ResponseEntity.created(new URI("/api/texts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /texts} : Updates an existing text.
     *
     * @param textDTO the textDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated textDTO,
     * or with status {@code 400 (Bad Request)} if the textDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the textDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/texts")
    public ResponseEntity<TextDTO> updateText(@Valid @RequestBody TextDTO textDTO) throws URISyntaxException {
        log.debug("REST request to update Text : {}", textDTO);
        if (textDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TextDTO result = textService.save(textDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, textDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /texts} : get all the texts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of texts in body.
     */
    @GetMapping("/texts")
    public ResponseEntity<List<TextDTO>> getAllTexts(TextCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Texts by criteria: {}", criteria);
        Page<TextDTO> page = textQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /texts/count} : count all the texts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/texts/count")
    public ResponseEntity<Long> countTexts(TextCriteria criteria) {
        log.debug("REST request to count Texts by criteria: {}", criteria);
        return ResponseEntity.ok().body(textQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /texts/:id} : get the "id" text.
     *
     * @param id the id of the textDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the textDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/texts/{id}")
    public ResponseEntity<TextDTO> getText(@PathVariable Long id) {
        log.debug("REST request to get Text : {}", id);
        Optional<TextDTO> textDTO = textService.findOne(id);
        return ResponseUtil.wrapOrNotFound(textDTO);
    }

    /**
     * {@code DELETE  /texts/:id} : delete the "id" text.
     *
     * @param id the id of the textDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/texts/{id}")
    public ResponseEntity<Void> deleteText(@PathVariable Long id) {
        log.debug("REST request to delete Text : {}", id);
        textService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
