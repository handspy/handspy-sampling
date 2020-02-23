package pt.up.hs.sampling.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link pt.up.hs.sampling.domain.AnnotationType} entity. This class is used
 * in {@link pt.up.hs.sampling.web.rest.AnnotationTypeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /annotation-types?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AnnotationTypeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter label;

    private StringFilter description;

    private BooleanFilter emotional;

    private DoubleFilter weight;

    private StringFilter color;

    public AnnotationTypeCriteria() {
    }

    public AnnotationTypeCriteria(AnnotationTypeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.label = other.label == null ? null : other.label.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.emotional = other.emotional == null ? null : other.emotional.copy();
        this.weight = other.weight == null ? null : other.weight.copy();
        this.color = other.color == null ? null : other.color.copy();
    }

    @Override
    public AnnotationTypeCriteria copy() {
        return new AnnotationTypeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getLabel() {
        return label;
    }

    public void setLabel(StringFilter label) {
        this.label = label;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BooleanFilter getEmotional() {
        return emotional;
    }

    public void setEmotional(BooleanFilter emotional) {
        this.emotional = emotional;
    }

    public DoubleFilter getWeight() {
        return weight;
    }

    public void setWeight(DoubleFilter weight) {
        this.weight = weight;
    }

    public StringFilter getColor() {
        return color;
    }

    public void setColor(StringFilter color) {
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
        final AnnotationTypeCriteria that = (AnnotationTypeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(label, that.label) &&
            Objects.equals(description, that.description) &&
            Objects.equals(emotional, that.emotional) &&
            Objects.equals(weight, that.weight) &&
            Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        label,
        description,
        emotional,
        weight,
        color
        );
    }

    @Override
    public String toString() {
        return "AnnotationTypeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (label != null ? "label=" + label + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (emotional != null ? "emotional=" + emotional + ", " : "") +
                (weight != null ? "weight=" + weight + ", " : "") +
                (color != null ? "color=" + color + ", " : "") +
            "}";
    }

}
