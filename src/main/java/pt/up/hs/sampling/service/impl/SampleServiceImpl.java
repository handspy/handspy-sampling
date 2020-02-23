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
     * @param sampleDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public SampleDTO save(SampleDTO sampleDTO) {
        log.debug("Request to save Sample : {}", sampleDTO);
        Sample sample = sampleMapper.toEntity(sampleDTO);
        sample = sampleRepository.save(sample);
        return sampleMapper.toDto(sample);
    }

    /**
     * Get all the samples.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<SampleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Samples");
        return sampleRepository.findAll(pageable)
            .map(sampleMapper::toDto);
    }

    /**
     * Get one sample by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<SampleDTO> findOne(Long id) {
        log.debug("Request to get Sample : {}", id);
        return sampleRepository.findById(id)
            .map(sampleMapper::toDto);
    }

    /**
     * Delete the sample by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sample : {}", id);
        sampleRepository.deleteById(id);
    }
}
