package pt.up.hs.sampling.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.up.hs.sampling.domain.Stroke;
import pt.up.hs.sampling.repository.StrokeRepository;
import pt.up.hs.sampling.service.StrokeService;
import pt.up.hs.sampling.service.dto.StrokeDTO;
import pt.up.hs.sampling.service.mapper.StrokeMapper;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link pt.up.hs.sampling.domain.Stroke}.
 */
@Service
@Transactional
public class StrokeServiceImpl implements StrokeService {

    private final Logger log = LoggerFactory.getLogger(StrokeServiceImpl.class);

    private final StrokeRepository strokeRepository;
    private final StrokeMapper strokeMapper;

    public StrokeServiceImpl(StrokeRepository strokeRepository, StrokeMapper strokeMapper) {
        this.strokeRepository = strokeRepository;
        this.strokeMapper = strokeMapper;
    }

    /**
     * Save a stroke.
     *
     * @param projectId ID of the project to which the stroke belongs.
     * @param protocolId  ID of the protocol to which the stroke belongs.
     * @param strokeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public StrokeDTO save(Long projectId, Long protocolId, StrokeDTO strokeDTO) {
        log.debug("Request to save Stroke {} in protocol {} of project {}", strokeDTO, protocolId, projectId);
        strokeDTO.setProtocolId(protocolId);
        Stroke stroke = strokeMapper.toEntity(strokeDTO);
        stroke = strokeRepository.save(stroke);
        return strokeMapper.toDto(stroke);
    }

    /**
     * Get all the strokes.
     *
     * @param projectId ID of the project to which the strokes belong.
     * @param protocolId  ID of the protocol to which the strokes belong.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<StrokeDTO> findAll(Long projectId, Long protocolId) {
        log.debug("Request to get all Strokes in protocol {} of project {}", protocolId, projectId);
        return strokeRepository.findAllByProjectIdAndProtocolId(projectId, protocolId).stream()
            .map(strokeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get the "id" stroke.
     *
     * @param projectId ID of the project to which the stroke belongs.
     * @param protocolId  ID of the protocol to which the stroke belongs.
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<StrokeDTO> findOne(Long projectId, Long protocolId, Long id) {
        log.debug("Request to get Stroke {} in protocol {} of project {}", id, protocolId, projectId);
        return strokeRepository.findByProjectIdAndProtocolIdAndId(projectId, protocolId, id)
            .map(strokeMapper::toDto);
    }

    /**
     * Delete the "id" stroke.
     *
     * @param projectId ID of the project to which the stroke belongs.
     * @param protocolId  ID of the protocol to which the stroke belongs.
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long projectId, Long protocolId, Long id) {
        log.debug("Request to delete Stroke {} in protocol {} of project {}", id, protocolId, projectId);
        strokeRepository.deleteAllByProtocolProjectIdAndProtocolIdAndId(projectId, protocolId, id);
    }
}
