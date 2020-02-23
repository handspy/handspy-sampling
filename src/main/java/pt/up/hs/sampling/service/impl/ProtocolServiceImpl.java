package pt.up.hs.sampling.service.impl;

import pt.up.hs.sampling.service.ProtocolService;
import pt.up.hs.sampling.domain.Protocol;
import pt.up.hs.sampling.repository.ProtocolRepository;
import pt.up.hs.sampling.service.dto.ProtocolDTO;
import pt.up.hs.sampling.service.mapper.ProtocolMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Protocol}.
 */
@Service
@Transactional
public class ProtocolServiceImpl implements ProtocolService {

    private final Logger log = LoggerFactory.getLogger(ProtocolServiceImpl.class);

    private final ProtocolRepository protocolRepository;

    private final ProtocolMapper protocolMapper;

    public ProtocolServiceImpl(ProtocolRepository protocolRepository, ProtocolMapper protocolMapper) {
        this.protocolRepository = protocolRepository;
        this.protocolMapper = protocolMapper;
    }

    /**
     * Save a protocol.
     *
     * @param protocolDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ProtocolDTO save(ProtocolDTO protocolDTO) {
        log.debug("Request to save Protocol : {}", protocolDTO);
        Protocol protocol = protocolMapper.toEntity(protocolDTO);
        protocol = protocolRepository.save(protocol);
        return protocolMapper.toDto(protocol);
    }

    /**
     * Get all the protocols.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProtocolDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Protocols");
        return protocolRepository.findAll(pageable)
            .map(protocolMapper::toDto);
    }

    /**
     * Get one protocol by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProtocolDTO> findOne(Long id) {
        log.debug("Request to get Protocol : {}", id);
        return protocolRepository.findById(id)
            .map(protocolMapper::toDto);
    }

    /**
     * Delete the protocol by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Protocol : {}", id);
        protocolRepository.deleteById(id);
    }
}
