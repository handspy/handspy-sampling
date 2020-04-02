package pt.up.hs.sampling.service.dto;

import io.github.jhipster.service.Criteria;
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

    private LongFilter layout;

    private IntegerFilter pageNumber;

    private LongFilter sampleId;

    public ProtocolCriteria() {
    }

    public ProtocolCriteria(ProtocolCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.layout = other.layout == null ? null : other.layout.copy();
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

    public LongFilter getLayout() {
        return layout;
    }

    public void setLayout(LongFilter layout) {
        this.layout = layout;
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
            Objects.equals(layout, that.layout) &&
            Objects.equals(pageNumber, that.pageNumber) &&
            Objects.equals(sampleId, that.sampleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        layout,
        pageNumber,
        sampleId
        );
    }

    @Override
    public String toString() {
        return "ProtocolCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (layout != null ? "layout=" + layout + ", " : "") +
                (pageNumber != null ? "pageNumber=" + pageNumber + ", " : "") +
                (sampleId != null ? "sampleId=" + sampleId + ", " : "") +
            "}";
    }
}
