package pt.up.hs.sampling.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import pt.up.hs.sampling.domain.enumeration.DotType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link pt.up.hs.sampling.domain.Dot} entity. This class is used
 * in {@link pt.up.hs.sampling.web.rest.DotResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /dots?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
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

    private InstantFilter timestamp;

    private IntegerFilter x;

    private IntegerFilter y;

    private DotTypeFilter type;

    private IntegerFilter tiltX;

    private IntegerFilter tiltY;

    private IntegerFilter twist;

    private DoubleFilter pressure;

    private LongFilter protocolId;

    public DotCriteria() {
    }

    public DotCriteria(DotCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.timestamp = other.timestamp == null ? null : other.timestamp.copy();
        this.x = other.x == null ? null : other.x.copy();
        this.y = other.y == null ? null : other.y.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.tiltX = other.tiltX == null ? null : other.tiltX.copy();
        this.tiltY = other.tiltY == null ? null : other.tiltY.copy();
        this.twist = other.twist == null ? null : other.twist.copy();
        this.pressure = other.pressure == null ? null : other.pressure.copy();
        this.protocolId = other.protocolId == null ? null : other.protocolId.copy();
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

    public InstantFilter getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(InstantFilter timestamp) {
        this.timestamp = timestamp;
    }

    public IntegerFilter getX() {
        return x;
    }

    public void setX(IntegerFilter x) {
        this.x = x;
    }

    public IntegerFilter getY() {
        return y;
    }

    public void setY(IntegerFilter y) {
        this.y = y;
    }

    public DotTypeFilter getType() {
        return type;
    }

    public void setType(DotTypeFilter type) {
        this.type = type;
    }

    public IntegerFilter getTiltX() {
        return tiltX;
    }

    public void setTiltX(IntegerFilter tiltX) {
        this.tiltX = tiltX;
    }

    public IntegerFilter getTiltY() {
        return tiltY;
    }

    public void setTiltY(IntegerFilter tiltY) {
        this.tiltY = tiltY;
    }

    public IntegerFilter getTwist() {
        return twist;
    }

    public void setTwist(IntegerFilter twist) {
        this.twist = twist;
    }

    public DoubleFilter getPressure() {
        return pressure;
    }

    public void setPressure(DoubleFilter pressure) {
        this.pressure = pressure;
    }

    public LongFilter getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(LongFilter protocolId) {
        this.protocolId = protocolId;
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
            Objects.equals(tiltX, that.tiltX) &&
            Objects.equals(tiltY, that.tiltY) &&
            Objects.equals(twist, that.twist) &&
            Objects.equals(pressure, that.pressure) &&
            Objects.equals(protocolId, that.protocolId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        timestamp,
        x,
        y,
        type,
        tiltX,
        tiltY,
        twist,
        pressure,
        protocolId
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
                (tiltX != null ? "tiltX=" + tiltX + ", " : "") +
                (tiltY != null ? "tiltY=" + tiltY + ", " : "") +
                (twist != null ? "twist=" + twist + ", " : "") +
                (pressure != null ? "pressure=" + pressure + ", " : "") +
                (protocolId != null ? "protocolId=" + protocolId + ", " : "") +
            "}";
    }

}
