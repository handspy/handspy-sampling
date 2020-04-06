package pt.up.hs.sampling.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link pt.up.hs.sampling.domain.Protocol} entity. This class is used
 * in {@link pt.up.hs.sampling.web.rest.ProtocolResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /protocols?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProtocolCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter width;

    private DoubleFilter height;

    private IntegerFilter pageNumber;

    private LongFilter sampleId;

    public ProtocolCriteria() {
    }

    public ProtocolCriteria(ProtocolCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.width = other.width == null ? null : other.width.copy();
        this.height = other.height == null ? null : other.height.copy();
        this.pageNumber = other.pageNumber == null ? null : other.pageNumber.copy();
        this.sampleId = other.sampleId == null ? null : other.sampleId.copy();
    }

    @Override
    public ProtocolCriteria copy() {
        return new ProtocolCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getWidth() {
        return width;
    }

    public void setWidth(DoubleFilter width) {
        this.width = width;
    }

    public DoubleFilter getHeight() {
        return height;
    }

    public void setHeight(DoubleFilter height) {
        this.height = height;
    }

    public IntegerFilter getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(IntegerFilter pageNumber) {
        this.pageNumber = pageNumber;
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
        final ProtocolCriteria that = (ProtocolCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(width, that.width) &&
            Objects.equals(height, that.height) &&
            Objects.equals(pageNumber, that.pageNumber) &&
            Objects.equals(sampleId, that.sampleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            width,
            height,
            pageNumber,
            sampleId
        );
    }

    @Override
    public String toString() {
        return "ProtocolCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (width != null ? "width=" + width + ", " : "") +
                (height != null ? "height=" + height + ", " : "") +
                (pageNumber != null ? "pageNumber=" + pageNumber + ", " : "") +
                (sampleId != null ? "sampleId=" + sampleId + ", " : "") +
            "}";
    }
}
