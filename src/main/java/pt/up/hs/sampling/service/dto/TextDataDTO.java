package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Text} entity.
 *
 * @author José Carlos Paiva
 */
@ApiModel(description = "Typewritten data collected for analysis (part of th" +
    "e sample). It may be a transcription of the protocol (automatic or manu" +
    "ally entered by an analyst) or text typed by a participant using a typi" +
    "ng device.")
public class TextDataDTO extends AbstractAuditingDTO {

    /**
     * Text ID.
     */
    @ApiModelProperty(value = "Text ID (envelope).")
    private Long textId;

    /**
     * Typewritten text collected.
     */
    @ApiModelProperty(value = "Typewritten text collected")
    private String data;

    public Long getTextId() {
        return textId;
    }

    public void setTextId(Long textId) {
        this.textId = textId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TextDataDTO textDTO = (TextDataDTO) o;
        if (textDTO.getTextId() == null || getTextId() == null) {
            return false;
        }
        return Objects.equals(getTextId(), textDTO.getTextId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getTextId());
    }

    @Override
    public String toString() {
        return "TextDataDTO{" +
            ", textId=" + getTextId() +
            ", data='" + getData() + "'" +
            "}";
    }
}
