package pt.up.hs.sampling.service.impl;

import pt.up.hs.sampling.service.LayoutService;
import pt.up.hs.sampling.domain.Layout;
import pt.up.hs.sampling.repository.LayoutRepository;
import pt.up.hs.sampling.service.dto.LayoutDTO;
import pt.up.hs.sampling.service.mapper.LayoutMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Layout}.
 */
@Service
@Transactional
public class LayoutServiceImpl implements LayoutService {

    private final Logger log = LoggerFactory.getLogger(LayoutServiceImpl.class);

    private final LayoutRepository layoutRepository;

    private final LayoutMapper layoutMapper;

    public LayoutServiceImpl(LayoutRepository layoutRepository, LayoutMapper layoutMapper) {
        this.layoutRepository = layoutRepository;
        this.layoutMapper = layoutMapper;
    }

    /**
     * Save a layout.
     *
     * @param layoutDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public LayoutDTO save(LayoutDTO layoutDTO) {
        log.debug("Request to save Layout : {}", layoutDTO);
        Layout layout = layoutMapper.toEntity(layoutDTO);
        layout = layoutRepository.save(layout);
        return layoutMapper.toDto(layout);
    }

    /**
     * Get all the layouts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<LayoutDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Layouts");
        return layoutRepository.findAll(pageable)
            .map(layoutMapper::toDto);
    }

    /**
     * Get one layout by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<LayoutDTO> findOne(Long id) {
        log.debug("Request to get Layout : {}", id);
        return layoutRepository.findById(id)
            .map(layoutMapper::toDto);
    }

    /**
     * Delete the layout by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Layout : {}", id);
        layoutRepository.deleteById(id);
    }
}
