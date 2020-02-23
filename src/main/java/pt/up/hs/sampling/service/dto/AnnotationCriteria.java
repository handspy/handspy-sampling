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
 * Criteria class for the {@link pt.up.hs.sampling.domain.Annotation} entity. This class is used
 * in {@link pt.up.hs.sampling.web.rest.AnnotationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /annotations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AnnotationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter type;

    private IntegerFilter start;

    private IntegerFilter size;

    private StringFilter note;

    private LongFilter textId;

    public AnnotationCriteria() {
    }

    public AnnotationCriteria(AnnotationCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.start = other.start == null ? null : other.start.copy();
        this.size = other.size == null ? null : other.size.copy();
        this.note = other.note == null ? null : other.note.copy();
        this.textId = other.textId == null ? null : other.textId.copy();
    }

    @Override
    public AnnotationCriteria copy() {
        return new AnnotationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getType() {
        return type;
    }

    public void setType(LongFilter type) {
        this.type = type;
    }

    public IntegerFilter getStart() {
        return start;
    }

    public void setStart(IntegerFilter start) {
        this.start = start;
    }

    public IntegerFilter getSize() {
        return size;
    }

    public void setSize(IntegerFilter size) {
        this.size = size;
    }

    public StringFilter getNote() {
        return note;
    }

    public void setNote(StringFilter note) {
        this.note = note;
    }

    public LongFilter getTextId() {
        return textId;
    }

    public void setTextId(LongFilter textId) {
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
        final AnnotationCriteria that = (AnnotationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(start, that.start) &&
            Objects.equals(size, that.size) &&
            Objects.equals(note, that.note) &&
            Objects.equals(textId, that.textId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        type,
        start,
        size,
        note,
        textId
        );
    }

    @Override
    public String toString() {
        return "AnnotationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (start != null ? "start=" + start + ", " : "") +
                (size != null ? "size=" + size + ", " : "") +
                (note != null ? "note=" + note + ", " : "") +
                (textId != null ? "textId=" + textId + ", " : "") +
            "}";
    }

}
