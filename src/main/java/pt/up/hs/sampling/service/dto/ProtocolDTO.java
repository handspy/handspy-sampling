package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Protocol} entity.
 *
 * @author José Carlos Paiva
 */
@ApiModel(description = "Handwritten data collected using a smartpen for ana" +
    "lysis (part of the sample).")
public class ProtocolDTO extends AbstractAuditingDTO {

    private Long id;

    /**
     * ID of the project (from Projects microservice).
     */
    @NotNull
    @ApiModelProperty(value = "ID of the project (from Projects microservice).")
    private Long projectId;

    /**
     * ID of the sample to which this text is linked (if any).
     */
    @ApiModelProperty(value = "ID of the sample to which this text is linked" +
        " (if any).")
    private Long sampleId;

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
     * Number of the page (if the protocol contains multiple pages)
     */
    @ApiModelProperty(value = "Number of the page (if the protocol contains multiple pages)")
    private Integer pageNumber;

    private List<StrokeDTO> strokes = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
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

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<StrokeDTO> getStrokes() {
        return strokes;
    }

    public void setStrokes(List<StrokeDTO> strokes) {
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

        ProtocolDTO protocolDTO = (ProtocolDTO) o;
        if (protocolDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), protocolDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProtocolDTO{" +
            "id=" + getId() +
            ", projectId=" + getProjectId() +
            ", sampleId=" + getSampleId() +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", pageNumber=" + getPageNumber() +
            "}";
    }
}
