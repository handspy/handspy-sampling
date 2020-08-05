package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pt.up.hs.sampling.domain.pojo.Stroke;

import java.util.*;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.ProtocolData} entity.
 *
 * @author José Carlos Paiva
 */
@ApiModel(description = "Handwritten data collected using a smartpen for ana" +
    "lysis (part of the sample).")
public class ProtocolDataDTO extends AbstractAuditingDTO {

    /**
     * ID of the protocol.
     */
    @ApiModelProperty(value = "ID of the protocol.")
    private Long protocolId;

    /**
     * Width of the layout in which the protocol has been written.
     */
    @ApiModelProperty(value = "Width of the layout in which the protocol has" +
        " been written.")
    private Double width;

    /**
     * Height of the layout in which the protocol has been written.
     */
    @ApiModelProperty(value = "Height of the layout in which the protocol ha" +
        "s been written.")
    private Double height;

    /**
     * Metadata of the capture.
     */
    @ApiModelProperty(value = "Metadata of the capture.")
    private Map<String, Object> metadata = new HashMap<>();

    /**
     * Strokes captured by the smartpen.
     */
    @ApiModelProperty(value = "Strokes captured by the smartpen.")
    private List<Stroke> strokes = new ArrayList<>();


    public Long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public List<Stroke> getStrokes() {
        return strokes;
    }

    public void setStrokes(List<Stroke> strokes) {
        this.strokes = strokes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProtocolDataDTO pdDTO = (ProtocolDataDTO) o;
        if (pdDTO.getProtocolId() == null || getProtocolId() == null) {
            return false;
        }
        return Objects.equals(getProtocolId(), pdDTO.getProtocolId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getProtocolId());
    }

    @Override
    public String toString() {
        return "ProtocolDataDTO{" +
            ", protocolId=" + getProtocolId() +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            "}";
    }
}
