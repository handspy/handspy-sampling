package pt.up.hs.sampling.processing.cloner;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pt.up.hs.sampling.service.ProtocolService;
import pt.up.hs.sampling.service.TextService;
import pt.up.hs.sampling.service.dto.ProtocolDTO;
import pt.up.hs.sampling.service.dto.TextDTO;

import java.util.Iterator;

import static pt.up.hs.sampling.processing.cloner.ClonerConstants.PROJECT_ID_PARAMETER;

@Component
@StepScope
public class TextItemReader implements ItemReader<Long> {

    private final TextService textService;
    private final Long projectId;

    private ItemReader<Long> delegate = null;

    public TextItemReader(
        final TextService textService,
        @Value("#{jobParameters[" + PROJECT_ID_PARAMETER + "]}") final Long projectId
    ) {
        this.textService = textService;
        this.projectId = projectId;
    }

    @Override
    public Long read() throws Exception {
        if (delegate == null) {
            delegate = new IteratorItemReader<>(textsIdsIterator());
        }
        return delegate.read();
    }

    public Iterator<Long> textsIdsIterator() {
        return textService.findAll(projectId)
            .parallelStream()
            .map(TextDTO::getId)
            .iterator();
    }
}
