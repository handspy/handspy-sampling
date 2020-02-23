package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Protocol} entity.
 */
@ApiModel(description = "Handwritten data collected using a smartpen for analysis (part of the\nsample).\n\n@author José Carlos Paiva")
public class ProtocolDTO implements Serializable {

    private Long id;

    /**
     * Layout in which the protocol has been written
     */
    @ApiModelProperty(value = "Layout in which the protocol has been written")
    private Long layout;

    /**
     * Device with which the protocol has been written
     */
    @ApiModelProperty(value = "Device with which the protocol has been written")
    private Long device;

    /**
     * Number of the page (if the protocol contains multiple pages)
     */
    @ApiModelProperty(value = "Number of the page (if the protocol contains multiple pages)")
    private Integer pageNumber;

    /**
     * A protocol belongs to a sample.
     */
    @ApiModelProperty(value = "A protocol belongs to a sample.")

    private Long sampleId;

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

    public Long getDevice() {
        return device;
    }

    public void setDevice(Long device) {
        this.device = device;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
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
            ", layout=" + getLayout() +
            ", device=" + getDevice() +
            ", pageNumber=" + getPageNumber() +
            ", sampleId=" + getSampleId() +
            "}";
    }
}
