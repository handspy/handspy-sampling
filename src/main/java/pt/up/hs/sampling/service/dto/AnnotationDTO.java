package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Annotation} entity.
 *
 * @author José Carlos Paiva
 */
@ApiModel(description = "An annotation added in a text.")
public class AnnotationDTO extends AbstractAuditingDTO {

    private Long id;

    /**
     * An annotation is made in a text.
     */
    @NotNull
    @ApiModelProperty(value = "An annotation is made in a text.", required = true)
    private Long textId;

    /**
     * Start position of the annotation.
     */
    @NotNull
    @ApiModelProperty(value = "Start position of the annotation.", required = true)
    private Integer start;

    /**
     * Size of the annotation.
     */
    @NotNull
    @ApiModelProperty(value = "Size of the annotation.", required = true)
    private Integer size;

    /**
     * Note about annotation.
     */
    @ApiModelProperty(value = "Note about annotation.")
    private String note;

    /**
     * Type of this annotation.
     */
    @NotNull
    @ApiModelProperty(value = "Type of this annotation.", required = true)
    private Long annotationTypeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnnotationTypeId() {
        return annotationTypeId;
    }

    public void setAnnotationTypeId(Long annotationTypeId) {
        this.annotationTypeId = annotationTypeId;
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
            ", start=" + getStart() +
            ", size=" + getSize() +
            ", note='" + getNote() + "'" +
            ", annotationTypeId=" + getAnnotationTypeId() +
            ", textId=" + getTextId() +
            "}";
    }
}
