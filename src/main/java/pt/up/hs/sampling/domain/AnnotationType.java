package pt.up.hs.sampling.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Types of annotations that can be added in a text.\n\n@author José Carlos Paiva
 */
@Entity
@Table(name = "annotation_type")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AnnotationType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Name of this type of annotation
     */
    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Label of this type of annotation
     */
    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    /**
     * Details about this type of annotation
     */
    @Size(max = 300)
    @Column(name = "description", length = 300)
    private String description;

    /**
     * Is it an emotional annotation?
     */
    @Column(name = "emotional")
    private Boolean emotional;

    /**
     * Weight of annotations of this type (e.g. an emotional annotation of sadness may have a negative weight while an emotional annotation of hapiness may have a positive weight)
     */
    @Column(name = "weight")
    private Double weight;

    /**
     * Color associated with this type of annotation
     */
    @NotNull
    @Size(max = 20)
    @Column(name = "color", length = 20, nullable = false)
    private String color;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public AnnotationType name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public AnnotationType label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public AnnotationType description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isEmotional() {
        return emotional;
    }

    public AnnotationType emotional(Boolean emotional) {
        this.emotional = emotional;
        return this;
    }

    public void setEmotional(Boolean emotional) {
        this.emotional = emotional;
    }

    public Double getWeight() {
        return weight;
    }

    public AnnotationType weight(Double weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getColor() {
        return color;
    }

    public AnnotationType color(String color) {
        this.color = color;
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnnotationType)) {
            return false;
        }
        return id != null && id.equals(((AnnotationType) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AnnotationType{" +
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
