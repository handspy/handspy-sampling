package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Note} entity.
 */
@ApiModel(description = "Note taken about a sample by an analyst.\n\n@author José Carlos Paiva")
public class NoteDTO implements Serializable {

    private Long id;

    /**
     * Text of the note
     */
    @ApiModelProperty(value = "Text of the note")
    private String text;

    /**
     * Is this note private
     */
    @ApiModelProperty(value = "Is this note private")
    private Boolean self;

    /**
     * A note belongs to a sample.
     */
    @ApiModelProperty(value = "A note belongs to a sample.")

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

    public Boolean isSelf() {
        return self;
    }

    public void setSelf(Boolean self) {
        this.self = self;
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

        NoteDTO noteDTO = (NoteDTO) o;
        if (noteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), noteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NoteDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", self='" + isSelf() + "'" +
            ", sampleId=" + getSampleId() +
            "}";
    }
}
