package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Text} entity.
 */
@ApiModel(description = "Typewritten data collected for analysis (part of the sample). It may be a\ntranscription of the protocol (automatic or manually entered by an analyst)\nor text typed by a participant using a typing device.\n\n@author José Carlos Paiva")
public class TextDTO implements Serializable {

    private Long id;

    /**
     * Typewritten text collected
     */
    @ApiModelProperty(value = "Typewritten text collected")
    private String text;

    /**
     * A text belongs to a sample.
     */
    @ApiModelProperty(value = "A text belongs to a sample.")

    private Long sampleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

        TextDTO textDTO = (TextDTO) o;
        if (textDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), textDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TextDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", sampleId=" + getSampleId() +
            "}";
    }
}
