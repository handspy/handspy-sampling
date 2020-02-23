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
     * @param textDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public TextDTO save(TextDTO textDTO) {
        log.debug("Request to save Text : {}", textDTO);
        Text text = textMapper.toEntity(textDTO);
        text = textRepository.save(text);
        return textMapper.toDto(text);
    }

    /**
     * Get all the texts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TextDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Texts");
        return textRepository.findAll(pageable)
            .map(textMapper::toDto);
    }

    /**
     * Get one text by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TextDTO> findOne(Long id) {
        log.debug("Request to get Text : {}", id);
        return textRepository.findById(id)
            .map(textMapper::toDto);
    }

    /**
     * Delete the text by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Text : {}", id);
        textRepository.deleteById(id);
    }
}
