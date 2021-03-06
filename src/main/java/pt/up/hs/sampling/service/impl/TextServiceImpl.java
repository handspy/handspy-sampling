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
import org.zalando.problem.Status;
import pt.up.hs.sampling.constants.EntityNames;
import pt.up.hs.sampling.constants.ErrorKeys;
import pt.up.hs.sampling.domain.Protocol;
import pt.up.hs.sampling.domain.Text;
import pt.up.hs.sampling.processing.cloner.ProtocolClonerJobLauncher;
import pt.up.hs.sampling.processing.cloner.TextClonerJobLauncher;
import pt.up.hs.sampling.repository.TextRepository;
import pt.up.hs.sampling.service.TextService;
import pt.up.hs.sampling.service.dto.BulkImportResultDTO;
import pt.up.hs.sampling.service.dto.TextDTO;
import pt.up.hs.sampling.service.exceptions.ServiceException;
import pt.up.hs.sampling.service.mapper.TextMapper;

import java.io.IOException;
import java.util.*;
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

    private final TextClonerJobLauncher textClonerJobLauncher;

    public TextServiceImpl(
        TextRepository textRepository,
        TextMapper textMapper,
        TextClonerJobLauncher textClonerJobLauncher
    ) {
        this.textRepository = textRepository;
        this.textMapper = textMapper;
        this.textClonerJobLauncher = textClonerJobLauncher;
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
        if (textDTO.getTaskId() != null && textDTO.getParticipantId() != null) {
            Optional<Text> textOptional = textRepository
                .findByProjectIdAndTaskIdAndParticipantId(projectId, textDTO.getTaskId(), textDTO.getParticipantId());
            if (textOptional.isPresent()) {
                if (!textOptional.get().getId().equals(textDTO.getId())) {
                    throw new ServiceException(Status.BAD_REQUEST, EntityNames.TEXT, ErrorKeys.ERR_EXISTS, "A text for this participant in this task already exists.");
                }
            }
        }
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
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<TextDTO> findAll(Long projectId) {
        log.debug("Request to get all Texts in project {}", projectId);
        return textRepository.findAllByProjectId(projectId)
            .parallelStream()
            .map(textMapper::toDto)
            .collect(Collectors.toList());
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
     * Delete many texts.
     *
     * @param projectId ID of the project to which this text belongs.
     * @param ids       the ids of the texts to remove.
     */
    @Override
    public void deleteMany(Long projectId, Long[] ids) {
        log.debug("Request to delete all texts {} in project {}", ids, projectId);
        textRepository.deleteAllByProjectIdAndIdIn(projectId, Arrays.asList(ids));
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

    @Override
    public TextDTO copy(
        Long projectId, Long id,
        Long toProjectId, boolean move,
        Map<Long, Long> taskMapping, Map<Long, Long> participantMapping
    ) {

        // find previous text
        TextDTO oldTextDTO = findOne(projectId, id).orElse(null);
        if (oldTextDTO == null) {
            throw new ServiceException(Status.NOT_FOUND, EntityNames.TEXT, ErrorKeys.ERR_NOT_FOUND, "Text does not exist");
        }

        // create & save new text
        TextDTO textDTO = new TextDTO();
        textDTO.setProjectId(toProjectId);
        textDTO.setLanguage(oldTextDTO.getLanguage());
        textDTO.setText(oldTextDTO.getText());
        if (!projectId.equals(toProjectId)) {
            if (oldTextDTO.getTaskId() != null) {
                textDTO.setTaskId(taskMapping.get(oldTextDTO.getTaskId()));
            }
            if (oldTextDTO.getParticipantId() != null) {
                textDTO.setParticipantId(participantMapping.get(oldTextDTO.getParticipantId()));
            }
        } else {
            textDTO.setTaskId(oldTextDTO.getTaskId());
            textDTO.setParticipantId(oldTextDTO.getParticipantId());
        }
        textDTO.setAnnotations(new HashSet<>());
        textDTO = save(toProjectId, textDTO);

        // delete previous if moving
        if (move) {
            delete(projectId, id);
        }

        return textDTO;
    }

    @Override
    public void bulkCopy(
        Long projectId, Long[] ids, Long toProjectId,
        boolean move,
        Map<Long, Long> taskMapping, Map<Long, Long> participantMapping
    ) {
        List<Long> idsList;
        if (ids == null || ids.length == 0) {
            List<Text> texts = textRepository.findAllByProjectId(projectId);
            idsList = texts.parallelStream()
                .map(Text::getId)
                .collect(Collectors.toList());
        } else {
            idsList = Arrays.stream(ids).collect(Collectors.toList());
        }

        textClonerJobLauncher.run(projectId, toProjectId, idsList, move, taskMapping, participantMapping);
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
            tika.setMaxStringLength(-1);
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
