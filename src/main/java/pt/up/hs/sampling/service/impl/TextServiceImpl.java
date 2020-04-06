package pt.up.hs.sampling.service.impl;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pt.up.hs.sampling.constants.EntityNames;
import pt.up.hs.sampling.constants.ErrorKeys;
import pt.up.hs.sampling.domain.Text;
import pt.up.hs.sampling.repository.TextRepository;
import pt.up.hs.sampling.service.TextService;
import pt.up.hs.sampling.service.dto.BulkImportResultDTO;
import pt.up.hs.sampling.service.dto.TextDTO;
import pt.up.hs.sampling.service.exceptions.ServiceException;
import pt.up.hs.sampling.service.mapper.TextMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Text}.
 */
@Service
@Transactional
public class TextServiceImpl implements TextService {

    private final Logger log = LoggerFactory.getLogger(TextServiceImpl.class);

    private final TextRepository textRepository;

    private final TextMapper textMapper;

    public TextServiceImpl(TextRepository textRepository, TextMapper textMapper) {
        this.textRepository = textRepository;
        this.textMapper = textMapper;
    }

    /**
     * Save a text.
     *
     * @param projectId ID of the project to which the text belongs.
     * @param textDTO   the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TextDTO save(Long projectId, TextDTO textDTO) {
        log.debug("Request to save Text {} in project {}", textDTO, projectId);
        textDTO.setProjectId(projectId);
        Text text = textMapper.toEntity(textDTO);
        text = textRepository.save(text);
        return textMapper.toDto(text);
    }

    /**
     * Save all texts.
     *
     * @param projectId ID of the project to which these texts belong.
     * @param textDTOs  the entities to save.
     * @return the persisted entities.
     */
    @Override
    public List<TextDTO> saveAll(Long projectId, List<TextDTO> textDTOs) {
        log.debug("Request to save all Texts in project {}", projectId);
        return textRepository
            .saveAll(
                textDTOs.parallelStream()
                    .map(textDTO -> {
                        Text text = textMapper.toEntity(textDTO);
                        text.setProjectId(projectId);
                        return text;
                    })
                    .collect(Collectors.toList())
            ).parallelStream()
            .map(textMapper::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get all the texts.
     *
     * @param projectId ID of the project to which the texts belong.
     * @param pageable  the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TextDTO> findAll(Long projectId, Pageable pageable) {
        log.debug("Request to get all Texts in project {}", projectId);
        return textRepository.findAllByProjectId(projectId, pageable)
            .map(textMapper::toDto);
    }

    /**
     * Get the "id" text.
     *
     * @param projectId ID of the project to which the text belongs.
     * @param id        the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TextDTO> findOne(Long projectId, Long id) {
        log.debug("Request to get Text {} in project {}", id, projectId);
        return textRepository.findByProjectIdAndId(projectId, id)
            .map(textMapper::toDto);
    }

    /**
     * Delete the "id" text.
     *
     * @param projectId ID of the project to which the text belongs.
     * @param id        the id of the entity.
     */
    @Override
    public void delete(Long projectId, Long id) {
        log.debug("Request to delete Text {} in project {}", id, projectId);
        textRepository.deleteAllByProjectIdAndId(projectId, id);
    }

    /**
     * Upload and import texts in bulk.
     *
     * @param projectId ID of the project to which this text belongs.
     * @param files     {@link MultipartFile} the multipart files.
     * @return {@link BulkImportResultDTO} upload summary.
     */
    @Override
    public BulkImportResultDTO<TextDTO> bulkImportTexts(
        Long projectId, MultipartFile[] files
    ) {
        log.debug("Request to bulk import Texts in project {}", projectId);
        BulkImportResultDTO<TextDTO> importResult = new BulkImportResultDTO<>();
        importResult.setTotal(files.length);

        long startTime = new Date().getTime();

        List<TextDTO> textDTOs = new ArrayList<>();
        int invalid = 0;
        for (MultipartFile file : files) {
            try {
                textDTOs.add(parseFileToTextDTO(projectId, file));
            } catch (Exception e) {
                log.error("Failed to read text file", e);
                invalid++;
            }
        }

        try {
            textDTOs = saveAll(projectId, textDTOs);
        } catch (Exception e) {
            log.error("Failed to save texts", e);
            invalid = files.length;
        }

        importResult.setProcessingTime(new Date().getTime() - startTime);
        importResult.setInvalid(invalid);
        importResult.setData(textDTOs);

        return importResult;
    }

    /* Helpers */

    /**
     * Parse file to text DTO.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param file      {@link MultipartFile} the multipart file.
     * @return {@link TextDTO} text DTO.
     */
    private TextDTO parseFileToTextDTO(Long projectId, MultipartFile file) {
        String text;
        try {
            Tika tika = new Tika();
            text = tika.parseToString(file.getInputStream()).trim();
            if (text.isEmpty()) {
                throw new IOException("Unparseable text.");
            }
        } catch (IOException | TikaException e) {
            throw new ServiceException(
                EntityNames.TEXT,
                ErrorKeys.ERR_READ_IMPORT,
                "Could not process imported file."
            );
        }

        TextDTO textDTO = new TextDTO();
        textDTO.setProjectId(projectId);
        textDTO.setText(text);

        return textDTO;
    }
}
