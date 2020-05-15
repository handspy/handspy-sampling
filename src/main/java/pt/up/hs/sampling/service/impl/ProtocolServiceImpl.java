package pt.up.hs.sampling.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.zalando.problem.Status;
import pt.up.hs.sampling.config.ApplicationProperties;
import pt.up.hs.sampling.constants.EntityNames;
import pt.up.hs.sampling.constants.ErrorKeys;
import pt.up.hs.sampling.domain.Protocol;
import pt.up.hs.sampling.domain.ProtocolData;
import pt.up.hs.sampling.processing.preview.BatchProtocolPreviewGenerationJobLauncher;
import pt.up.hs.sampling.repository.ProtocolDataRepository;
import pt.up.hs.sampling.repository.ProtocolRepository;
import pt.up.hs.sampling.service.ProtocolService;
import pt.up.hs.sampling.service.dto.BulkImportResultDTO;
import pt.up.hs.sampling.service.dto.ProtocolDTO;
import pt.up.hs.sampling.service.dto.ProtocolDataDTO;
import pt.up.hs.sampling.service.exceptions.ServiceException;
import pt.up.hs.sampling.service.mapper.ProtocolDataMapper;
import pt.up.hs.sampling.service.mapper.ProtocolMapper;
import pt.up.hs.sampling.service.mapper.UhcPageMapper;
import pt.up.hs.uhc.UniversalHandwritingConverter;
import pt.up.hs.uhc.models.Format;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private final ApplicationProperties properties;

    private final ProtocolRepository protocolRepository;
    private final ProtocolMapper protocolMapper;

    private final ProtocolDataRepository protocolDataRepository;
    private final ProtocolDataMapper protocolDataMapper;

    private final UhcPageMapper uhcPageMapper;

    private final BatchProtocolPreviewGenerationJobLauncher previewGenerationJobLauncher;

    public ProtocolServiceImpl(
        ApplicationProperties properties,
        ProtocolRepository protocolRepository,
        ProtocolMapper protocolMapper,
        ProtocolDataRepository protocolDataRepository,
        ProtocolDataMapper protocolDataMapper,
        UhcPageMapper uhcPageMapper,
        BatchProtocolPreviewGenerationJobLauncher previewGenerationJobLauncher
    ) {
        this.properties = properties;
        this.protocolRepository = protocolRepository;
        this.protocolMapper = protocolMapper;
        this.protocolDataRepository = protocolDataRepository;
        this.protocolDataMapper = protocolDataMapper;
        this.uhcPageMapper = uhcPageMapper;
        this.previewGenerationJobLauncher = previewGenerationJobLauncher;
    }

    /**
     * Save a protocol.
     *
     * @param projectId   ID of the project to which this protocol belongs.
     * @param protocolDTO the entity to save.
     * @return the persisted entity.
     */
    public ProtocolDTO save(Long projectId, ProtocolDTO protocolDTO) {
        log.debug("Request to save Protocol {} in project {}", protocolDTO, projectId);
        protocolDTO.setProjectId(projectId);
        Protocol protocol = protocolMapper.toEntity(protocolDTO);
        protocol = protocolRepository.save(protocol);
        return protocolMapper.toDto(protocol);
    }

    /**
     * Save a protocol data.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param pdDTO     the data to save.
     */
    @Override
    public ProtocolDTO saveData(Long projectId, ProtocolDataDTO pdDTO) {
        log.debug("Request to save Protocol {} data in project {}", pdDTO, projectId);
        Protocol protocol;
        if (pdDTO.getProtocolId() != null) {
            Optional<Protocol> optProtocol = protocolRepository
                .findByProjectIdAndId(projectId, pdDTO.getProtocolId());
            if (!optProtocol.isPresent()) {
                throw new ServiceException(
                    Status.NOT_FOUND, EntityNames.PROTOCOL, ErrorKeys.ERR_NOT_FOUND,
                    "Protocol not found"
                );
            }
            protocol = optProtocol.get();
        } else {
            protocol = new Protocol().projectId(projectId);
        }
        ProtocolData pd = protocolDataMapper.toEntity(pdDTO);
        pd.setProtocol(protocol);
        pd = protocolDataRepository.save(pd);
        previewGenerationJobLauncher.newExecution();
        return protocolMapper.toDto(pd.getProtocol());
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
     * Get the "id" protocol's data.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param id        the id of the entity.
     * @return the entity data.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProtocolDataDTO> findOneData(Long projectId, Long id) {
        log.debug("Request to get Protocol {}'s data in project {}", id, projectId);
        Optional<ProtocolData> pd = protocolDataRepository
            .findByProtocolProjectIdAndProtocolId(projectId, id);
        return pd.map(protocolDataMapper::toDto);
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
        if (protocolDataRepository.existsById(id)) {
            protocolDataRepository.deleteById(id);
        }
        protocolRepository.deleteByProjectIdAndId(projectId, id);
        try {
            Files.deleteIfExists(Paths.get(
                properties.getPreview().getPath(),
                projectId.toString(),
                id.toString() + ".png"
            ));
        } catch (IOException e) {
            log.error("Failed to delete protocol preview", e);
            // ignore errors
        }
    }

    /**
     * Upload and import protocols in bulk.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param type      type of protocols being uploaded.
     * @param files     {@link MultipartFile} the multipart files.
     * @return {@link BulkImportResultDTO} upload summary.
     */
    @Override
    public BulkImportResultDTO<ProtocolDTO> bulkImportProtocols(
        Long projectId, String type, MultipartFile[] files
    ) {
        log.debug("Request to bulk import Protocols in project {}", projectId);
        BulkImportResultDTO<ProtocolDTO> importResult = new BulkImportResultDTO<>();
        importResult.setTotal(files.length);

        long startTime = new Date().getTime();

        List<ProtocolDTO> savedProtocolDTOs = new ArrayList<>();
        int invalid = 0;
        for (MultipartFile file : files) {
            List<ProtocolDTO> protocolDTOs;
            try {
                protocolDTOs = importProtocol(projectId, type, file);
            } catch (Exception e) {
                invalid++;
                continue;
            }
            savedProtocolDTOs.addAll(protocolDTOs);
        }

        previewGenerationJobLauncher.newExecution();

        importResult.setProcessingTime(new Date().getTime() - startTime);
        importResult.setInvalid(invalid);
        importResult.setData(savedProtocolDTOs);

        return importResult;
    }

    @Override
    public Optional<byte[]> getPreview(Long projectId, Long id) {
        log.debug("Request to get preview for Protocol {} in project {}", id, projectId);

        try {
            Path previewPath = Paths.get(
                properties.getPreview().getPath(),
                projectId.toString(),
                id.toString() + ".png"
            );
            if (!Files.exists(previewPath)) {
                return Optional.empty();
            }
            return Optional.of(Files.readAllBytes(previewPath));
        } catch (IOException e) {
            throw new ServiceException(
                EntityNames.PROTOCOL,
                ErrorKeys.ERR_READING_PREVIEW,
                "Failed to read protocol preview."
            );
        }
    }

    /* Helpers */

    /**
     * Upload and import protocols.
     *
     * @param projectId ID of the project to which this protocol belongs.
     * @param type      type of protocol being uploaded.
     * @param file      {@link MultipartFile} the multipart file.
     * @return {@link List} uploaded protocols.
     */
    private List<ProtocolDTO> importProtocol(
        Long projectId, String type, MultipartFile file
    ) {

        List<pt.up.hs.uhc.models.Page> pages;
        try {
            String filename;
            if (file.getOriginalFilename() == null) {
                filename = file.getName();
            } else {
                filename = file.getOriginalFilename();
            }
            pages = new UniversalHandwritingConverter()
                .inputFormat(formatFromString(type))
                .file(filename, file.getInputStream())
                .normalize(true, 3)
                .center()
                .getPages();
        } catch (IOException e) {
            throw new ServiceException(
                EntityNames.PROTOCOL,
                ErrorKeys.ERR_READ_IMPORT,
                "Could not process imported file."
            );
        }

        List<ProtocolData> pds = pages.parallelStream()
            .map(uhcPageMapper::uhcPageToProtocolData)
            .peek(pd -> pd.setProtocol(new Protocol().projectId(projectId)))
            .collect(Collectors.toList());

       pds = protocolDataRepository.saveAll(pds);

        return pds.parallelStream()
            .map(pd -> protocolMapper.toDto(pd.getProtocol()))
            .collect(Collectors.toList());
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
