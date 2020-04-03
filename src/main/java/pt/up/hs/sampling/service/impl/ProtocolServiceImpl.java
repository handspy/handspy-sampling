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
import pt.up.hs.sampling.domain.enumeration.DotType;
import pt.up.hs.sampling.repository.ProtocolRepository;
import pt.up.hs.sampling.service.ProtocolService;
import pt.up.hs.sampling.service.dto.BulkImportResultDTO;
import pt.up.hs.sampling.service.dto.DotDTO;
import pt.up.hs.sampling.service.dto.ProtocolDTO;
import pt.up.hs.sampling.service.dto.StrokeDTO;
import pt.up.hs.sampling.service.exceptions.ServiceException;
import pt.up.hs.sampling.service.mapper.ProtocolMapper;
import pt.up.hs.uhc.UniversalHandwritingConverter;
import pt.up.hs.uhc.models.Format;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
     * Save all protocols.
     *
     * @param projectId    ID of the project to which these protocols belong.
     * @param protocolDTOs the entities to save.
     * @return the persisted entities.
     */
    @Override
    public List<ProtocolDTO> saveAll(Long projectId, List<ProtocolDTO> protocolDTOs) {
        log.debug("Request to save all Protocols in project {}", projectId);
        return protocolRepository
            .saveAll(
                protocolDTOs.parallelStream()
                    .map(protocolDTO -> {
                        Protocol protocol = protocolMapper.toEntity(protocolDTO);
                        protocol.setProjectId(projectId);
                        return protocol;
                    })
                    .collect(Collectors.toList())
            ).parallelStream()
            .map(protocolMapper::toDto)
            .collect(Collectors.toList());
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
        Optional<Protocol> protocol = protocolRepository.findByProjectIdAndId(projectId, id);
        return protocol.map(protocolMapper::toDto);
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
     * @param projectId ID of the project to which this protocol belongs.
     * @param type      type of protocol being uploaded.
     * @param file      {@link MultipartFile} the multipart file.
     * @return {@link List} uploaded protocols.
     */
    @Override
    public List<ProtocolDTO> importProtocol(
        Long projectId, String type, MultipartFile file
    ) {
        List<pt.up.hs.uhc.models.Page> pages;
        try {
            pages = new UniversalHandwritingConverter()
                .inputFormat(formatFromString(type))
                .file(file.getOriginalFilename(), file.getInputStream())
                .readAll()
                .getPages();
        } catch (IOException e) {
            throw new ServiceException(
                EntityNames.PROTOCOL,
                ErrorKeys.ERR_READ_IMPORT,
                "Could not process imported file."
            );
        }

        List<ProtocolDTO> protocolDTOs = pages.parallelStream()
            .map(this::getProtocolDTO)
            .collect(Collectors.toList());

        return saveAll(projectId, protocolDTOs);
    }

    /**
     * Upload and import protocols in bulk.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param type      type of protocols being uploaded.
     * @param files     {@link MultipartFile} the multipart files.
     * @return {@link List} uploaded protocols.
     */
    @Override
    public BulkImportResultDTO<ProtocolDTO> bulkImportProtocols(
        Long projectId, String type, MultipartFile[] files
    ) {
        BulkImportResultDTO<ProtocolDTO> importResult = new BulkImportResultDTO<>();
        importResult.setTotal(files.length);

        long startTime = new Date().getTime();

        List<ProtocolDTO> savedProtocolDTOs = new ArrayList<>();
        int invalid = 0;
        for (MultipartFile file: files) {
            List<ProtocolDTO> protocolDTOs;
            try {
                protocolDTOs = importProtocol(projectId, type, file);
            } catch (Exception e) {
                invalid++;
                continue;
            }
            savedProtocolDTOs.addAll(protocolDTOs);
        }

        importResult.setProcessingTime(new Date().getTime() - startTime);
        importResult.setInvalid(invalid);
        importResult.setData(savedProtocolDTOs);

        return importResult;
    }

    /* Helpers */

    /**
     * Get a protocol DTO from a {@link pt.up.hs.uhc.models.Page} page.
     *
     * @param page {@link pt.up.hs.uhc.models.Page} page to convert.
     * @return {@link ProtocolDTO} protocol dto.
     */
    private ProtocolDTO getProtocolDTO(pt.up.hs.uhc.models.Page page) {

        ProtocolDTO protocolDTO = new ProtocolDTO();

        page.getStrokes().parallelStream()
            .forEachOrdered(stroke -> protocolDTO.getStrokes().add(getStrokeDTO(stroke)));

        return protocolDTO;
    }

    /**
     * Get a stroke DTO from a {@link pt.up.hs.uhc.models.Stroke} stroke.
     *
     * @param stroke {@link pt.up.hs.uhc.models.Stroke} stroke to convert.
     * @return {@link StrokeDTO} stroke dto.
     */
    private StrokeDTO getStrokeDTO(pt.up.hs.uhc.models.Stroke stroke) {

        StrokeDTO strokeDTO = new StrokeDTO();

        strokeDTO.setStartTime(stroke.getStartTime());
        strokeDTO.setEndTime(stroke.getEndTime());

        stroke.getDots().parallelStream()
            .forEachOrdered(dot -> strokeDTO.getDots().add(getDotDTO(dot)));

        return strokeDTO;
    }

    /**
     * Get a dot DTO from a {@link pt.up.hs.uhc.models.Dot} dot.
     *
     * @param dot {@link pt.up.hs.uhc.models.Dot} dot to convert.
     * @return {@link DotDTO} dot dto.
     */
    private DotDTO getDotDTO(pt.up.hs.uhc.models.Dot dot) {
        DotDTO dotDTO = new DotDTO();
        dotDTO.setX(dot.getX());
        dotDTO.setY(dot.getY());
        dotDTO.setTimestamp(dot.getTimestamp());
        dotDTO.setType(DotType.valueOf(dot.getType().toString()));
        dotDTO.setPressure(dot.getPressure());
        return dotDTO;
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
