package pt.up.hs.sampling.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import pt.up.hs.sampling.domain.Protocol;
import pt.up.hs.sampling.domain.ProtocolData;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

public class CustomProtocolDataRepositoryImpl implements CustomProtocolDataRepository {

    private final Logger log = LoggerFactory.getLogger(CustomProtocolDataRepositoryImpl.class);

    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private final int batchSize = 25;

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ProtocolData> bulkSave(@Nonnull List<ProtocolData> protocolsData) {
        List<ProtocolData> saved = new ArrayList<>();
        for (int i = 1; i <= protocolsData.size(); i++) {
            ProtocolData protocolData = protocolsData.get(i - 1);
            if (protocolData.getProtocol() != null) {
                Protocol protocol = em.merge(protocolData.getProtocol());
                protocolData.setProtocol(protocol);
            }
            em.persist(protocolData);
            if (i % batchSize == 0) {
                em.flush();
                em.clear();
            }
            saved.add(protocolData);
            log.error(String.valueOf(i));
        }
        em.flush();
        em.clear();
        return saved;
    }
}
