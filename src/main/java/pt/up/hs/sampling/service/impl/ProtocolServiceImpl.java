package pt.up.hs.sampling.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pt.up.hs.sampling.constants.EntityNames;
import pt.up.hs.sampling.constants.ErrorKeys;
import pt.up.hs.sampling.domain.Protocol;
import pt.up.hs.sampling.repository.ProtocolRepository;
import pt.up.hs.sampling.service.ProtocolService;
import pt.up.hs.sampling.service.dto.BulkImportResultDTO;
import pt.up.hs.sampling.service.dto.ProtocolDTO;
import pt.up.hs.sampling.service.exceptions.ServiceException;
import pt.up.hs.sampling.service.mapper.ProtocolMapper;
import pt.up.hs.uhc.UniversalHandwritingConverter;
import pt.up.hs.uhc.models.Format;
import pt.up.hs.uhc.models.Stroke;

import java.io.IOException;
import java.util.List;
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
     * @param projectId   ID of the project to which this protocol belongs.
     * @param protocolDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ProtocolDTO save(Long projectId, ProtocolDTO protocolDTO) {
        log.debug("Request to save Protocol {} in project {}", protocolDTO, projectId);
        protocolDTO.setProjectId(projectId);
        Protocol protocol = protocolMapper.toEntity(protocolDTO);
        protocol = protocolRepository.save(protocol);
        return protocolMapper.toDto(protocol);
    }

    /**
     * Get all the protocols.
     *
     * @param projectId ID of the project to which the protocols belong.
     * @param pageable  the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProtocolDTO> findAll(Long projectId, Pageable pageable) {
        log.debug("Request to get all Protocols in project {}", projectId);
        return protocolRepository.findAllByProjectId(projectId, pageable)
            .map(protocolMapper::toDto);
    }

    /**
     * Get the "id" protocol.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param id        the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProtocolDTO> findOne(Long projectId, Long id) {
        log.debug("Request to get Protocol {} in project {}", id, projectId);
        return protocolRepository.findByProjectIdAndId(projectId, id)
            .map(protocolMapper::toDto);
    }

    /**
     * Delete the "id" protocol.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param id        the id of the entity.
     */
    @Override
    public void delete(Long projectId, Long id) {
        log.debug("Request to delete Protocol {} in project {}", id, projectId);
        protocolRepository.deleteAllByProjectIdAndId(projectId, id);
    }

    /**
     * Upload and import protocols.
     *
     * @param projectId   ID of the project to which this protocol belongs.
     * @param type        type of protocol being uploaded.
     * @param file {@link MultipartFile} the file.
     * @return {@link BulkImportResultDTO} response to protocol upload.
     */
    @Override
    public BulkImportResultDTO<ProtocolDTO> importProtocol(
        Long projectId, String type, MultipartFile file
    ) {
        List<pt.up.hs.uhc.models.Page> pages;
        try {
            pages = new UniversalHandwritingConverter()
                .inputFormat(formatFromString(type))
                .file(file.getName(), file.getInputStream())
                .readAll()
                .getPages();
        } catch (IOException e) {
            throw new ServiceException(EntityNames.PROTOCOL, ErrorKeys.ERR_READ_IMPORT, "Could not process imported file.");
        }



        return null;
    }

    /* Helpers */

    /**
     * Save a {@link pt.up.hs.uhc.models.Page} page in
     *
     * @param page {@link pt.up.hs.uhc.models.Page} page to save.
     * @return {@link ProtocolDTO} dto of saved protocol.
     */
    private ProtocolDTO savePage(pt.up.hs.uhc.models.Page page) {

        ProtocolDTO protocolDTO = new ProtocolDTO();

        for (Stroke stroke: page.getStrokes()) {
            // stroke.
        }

        return null;
    }

    /**
     * Get {@link Format} format from {@link String} string.
     *
     * @param str format string.
     * @return {@link Format} format or null
     */
    private Format formatFromString(String str) {
        if (str == null) {
            return null;
        }
        try {
            return Format.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
