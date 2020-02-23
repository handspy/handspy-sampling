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
 * Criteria class for the {@link pt.up.hs.sampling.domain.Note} entity. This class is used
 * in {@link pt.up.hs.sampling.web.rest.NoteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NoteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter text;

    private BooleanFilter self;

    private LongFilter sampleId;

    public NoteCriteria() {
    }

    public NoteCriteria(NoteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.text = other.text == null ? null : other.text.copy();
        this.self = other.self == null ? null : other.self.copy();
        this.sampleId = other.sampleId == null ? null : other.sampleId.copy();
    }

    @Override
    public NoteCriteria copy() {
        return new NoteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getText() {
        return text;
    }

    public void setText(StringFilter text) {
        this.text = text;
    }

    public BooleanFilter getSelf() {
        return self;
    }

    public void setSelf(BooleanFilter self) {
        this.self = self;
    }

    public LongFilter getSampleId() {
        return sampleId;
    }

    public void setSampleId(LongFilter sampleId) {
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
        final NoteCriteria that = (NoteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(text, that.text) &&
            Objects.equals(self, that.self) &&
            Objects.equals(sampleId, that.sampleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        text,
        self,
        sampleId
        );
    }

    @Override
    public String toString() {
        return "NoteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (text != null ? "text=" + text + ", " : "") +
                (self != null ? "self=" + self + ", " : "") +
                (sampleId != null ? "sampleId=" + sampleId + ", " : "") +
            "}";
    }

}
