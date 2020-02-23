package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Annotation} entity.
 */
@ApiModel(description = "An annotation added in a text.\n\n@author José Carlos Paiva")
public class AnnotationDTO implements Serializable {

    private Long id;

    /**
     * Type of this annotation
     */
    @NotNull
    @ApiModelProperty(value = "Type of this annotation", required = true)
    private Long type;

    /**
     * Start position of the annotation
     */
    @NotNull
    @ApiModelProperty(value = "Start position of the annotation", required = true)
    private Integer start;

    /**
     * Size of the annotation
     */
    @NotNull
    @ApiModelProperty(value = "Size of the annotation", required = true)
    private Integer size;

    /**
     * Note about annotation
     */
    @ApiModelProperty(value = "Note about annotation")
    private String note;

    /**
     * An annotation is made in a text.
     */
    @ApiModelProperty(value = "An annotation is made in a text.")

    private Long textId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Long getTextId() {
        return textId;
    }

    public void setTextId(Long textId) {
        this.textId = textId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AnnotationDTO annotationDTO = (AnnotationDTO) o;
        if (annotationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), annotationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AnnotationDTO{" +
            "id=" + getId() +
            ", type=" + getType() +
            ", start=" + getStart() +
            ", size=" + getSize() +
            ", note='" + getNote() + "'" +
            ", textId=" + getTextId() +
            "}";
    }
}
