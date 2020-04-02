package pt.up.hs.sampling.service;

import org.springframework.web.multipart.MultipartFile;
import pt.up.hs.sampling.service.dto.BulkImportResultDTO;
import pt.up.hs.sampling.service.dto.ProtocolDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
     * Get all the protocols.
     *
     * @param projectId ID of the project to which the protocols belong.
     * @param pageable  the pagination information.
     * @return the list of entities.
     */
    Page<ProtocolDTO> findAll(Long projectId, Pageable pageable);

    /**
     * Get the "id" protocol.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param id        the id of the entity.
     * @return the entity.
     */
    Optional<ProtocolDTO> findOne(Long projectId, Long id);

    /**
     * Delete the "id" protocol.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param id        the id of the entity.
     */
    void delete(Long projectId, Long id);

    /**
     * Upload and import protocols.
     *
     * @param projectId   ID of the project to which this protocol belongs.
     * @param type        type of protocol being uploaded.
     * @param file        {@link InputStream} the file input stream.
     * @return {@link BulkImportResultDTO} response to protocol upload.
     */
    BulkImportResultDTO<ProtocolDTO> importProtocol(Long projectId, String type, MultipartFile file);
}
