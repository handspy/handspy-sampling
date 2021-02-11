package pt.up.hs.sampling.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pt.up.hs.sampling.constants.EntityNames;
import pt.up.hs.sampling.constants.ErrorKeys;
import pt.up.hs.sampling.service.TextQueryService;
import pt.up.hs.sampling.service.TextService;
import pt.up.hs.sampling.service.dto.BulkImportResultDTO;
import pt.up.hs.sampling.service.dto.TextCriteria;
import pt.up.hs.sampling.service.dto.TextDTO;
import pt.up.hs.sampling.web.rest.errors.BadRequestAlertException;
import pt.up.hs.sampling.web.rest.vm.CopyPayload;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link pt.up.hs.sampling.domain.Text}.
 */
@RestController
@RequestMapping("/api/projects/{projectId}")
public class TextResource {

    private final Logger log = LoggerFactory.getLogger(TextResource.class);

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
     * @param projectId ID of the project to which this text belongs.
     * @param textDTO   the textDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and
     * with body the new textDTO, or with status {@code 400 (Bad Request)} if
     * the text has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/texts")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'WRITE')"
    )
    public ResponseEntity<TextDTO> createText(
        @PathVariable("projectId") Long projectId,
        @Valid @RequestBody TextDTO textDTO
    ) throws URISyntaxException {
        log.debug("REST request to save Text {} in project {}", textDTO, projectId);
        if (textDTO.getId() != null) {
            throw new BadRequestAlertException("A new text cannot already have an ID", EntityNames.TEXT, ErrorKeys.ERR_ID_EXISTS);
        }
        TextDTO result = textService.save(projectId, textDTO);
        return ResponseEntity.created(new URI("/api/projects/" + projectId + "/texts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, EntityNames.TEXT, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST /texts/import} : import texts sent in multipart/form-data.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param files     {@link MultipartFile[]} files from multipart/form-data.
     * @return {@link ResponseEntity} with status {@code 200 (OK)} and with
     * body the {@link BulkImportResultDTO}.
     */
    @PostMapping(value = "/texts/import", consumes = "multipart/form-data")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'WRITE')"
    )
    public ResponseEntity<BulkImportResultDTO<TextDTO>> importTexts(
        @PathVariable("projectId") Long projectId,
        @RequestParam("file") MultipartFile[] files
    ) {
        log.debug("REST request to import Texts sent in multipart/form-data in project {}", projectId);
        BulkImportResultDTO<TextDTO> result = textService.bulkImportTexts(projectId, files);
        return ResponseEntity.ok(result);
    }

    /**
     * {@code PUT  /texts} : Updates an existing text.
     *
     * @param projectId ID of the project to which this text belongs.
     * @param textDTO   the textDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated textDTO,
     * or with status {@code 400 (Bad Request)} if the textDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the textDTO couldn't be updated.
     */
    @PutMapping("/texts")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'WRITE')"
    )
    public ResponseEntity<TextDTO> updateText(
        @PathVariable("projectId") Long projectId,
        @Valid @RequestBody TextDTO textDTO
    ) {
        log.debug("REST request to update Text {} in project {}", textDTO, projectId);
        if (textDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", EntityNames.TEXT, ErrorKeys.ERR_ID_NULL);
        }
        TextDTO result = textService.save(projectId, textDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, EntityNames.TEXT, textDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /texts} : get all the texts.
     *
     * @param projectId ID of the project to which the texts belong.
     * @param pageable  the pagination information.
     * @param criteria  the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of texts in body.
     */
    @GetMapping("/texts")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'READ')"
    )
    public ResponseEntity<List<TextDTO>> getAllTexts(
        @PathVariable("projectId") Long projectId,
        TextCriteria criteria, Pageable pageable
    ) {
        log.debug("REST request to get Texts by criteria {} in project {}", criteria, projectId);
        /*Page<TextDTO> page = textQueryService.findByCriteria(projectId, criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(), page
        );
        return ResponseEntity.ok()
            .headers(headers)
            .body(page.getContent());*/
        List<TextDTO> textDTOs = textQueryService.findByCriteria(projectId, criteria);
        return ResponseEntity.ok().body(textDTOs);
    }

    /**
     * {@code GET  /texts/count} : count all the texts.
     *
     * @param projectId ID of the project to which the texts belong.
     * @param criteria  the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/texts/count")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'READ')"
    )
    public ResponseEntity<Long> countTexts(
        @PathVariable("projectId") Long projectId,
        TextCriteria criteria
    ) {
        log.debug("REST request to count Texts by criteria {} in project {}", criteria, projectId);
        return ResponseEntity.ok().body(textQueryService.countByCriteria(projectId, criteria));
    }

    /**
     * {@code GET  /texts/:id} : get the "id" text.
     *
     * @param projectId ID of the project to which this text belongs.
     * @param id        the id of the textDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the textDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/texts/{id}")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'READ')"
    )
    public ResponseEntity<TextDTO> getText(
        @PathVariable("projectId") Long projectId,
        @PathVariable Long id
    ) {
        log.debug("REST request to get Text {} from project {}", id, projectId);
        Optional<TextDTO> textDTO = textService.findOne(projectId, id);
        return ResponseUtil.wrapOrNotFound(textDTO);
    }

    /**
     * {@code DELETE  /texts/:id} : delete the "id" text.
     *
     * @param projectId ID of the project to which this text belongs.
     * @param id        the id of the textDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/texts/{id}")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'MANAGE')"
    )
    public ResponseEntity<Void> deleteText(
        @PathVariable("projectId") Long projectId,
        @PathVariable Long id
    ) {
        log.debug("REST request to delete Text {} from project {}", id, projectId);
        textService.delete(projectId, id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, EntityNames.TEXT, id.toString()))
            .build();
    }

    /**
     * {@code DELETE  /texts} : delete many texts.
     *
     * @param projectId ID of the project to which this text belongs.
     * @param ids       the id of the texts to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/texts")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'MANAGE')"
    )
    public ResponseEntity<Void> deleteProtocol(
        @PathVariable("projectId") Long projectId,
        @RequestParam("ids") Long[] ids
    ) {
        log.debug("REST request to delete Texts {} in project {}", ids, projectId);
        textService.deleteMany(projectId, ids);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, EntityNames.TEXT, Arrays.stream(ids).map(String::valueOf).collect(Collectors.joining(","))))
            .build();
    }

    @PostMapping("/texts/{id}/copy")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'READ') and " +
            "(not #payload.move or hasPermission(#projectId, 'Project', 'MANAGE')) and " +
            "hasPermission(#payload.projectId, 'Project', 'WRITE')"
    )
    public ResponseEntity<TextDTO> copy(
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @Valid @RequestBody CopyPayload payload
    ) throws URISyntaxException {
        log.debug("REST request to copy Text {} from project {} to project {}", id, projectId, payload.getProjectId());
        TextDTO result = textService.copy(projectId, id, payload.getProjectId(), payload.isMove(), payload.getTaskMapping(), payload.getParticipantMapping());
        return ResponseEntity
            .created(new URI("/api/projects/" + payload.getProjectId() + "/texts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, EntityNames.TEXT, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/texts/copy")
    @PreAuthorize(
        "hasAnyRole('ROLE_USER', 'ROLE_ADVANCED_USER', 'ROLE_ADMIN') and " +
            "hasPermission(#projectId, 'Project', 'READ') and " +
            "(not #payload.move or hasPermission(#projectId, 'Project', 'MANAGE')) and " +
            "hasPermission(#payload.projectId, 'Project', 'WRITE')"
    )
    public ResponseEntity<Void> bulkCopy(
        @PathVariable("projectId") Long projectId,
        @RequestParam("ids") Long[] ids,
        @Valid @RequestBody CopyPayload payload
    ) {
        log.debug("REST request to copy Texts {} from project {} to project {}", ids, projectId, payload.getProjectId());
        textService.bulkCopy(projectId, ids, payload.getProjectId(), payload.isMove(), payload.getTaskMapping(), payload.getParticipantMapping());
        return ResponseEntity.ok().build();
    }
}
