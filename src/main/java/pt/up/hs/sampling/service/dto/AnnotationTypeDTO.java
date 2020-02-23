package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.AnnotationType} entity.
 */
@ApiModel(description = "Types of annotations that can be added in a text.\n\n@author José Carlos Paiva")
public class AnnotationTypeDTO implements Serializable {

    private Long id;

    /**
     * Name of this type of annotation
     */
    @NotNull
    @ApiModelProperty(value = "Name of this type of annotation", required = true)
    private String name;

    /**
     * Label of this type of annotation
     */
    @NotNull
    @ApiModelProperty(value = "Label of this type of annotation", required = true)
    private String label;

    /**
     * Details about this type of annotation
     */
    @Size(max = 300)
    @ApiModelProperty(value = "Details about this type of annotation")
    private String description;

    /**
     * Is it an emotional annotation?
     */
    @ApiModelProperty(value = "Is it an emotional annotation?")
    private Boolean emotional;

    /**
     * Weight of annotations of this type (e.g. an emotional annotation of sadness may have a negative weight while an emotional annotation of hapiness may have a positive weight)
     */
    @ApiModelProperty(value = "Weight of annotations of this type (e.g. an emotional annotation of sadness may have a negative weight while an emotional annotation of hapiness may have a positive weight)")
    private Double weight;

    /**
     * Color associated with this type of annotation
     */
    @NotNull
    @Size(max = 20)
    @ApiModelProperty(value = "Color associated with this type of annotation", required = true)
    private String color;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isEmotional() {
        return emotional;
    }

    public void setEmotional(Boolean emotional) {
        this.emotional = emotional;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AnnotationTypeDTO annotationTypeDTO = (AnnotationTypeDTO) o;
        if (annotationTypeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), annotationTypeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AnnotationTypeDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            ", emotional='" + isEmotional() + "'" +
            ", weight=" + getWeight() +
            ", color='" + getColor() + "'" +
            "}";
    }
}
