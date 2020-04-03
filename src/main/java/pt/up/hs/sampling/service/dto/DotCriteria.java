package pt.up.hs.sampling.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.*;
import pt.up.hs.sampling.domain.enumeration.DotType;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link pt.up.hs.sampling.domain.Dot} entity. This
 * class is used in {@link pt.up.hs.sampling.web.rest.DotResource} to receive
 * all the possible filtering options from the Http GET request parameters.
 */
public class DotCriteria implements Serializable, Criteria {
    /**
     * Class for filtering DotType
     */
    public static class DotTypeFilter extends Filter<DotType> {

        public DotTypeFilter() {
        }

        public DotTypeFilter(DotTypeFilter filter) {
            super(filter);
        }

        @Override
        public DotTypeFilter copy() {
            return new DotTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter timestamp;

    private DoubleFilter x;

    private DoubleFilter y;

    private DotTypeFilter type;

    private DoubleFilter pressure;

    public DotCriteria() {
    }

    public DotCriteria(DotCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.timestamp = other.timestamp == null ? null : other.timestamp.copy();
        this.x = other.x == null ? null : other.x.copy();
        this.y = other.y == null ? null : other.y.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.pressure = other.pressure == null ? null : other.pressure.copy();
    }

    @Override
    public DotCriteria copy() {
        return new DotCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LongFilter timestamp) {
        this.timestamp = timestamp;
    }

    public DoubleFilter getX() {
        return x;
    }

    public void setX(DoubleFilter x) {
        this.x = x;
    }

    public DoubleFilter getY() {
        return y;
    }

    public void setY(DoubleFilter y) {
        this.y = y;
    }

    public DotTypeFilter getType() {
        return type;
    }

    public void setType(DotTypeFilter type) {
        this.type = type;
    }

    public DoubleFilter getPressure() {
        return pressure;
    }

    public void setPressure(DoubleFilter pressure) {
        this.pressure = pressure;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DotCriteria that = (DotCriteria) o;
        return
            Objects.equals(id, that.id) &&
                Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(x, that.x) &&
                Objects.equals(y, that.y) &&
                Objects.equals(type, that.type) &&
                Objects.equals(pressure, that.pressure);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            timestamp,
            x,
            y,
            type,
            pressure
        );
    }

    @Override
    public String toString() {
        return "DotCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (timestamp != null ? "timestamp=" + timestamp + ", " : "") +
            (x != null ? "x=" + x + ", " : "") +
            (y != null ? "y=" + y + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (pressure != null ? "pressure=" + pressure + ", " : "") +
            "}";
    }

}
