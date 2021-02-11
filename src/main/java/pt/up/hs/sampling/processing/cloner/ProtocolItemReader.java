package pt.up.hs.sampling.processing.cloner;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pt.up.hs.sampling.service.ProtocolService;
import pt.up.hs.sampling.service.dto.ProtocolDTO;

import java.util.Iterator;

import static pt.up.hs.sampling.processing.cloner.ClonerConstants.PROJECT_ID_PARAMETER;

@Component
@StepScope
public class ProtocolItemReader implements ItemReader<Long> {

    private final ProtocolService protocolService;
    private final Long projectId;

    private ItemReader<Long> delegate = null;

    public ProtocolItemReader(
        final ProtocolService protocolService,
        @Value("#{jobParameters[" + PROJECT_ID_PARAMETER + "]}") final Long projectId
    ) {
        this.protocolService = protocolService;
        this.projectId = projectId;
    }

    @Override
    public Long read() throws Exception {
        if (delegate == null) {
            delegate = new IteratorItemReader<>(protocolsIdsIterator());
        }
        return delegate.read();
    }

    public Iterator<Long> protocolsIdsIterator() {
        return protocolService.findAll(projectId)
            .parallelStream()
            .map(ProtocolDTO::getId)
            .iterator();
    }
}
