package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.*;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Protocol} entity.
 *
 * @author José Carlos Paiva
 */
@ApiModel(description = "Handwritten data collected using a smartpen for ana" +
    "lysis (part of the sample).")
public class ProtocolDTO implements Serializable {

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
     * Layout in which the protocol has been written
     */
    @ApiModelProperty(value = "Layout in which the protocol has been written")
    private Long layout;

    /**
     * Number of the page (if the protocol contains multiple pages)
     */
    @ApiModelProperty(value = "Number of the page (if the protocol contains multiple pages)")
    private Integer pageNumber;

    private List<DotDTO> dots = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLayout() {
        return layout;
    }

    public void setLayout(Long layout) {
        this.layout = layout;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public List<DotDTO> getDots() {
        return dots;
    }

    public void setDots(List<DotDTO> dots) {
        this.dots = dots;
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
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
            ", layout=" + getLayout() +
            ", pageNumber=" + getPageNumber() +
            "}";
    }
}
