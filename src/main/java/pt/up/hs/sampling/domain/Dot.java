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
     * Offset at which the dot was captured.
     */
    @NotNull
    @Column(name = "timestamp", nullable = false)
    private Long timestamp;

    /**
     * Position of dot in X axis
     */
    @NotNull
    @Column(name = "x", nullable = false)
    private Double x;

    /**
     * Position of dot in Y axis
     */
    @NotNull
    @Column(name = "y", nullable = false)
    private Double y;

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
     * A dot belongs to a stroke.
     */
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "stroke_id", referencedColumnName = "id"/*, updatable = false*/)
    @JsonIgnoreProperties("dots")
    private Stroke stroke;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Dot timestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Double getX() {
        return x;
    }

    public Dot x(Double x) {
        this.x = x;
        return this;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public Dot y(Double y) {
        this.y = y;
        return this;
    }

    public void setY(Double y) {
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

    public Stroke getStroke() {
        return stroke;
    }

    public Dot stroke(Stroke stroke) {
        this.stroke = stroke;
        return this;
    }

    public void setStroke(Stroke stroke) {
        this.stroke = stroke;
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
