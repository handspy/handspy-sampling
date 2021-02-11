package pt.up.hs.sampling.service;

import org.springframework.web.multipart.MultipartFile;
import pt.up.hs.sampling.service.dto.BulkImportResultDTO;
import pt.up.hs.sampling.service.dto.ProtocolDTO;
import pt.up.hs.sampling.service.dto.TextDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service Interface for managing {@link pt.up.hs.sampling.domain.Text}.
 */
public interface TextService {

    /**
     * Save a text.
     *
     * @param projectId ID of the project to which the text belongs.
     * @param textDTO the entity to save.
     * @return the persisted entity.
     */
    TextDTO save(Long projectId, TextDTO textDTO);

    /**
     * Save all texts.
     *
     * @param projectId ID of the project to which these texts belong.
     * @param textDTOs  the entities to save.
     * @return the persisted entities.
     */
    List<TextDTO> saveAll(Long projectId, List<TextDTO> textDTOs);

    /**
     * Get all the texts.
     *
     * @param projectId ID of the project to which the texts belong.
     * @return the list of entities.
     */
    List<TextDTO> findAll(Long projectId);

    /**
     * Get the "id" text.
     *
     * @param projectId ID of the project to which the text belongs.
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TextDTO> findOne(Long projectId, Long id);

    /**
     * Delete the "id" text.
     *
     * @param projectId ID of the project to which the text belongs.
     * @param id the id of the entity.
     */
    void delete(Long projectId, Long id);

    /**
     * Delete many texts.
     *
     * @param projectId ID of the project to which this text belongs.
     * @param ids       the ids of the texts to remove.
     */
    void deleteMany(Long projectId, Long[] ids);

    /**
     * Upload and import texts in bulk.
     *
     * @param projectId ID of the project to which this text belongs.
     * @param files     {@link MultipartFile} the multipart files.
     * @return {@link BulkImportResultDTO} upload summary.
     */
    BulkImportResultDTO<TextDTO> bulkImportTexts(Long projectId, MultipartFile[] files);

    /**
     * Copy a text from a project to another.
     *
     * @param projectId            ID of the project to which the text belongs.
     * @param id                   ID of the text to copy.
     * @param toProjectId          ID of the target project to copy the text to.
     * @param move                 is it a move?
     * @param taskMapping          map from source tasks to target tasks
     * @param participantMapping   map from source participants to target tasks
     * @return the persisted entity.
     */
    TextDTO copy(
        Long projectId, Long id, Long toProjectId,
        boolean move,
        Map<Long, Long> taskMapping, Map<Long, Long> participantMapping
    );

    /**
     * Bulk copy texts from a project to another.
     *
     * @param projectId            ID of the project to which the protocol belongs.
     * @param ids                  ID of the texts to copy.
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
