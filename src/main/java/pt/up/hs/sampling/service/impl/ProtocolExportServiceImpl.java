package pt.up.hs.sampling.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.up.hs.sampling.config.ApplicationProperties;
import pt.up.hs.sampling.constants.EntityNames;
import pt.up.hs.sampling.constants.ErrorKeys;
import pt.up.hs.sampling.domain.ProtocolData;
import pt.up.hs.sampling.repository.ProtocolDataRepository;
import pt.up.hs.sampling.service.ProtocolExportService;
import pt.up.hs.sampling.service.dto.ExportFormat;
import pt.up.hs.sampling.service.exceptions.ServiceException;
import pt.up.hs.sampling.service.mapper.UhcPageMapper;
import pt.up.hs.sampling.utils.Files;
import pt.up.hs.uhc.UniversalHandwritingConverter;
import pt.up.hs.uhc.models.Format;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service Implementation for exporting {@link pt.up.hs.sampling.domain.Protocol}.
 */
@Service
@Transactional
public class ProtocolExportServiceImpl implements ProtocolExportService {

    private final Logger log = LoggerFactory.getLogger(ProtocolExportServiceImpl.class);

    private final ApplicationProperties properties;

    private final ProtocolDataRepository protocolDataRepository;

    private final UhcPageMapper uhcPageMapper;


    public ProtocolExportServiceImpl(
        ApplicationProperties properties,
        ProtocolDataRepository protocolDataRepository,
        UhcPageMapper uhcPageMapper
    ) {
        this.properties = properties;
        this.protocolDataRepository = protocolDataRepository;
        this.uhcPageMapper = uhcPageMapper;
    }


    @Override
    public ByteArrayInputStream export(Long projectId, Long protocolId, ExportFormat format) {

        byte[] byteArray = exportToByteArray(projectId, protocolId, format);

        return new ByteArrayInputStream(byteArray);
    }

    private byte[] exportToByteArray(Long projectId, Long protocolId, ExportFormat format) {
        Optional<ProtocolData> optionalPd = protocolDataRepository
            .findByProtocolProjectIdAndProtocolId(projectId, protocolId);
        if (!optionalPd.isPresent()) return new byte[0];

        ProtocolData pd = optionalPd.get();

        UniversalHandwritingConverter uhc = new UniversalHandwritingConverter()
            .page(uhcPageMapper.protocolDataToUhcPage(pd));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        switch (format) {
            case XML:
                uhc.outputFormat(Format.INKML).write(baos);
                break;
            case JSON:
                uhc.outputFormat(Format.HANDSPY).write(baos);
                break;
            case SVG:
                uhc.outputFormat(Format.SVG).write(baos);
                break;
        }
        return baos.toByteArray();
    }

    @Override
    public ByteArrayInputStream bulkExport(Long projectId, Long[] protocolIds, ExportFormat format) {

        Map<String, byte[]> filesMap = new HashMap<>();
        for (Long protocolId: protocolIds) {
            String key = "protocol_" + projectId + "_" + protocolId + "." + format.getExtension();
            filesMap.put(key, exportToByteArray(projectId, protocolId, format));
        }
        try {
            return new ByteArrayInputStream(Files.listBytesToZip(filesMap));
        } catch (IOException e) {
            throw new ServiceException(
                EntityNames.PROTOCOL,
                ErrorKeys.ERR_EXPORT_BULK,
                "Failed to bulk export protocols."
            );
        }
    }
}
