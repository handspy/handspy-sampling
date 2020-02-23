package pt.up.hs.sampling.service.impl;

import pt.up.hs.sampling.service.DotService;
import pt.up.hs.sampling.domain.Dot;
import pt.up.hs.sampling.repository.DotRepository;
import pt.up.hs.sampling.service.dto.DotDTO;
import pt.up.hs.sampling.service.mapper.DotMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Dot}.
 */
@Service
@Transactional
public class DotServiceImpl implements DotService {

    private final Logger log = LoggerFactory.getLogger(DotServiceImpl.class);

    private final DotRepository dotRepository;

    private final DotMapper dotMapper;

    public DotServiceImpl(DotRepository dotRepository, DotMapper dotMapper) {
        this.dotRepository = dotRepository;
        this.dotMapper = dotMapper;
    }

    /**
     * Save a dot.
     *
     * @param dotDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public DotDTO save(DotDTO dotDTO) {
        log.debug("Request to save Dot : {}", dotDTO);
        Dot dot = dotMapper.toEntity(dotDTO);
        dot = dotRepository.save(dot);
        return dotMapper.toDto(dot);
    }

    /**
     * Get all the dots.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<DotDTO> findAll() {
        log.debug("Request to get all Dots");
        return dotRepository.findAll().stream()
            .map(dotMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one dot by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DotDTO> findOne(Long id) {
        log.debug("Request to get Dot : {}", id);
        return dotRepository.findById(id)
            .map(dotMapper::toDto);
    }

    /**
     * Delete the dot by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Dot : {}", id);
        dotRepository.deleteById(id);
    }
}
