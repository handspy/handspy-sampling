package pt.up.hs.sampling.processing.preview;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import pt.up.hs.sampling.domain.Protocol;
import pt.up.hs.sampling.domain.ProtocolData;
import pt.up.hs.sampling.service.dto.ProtocolDTO;
import pt.up.hs.sampling.service.dto.ProtocolDataDTO;
import pt.up.hs.sampling.service.mapper.UhcPageMapper;
import pt.up.hs.uhc.models.Page;

import javax.annotation.Nonnull;

/**
 * Generate preview for a single {@link Protocol}.
 *
 * @author José Carlos Paiva
 */
@Component
@StepScope
public class ProtocolPreviewGenerationProcessor implements ItemProcessor<ProtocolData, Page> {

    private final UhcPageMapper uhcPageMapper;

    public ProtocolPreviewGenerationProcessor(UhcPageMapper uhcPageMapper) {
        this.uhcPageMapper = uhcPageMapper;
    }

    @Override
    public Page process(@Nonnull ProtocolData pd) throws Exception {
        return uhcPageMapper.protocolDataToUhcPage(pd);
    }
}
