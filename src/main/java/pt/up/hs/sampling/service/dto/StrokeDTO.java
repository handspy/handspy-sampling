package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Stroke} entity.
 *
 * @author José Carlos Paiva
 */
@ApiModel(description = "Stroke is a collection of consecutive dots (part of" +
    " a protocol).")
public class StrokeDTO implements Serializable {

    private Long id;

    /**
     * A stroke belongs to a protocol.
     */
    @NotNull
    @ApiModelProperty(value = "A stroke belongs to a protocol.", required = true)
    private Long protocolId;

    /**
     * Offset at which the stroke started.
     */
    @NotNull
    @ApiModelProperty(value = "Offset at which the stroke started.", required = true)
    private Long startTime;

    /**
     * Offset at which the stroke ended.
     */
    @NotNull
    @ApiModelProperty(value = "Offset at which the stroke ended.", required = true)
    private Long endTime;

    private List<DotDTO> dots = new ArrayList<>();


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

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public List<DotDTO> getDots() {
        return dots;
    }

    public void setDots(List<DotDTO> dots) {
        this.dots = dots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        StrokeDTO strokeDTO = (StrokeDTO) o;
        if (strokeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), strokeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StrokeDTO{" +
            "id=" + getId() +
            ", protocolId=" + getProtocolId() +
            ", startTime=" + getStartTime() +
            ", endTime=" + getEndTime() +
            "}";
    }
}
