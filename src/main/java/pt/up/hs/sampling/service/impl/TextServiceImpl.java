package pt.up.hs.sampling.service.impl;

import pt.up.hs.sampling.service.TextService;
import pt.up.hs.sampling.domain.Text;
import pt.up.hs.sampling.repository.TextRepository;
import pt.up.hs.sampling.service.dto.TextDTO;
import pt.up.hs.sampling.service.mapper.TextMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
}
