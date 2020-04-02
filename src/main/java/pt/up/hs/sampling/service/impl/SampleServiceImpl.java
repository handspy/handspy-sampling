package pt.up.hs.sampling.service.impl;

import pt.up.hs.sampling.service.SampleService;
import pt.up.hs.sampling.domain.Sample;
import pt.up.hs.sampling.repository.SampleRepository;
import pt.up.hs.sampling.service.dto.SampleDTO;
import pt.up.hs.sampling.service.mapper.SampleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Sample}.
 */
@Service
@Transactional
public class SampleServiceImpl implements SampleService {

    private final Logger log = LoggerFactory.getLogger(SampleServiceImpl.class);

    private final SampleRepository sampleRepository;

    private final SampleMapper sampleMapper;

    public SampleServiceImpl(SampleRepository sampleRepository, SampleMapper sampleMapper) {
        this.sampleRepository = sampleRepository;
        this.sampleMapper = sampleMapper;
    }

    /**
     * Save a sample.
     *
     * @param projectId ID of the project to which the sample belongs.
     * @param sampleDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SampleDTO save(Long projectId, SampleDTO sampleDTO) {
        log.debug("Request to save Sample {} in project {}", sampleDTO, projectId);
        sampleDTO.setProjectId(projectId);
        Sample sample = sampleMapper.toEntity(sampleDTO);
        sample = sampleRepository.save(sample);
        return sampleMapper.toDto(sample);
    }

    /**
     * Get all the samples.
     *
     * @param projectId ID of the project to which the samples belong.
     * @param pageable  the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SampleDTO> findAll(Long projectId, Pageable pageable) {
        log.debug("Request to get all Samples in project {}", projectId);
        return sampleRepository.findAllByProjectId(projectId, pageable)
            .map(sampleMapper::toDto);
    }

    /**
     * Get the "id" sample.
     *
     * @param projectId ID of the project to which the sample belongs.
     * @param id        the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SampleDTO> findOne(Long projectId, Long id) {
        log.debug("Request to get Sample {} in project {}", id, projectId);
        return sampleRepository.findByProjectIdAndId(projectId, id)
            .map(sampleMapper::toDto);
    }

    /**
     * Delete the "id" sample.
     *
     * @param projectId ID of the project to which the sample belongs.
     * @param id        the id of the entity.
     */
    @Override
    public void delete(Long projectId, Long id) {
        log.debug("Request to delete Sample {} in project {}", id, projectId);
        sampleRepository.deleteAllByProjectIdAndId(projectId, id);
    }
}
