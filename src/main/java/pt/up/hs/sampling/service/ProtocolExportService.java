package pt.up.hs.sampling.service;

import pt.up.hs.sampling.service.dto.ExportFormat;

import java.io.ByteArrayInputStream;

/**
 * Service Interface for managing protocol exports.
 *
 * @author José Carlos Paiva
 */
public interface ProtocolExportService {

    /**
     * Export an analysis of a protocol.
     *
     * @param projectId {@link Long} ID of the project.
     * @param protocolId {@link Long} ID of the protocol from which the analysis is to export.
     * @param format {@link ExportFormat} Format of the export.
     * @return {@link ByteArrayInputStream } of the 'created workbook'.
     */
    ByteArrayInputStream export(Long projectId, Long protocolId, ExportFormat format);

    /**
     * Bulk export the protocols.
     *
     * @param projectId {@link Long} ID of the project.
     * @param protocolIds {@link Long[]} ID of the protocols from which the analyses are to export.
     * @param format {@link ExportFormat} Format of the export.
     * @return {@link ByteArrayInputStream} of the created workbook.
     */
    ByteArrayInputStream bulkExport(Long projectId, Long[] protocolIds, ExportFormat format);
}
