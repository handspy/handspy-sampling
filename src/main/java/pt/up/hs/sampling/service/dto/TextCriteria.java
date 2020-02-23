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
 * Criteria class for the {@link pt.up.hs.sampling.domain.Text} entity. This class is used
 * in {@link pt.up.hs.sampling.web.rest.TextResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /texts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TextCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter text;

    private LongFilter sampleId;

    public TextCriteria() {
    }

    public TextCriteria(TextCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.text = other.text == null ? null : other.text.copy();
        this.sampleId = other.sampleId == null ? null : other.sampleId.copy();
    }

    @Override
    public TextCriteria copy() {
        return new TextCriteria(this);
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
        final TextCriteria that = (TextCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(text, that.text) &&
            Objects.equals(sampleId, that.sampleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        text,
        sampleId
        );
    }

    @Override
    public String toString() {
        return "TextCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (text != null ? "text=" + text + ", " : "") +
                (sampleId != null ? "sampleId=" + sampleId + ", " : "") +
            "}";
    }

}
