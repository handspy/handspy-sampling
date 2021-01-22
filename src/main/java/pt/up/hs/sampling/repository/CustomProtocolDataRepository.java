package pt.up.hs.sampling.repository;

import pt.up.hs.sampling.domain.ProtocolData;

import javax.annotation.Nonnull;
import java.util.List;

public interface CustomProtocolDataRepository {
    List<ProtocolData> bulkSave(@Nonnull List<ProtocolData> protocolsData);
}
