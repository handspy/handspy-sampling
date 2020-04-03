package pt.up.hs.sampling.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.LongFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link pt.up.hs.sampling.domain.Stroke} entity. This
 * class is used in {@link pt.up.hs.sampling.web.rest.StrokeResource} to
 * receive all the possible filtering options from the Http GET request
 * parameters.
 */
public class StrokeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter startTime;

    private LongFilter endTime;

    public StrokeCriteria() {
    }

    public StrokeCriteria(StrokeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.endTime = other.endTime == null ? null : other.endTime.copy();
    }

    @Override
    public StrokeCriteria copy() {
        return new StrokeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getStartTime() {
        return startTime;
    }

    public void setStartTime(LongFilter startTime) {
        this.startTime = startTime;
    }

    public LongFilter getEndTime() {
        return endTime;
    }

    public void setEndTime(LongFilter endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StrokeCriteria that = (StrokeCriteria) o;
        return
            Objects.equals(id, that.id) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            startTime,
            endTime
        );
    }

    @Override
    public String toString() {
        return "StrokeCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (startTime != null ? "startTime=" + startTime + ", " : "") +
            (endTime != null ? "endTime=" + endTime + ", " : "") +
            "}";
    }
}
