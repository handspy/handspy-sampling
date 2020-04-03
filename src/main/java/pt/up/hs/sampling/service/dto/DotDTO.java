package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import pt.up.hs.sampling.domain.enumeration.DotType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Dot} entity.
 *
 * @author José Carlos Paiva
 */
@ApiModel(description = "Dot represents an event of the smartpen (part of a " +
    "protocol).")
public class DotDTO implements Serializable {

    private Long id;

    /**
     * A dot belongs to a stroke.
     */
    @NotNull
    @ApiModelProperty(value = "A dot belongs to a stroke.", required = true)
    private Long strokeId;

    /**
     * Offset at which the dot was captured.
     */
    @NotNull
    @ApiModelProperty(value = "Offset at which the dot was captured.", required = true)
    private Long timestamp;

    /**
     * Position of dot in X axis.
     */
    @NotNull
    @ApiModelProperty(value = "Position of dot in X axis.", required = true)
    private Double x;

    /**
     * Position of dot in Y axis.
     */
    @NotNull
    @ApiModelProperty(value = "Position of dot in Y axis.", required = true)
    private Double y;

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

    public Long getStrokeId() {
        return strokeId;
    }

    public void setStrokeId(Long strokeId) {
        this.strokeId = strokeId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
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
            ", strokeId=" + getStrokeId() +
            ", timestamp=" + getTimestamp() +
            ", x=" + getX() +
            ", y=" + getY() +
            ", type='" + getType() + "'" +
            ", pressure=" + getPressure() +
            "}";
    }
}
