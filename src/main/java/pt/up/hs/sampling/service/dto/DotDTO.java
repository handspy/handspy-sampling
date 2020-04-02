package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import pt.up.hs.sampling.domain.enumeration.DotType;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Dot} entity.
 *
 * @author José Carlos Paiva
 */
@ApiModel(description = "Dot represents an event of the smartpen (part of a protocol).")
public class DotDTO implements Serializable {

    private Long id;

    /**
     * A dot belongs to a protocol.
     */
    @NotNull
    @ApiModelProperty(value = "A dot belongs to a protocol.", required = true)
    private Long protocolId;

    /**
     * Moment at which the dot was captured.
     */
    @NotNull
    @ApiModelProperty(value = "Moment at which the dot was captured.", required = true)
    private Instant timestamp;

    /**
     * Position of dot in X axis.
     */
    @NotNull
    @ApiModelProperty(value = "Position of dot in X axis.", required = true)
    private Integer x;

    /**
     * Position of dot in Y axis.
     */
    @NotNull
    @ApiModelProperty(value = "Position of dot in Y axis.", required = true)
    private Integer y;

    /**
     * Type of dot emitted.
     */
    @ApiModelProperty(value = "Type of dot emitted.")
    private DotType type;

    /**
     * Pressure applied to the pen tip.
     */
    @ApiModelProperty(value = "Pressure applied to the pen tip.")
    private Double pressure;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public DotType getType() {
        return type;
    }

    public void setType(DotType type) {
        this.type = type;
    }

    public Double getPressure() {
        return pressure;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DotDTO dotDTO = (DotDTO) o;
        if (dotDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dotDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DotDTO{" +
            "id=" + getId() +
            ", protocolId=" + getProtocolId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", x=" + getX() +
            ", y=" + getY() +
            ", type='" + getType() + "'" +
            ", pressure=" + getPressure() +
            "}";
    }
}
