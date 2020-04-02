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
 * Dot represents an event of the smartpen (part of a protocol).
 *
 * @author José Carlos Paiva
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
     * Pressure applied to the pen tip
     */
    @Column(name = "pressure")
    private Double pressure;

    /**
     * A dot belongs to a protocol.
     */
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "protocol_id", updatable = false)
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
            ", pressure=" + getPressure() +
            "}";
    }
}
