package pt.up.hs.sampling.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import pt.up.hs.sampling.domain.enumeration.DotType;

/**
 * Dot represents an event of the smartpen (part of a protocol).\n\n@author José Carlos Paiva
 */
@Entity
@Table(name = "dot")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Dot implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Moment at which the dot was captured
     */
    @NotNull
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    /**
     * Position of dot in X axis
     */
    @NotNull
    @Column(name = "x", nullable = false)
    private Integer x;

    /**
     * Position of dot in Y axis
     */
    @NotNull
    @Column(name = "y", nullable = false)
    private Integer y;

    /**
     * Type of dot emitted
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DotType type;

    /**
     * Tilt of the pen in X axis
     */
    @Column(name = "tilt_x")
    private Integer tiltX;

    /**
     * Tilt of the pen in Y axis
     */
    @Column(name = "tilt_y")
    private Integer tiltY;

    /**
     * Twist angle of the pen
     */
    @Column(name = "twist")
    private Integer twist;

    /**
     * Pressure applied to the pen tip
     */
    @Column(name = "pressure")
    private Double pressure;

    /**
     * A dot belongs to a protocol.
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("dots")
    private Protocol protocol;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Dot timestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getX() {
        return x;
    }

    public Dot x(Integer x) {
        this.x = x;
        return this;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public Dot y(Integer y) {
        this.y = y;
        return this;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public DotType getType() {
        return type;
    }

    public Dot type(DotType type) {
        this.type = type;
        return this;
    }

    public void setType(DotType type) {
        this.type = type;
    }

    public Integer getTiltX() {
        return tiltX;
    }

    public Dot tiltX(Integer tiltX) {
        this.tiltX = tiltX;
        return this;
    }

    public void setTiltX(Integer tiltX) {
        this.tiltX = tiltX;
    }

    public Integer getTiltY() {
        return tiltY;
    }

    public Dot tiltY(Integer tiltY) {
        this.tiltY = tiltY;
        return this;
    }

    public void setTiltY(Integer tiltY) {
        this.tiltY = tiltY;
    }

    public Integer getTwist() {
        return twist;
    }

    public Dot twist(Integer twist) {
        this.twist = twist;
        return this;
    }

    public void setTwist(Integer twist) {
        this.twist = twist;
    }

    public Double getPressure() {
        return pressure;
    }

    public Dot pressure(Double pressure) {
        this.pressure = pressure;
        return this;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public Dot protocol(Protocol protocol) {
        this.protocol = protocol;
        return this;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dot)) {
            return false;
        }
        return id != null && id.equals(((Dot) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Dot{" +
            "id=" + getId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", x=" + getX() +
            ", y=" + getY() +
            ", type='" + getType() + "'" +
            ", tiltX=" + getTiltX() +
            ", tiltY=" + getTiltY() +
            ", twist=" + getTwist() +
            ", pressure=" + getPressure() +
            "}";
    }
}
