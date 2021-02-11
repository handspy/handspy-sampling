package pt.up.hs.sampling.processing.importing;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import pt.up.hs.sampling.domain.ProtocolData;
import pt.up.hs.sampling.repository.ProtocolDataRepository;
import pt.up.hs.sampling.service.mapper.UhcPageMapper;
import pt.up.hs.uhc.models.Page;

import javax.annotation.Nonnull;

/**
 * Import protocol page into database.
 */
@Component
@StepScope
public class ImportProtocolProcessor implements ItemProcessor<Page, ProtocolData> {

    private final ProtocolDataRepository protocolDataRepository;
    private final UhcPageMapper uhcPageMapper;

    public ImportProtocolProcessor(
        ProtocolDataRepository protocolDataRepository,
        UhcPageMapper uhcPageMapper
    ) {
        this.protocolDataRepository = protocolDataRepository;
        this.uhcPageMapper = uhcPageMapper;
    }

    @Override
    public ProtocolData process(@Nonnull Page page) {
        ProtocolData protocolData = uhcPageMapper.uhcPageToProtocolData(page);
        ProtocolData saved = protocolDataRepository.save(protocolData);
        protocolDataRepository.flush();
        return saved;
    }
}
