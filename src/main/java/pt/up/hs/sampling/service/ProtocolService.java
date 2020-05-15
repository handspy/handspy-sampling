package pt.up.hs.sampling.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import pt.up.hs.sampling.service.dto.BulkImportResultDTO;
import pt.up.hs.sampling.service.dto.ProtocolDTO;
import pt.up.hs.sampling.service.dto.ProtocolDataDTO;

import java.io.InputStream;
import java.util.Optional;

/**
 * Service Interface for managing {@link pt.up.hs.sampling.domain.Protocol}.
 */
public interface ProtocolService {

    /**
     * Save a protocol.
     *
     * @param projectId   ID of the project to which this protocol belongs.
     * @param protocolDTO the entity to save.
     * @return the persisted entity.
     */
    ProtocolDTO save(Long projectId, ProtocolDTO protocolDTO);

    /**
     * Save a protocol data.
     *
     * @param projectId       ID of the project to which this protocol belongs.
     * @param protocolDataDTO the data to save.
     * @return the persisted entity's envelope.
     */
    ProtocolDTO saveData(Long projectId, ProtocolDataDTO protocolDataDTO);

    /**
     * Get all the protocols.
     *
     * @param projectId ID of the project to which the protocols belong.
     * @param pageable  the pagination information.
     * @return the list of entities.
     */
    Page<ProtocolDTO> findAll(Long projectId, Pageable pageable);

    /**
     * Get the "protocolId" protocol.
     *
     * @param projectId  ID of the project to which this protocol belongs.
     * @param id ID of the protocol.
     * @return the data.
     */
    Optional<ProtocolDTO> findOne(Long projectId, Long id);

    /**
     * Get the "id" protocol's data.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param id        the id of the entity.
     * @return the entity.
     */
    Optional<ProtocolDataDTO> findOneData(Long projectId, Long id);

    /**
     * Delete the "id" protocol.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param id        the id of the entity.
     */
    void delete(Long projectId, Long id);

    /**
     * Upload and import protocols in bulk.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param type      type of protocols being uploaded.
     * @param files     {@link MultipartFile} the multipart files.
     * @return {@link BulkImportResultDTO} upload summary.
     */
    BulkImportResultDTO<ProtocolDTO> bulkImportProtocols(Long projectId, String type, MultipartFile[] files);

    /**
     * Get an {@link InputStream} to the image preview.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param id        the id of the entity.
     * @return {@link InputStream} to the image preview.
     */
    Optional<byte[]> getPreview(Long projectId, Long id);
}
