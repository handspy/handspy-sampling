package pt.up.hs.sampling.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.up.hs.sampling.service.ProtocolExportService;
import pt.up.hs.sampling.service.dto.ExportFormat;

import java.io.ByteArrayInputStream;

/**
 * REST controller for exporting pause-burst reports.
 */
@RestController
@RequestMapping("/api/projects/{projectId}/protocols")
public class ProtocolExportResource {

    private final Logger log = LoggerFactory.getLogger(ProtocolExportResource.class);

    private final ProtocolExportService exportService;

    public ProtocolExportResource(ProtocolExportService exportService) {
        this.exportService = exportService;
    }

    /**
     * {@code GET  /export} : export analysis with ID.
     *
     * @param projectId     ID of the project.
     * @param protocolId    ID of the protocol.
     * @param formatStr     Export format.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the excel file.
     */
    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> export(
        @PathVariable("projectId") Long projectId,
        @RequestParam(value = "protocolId") Long protocolId,
        @RequestParam(value = "format", required = false, defaultValue = "JSON") String formatStr
    ) {
        log.debug("REST request to export the protocol {} of project {}", protocolId, projectId);

        ExportFormat exportFormat;
        try {
            exportFormat = ExportFormat.valueOf(formatStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            exportFormat = ExportFormat.JSON;
        }

        ByteArrayInputStream bais = exportService.export(projectId, protocolId, exportFormat);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", exportFormat.getMimeType());
        headers.add("Content-Disposition",
            "attachment; filename=protocol_" + projectId + "_" + protocolId + "." + exportFormat.getExtension());
        return ResponseEntity.ok()
            .headers(headers)
            .body(new InputStreamResource(bais));
    }

    /**
     * {@code GET  /export} : export analyses of text IDs.
     *
     * @param projectId     ID of the project.
     * @param protocolIds   IDs of the protocols analyzed.
     * @param formatStr     Export format.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the excel file.
     */
    @GetMapping("/bulk-export")
    public ResponseEntity<InputStreamResource> bulkExport(
        @PathVariable("projectId") Long projectId,
        @RequestParam(value = "protocolId[]") Long[] protocolIds,
        @RequestParam(value = "format", required = false, defaultValue = "JSON") String formatStr
    ) {
        log.debug("REST request to bulk export the protocols {} of project {}", protocolIds, projectId);

        ExportFormat exportFormat;
        try {
            exportFormat = ExportFormat.valueOf(formatStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            exportFormat = ExportFormat.JSON;
        }

        ByteArrayInputStream bais = exportService.bulkExport(projectId, protocolIds, exportFormat);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/zip");
        headers.add("Content-Disposition",
            "attachment; filename=protocols_" + projectId + ".zip");
        return ResponseEntity.ok()
            .headers(headers)
            .body(new InputStreamResource(bais));
    }
}
