package pt.up.hs.sampling.service;

import org.springframework.web.multipart.MultipartFile;
import pt.up.hs.sampling.service.dto.BulkImportResultDTO;
import pt.up.hs.sampling.service.dto.ProtocolDTO;
import pt.up.hs.sampling.service.dto.ProtocolDataDTO;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
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
     * @return the list of entities.
     */
    List<ProtocolDTO> findAll(Long projectId);

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
     * Delete many protocols.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param ids       the ids of the entities to remove.
     */
    void deleteMany(Long projectId, Long[] ids);

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

    /**
     * Copy a protocol from a project to another.
     *
     * @param projectId            ID of the project to which the protocol belongs.
     * @param id                   ID of the protocol to copy.
     * @param toProjectId          ID of the target project to copy the protocol to.
     * @param move                 is it a move?
     * @param taskMapping          map from source tasks to target tasks
     * @param participantMapping   map from source participants to target tasks
     * @return the persisted entity.
     */
    ProtocolDTO copy(
        Long projectId, Long id, Long toProjectId,
        boolean move,
        Map<Long, Long> taskMapping, Map<Long, Long> participantMapping
    );

    /**
     * Bulk copy protocols from a project to another.
     *
     * @param projectId            ID of the project to which the protocol belongs.
     * @param ids                  ID of the protocols to copy.
     * @param toProjectId          ID of the target project to copy the protocol to.
     * @param move                 is it a move?
     * @param taskMapping          map from source tasks to target tasks
     * @param participantMapping   map from source participants to target tasks
     */
    void bulkCopy(
        Long projectId, Long[] ids, Long toProjectId,
        boolean move,
        Map<Long, Long> taskMapping, Map<Long, Long> participantMapping
    );
}
