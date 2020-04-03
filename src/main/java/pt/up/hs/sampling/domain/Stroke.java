package pt.up.hs.sampling.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import pt.up.hs.sampling.domain.enumeration.DotType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Stroke is a collection of consecutive dots (part of a protocol).
 *
 * @author José Carlos Paiva
 */
@Entity
@Table(name = "stroke")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Stroke implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Offset at which the stroke started.
     */
    @NotNull
    @Column(name = "start_time", nullable = false)
    private Long startTime;

    /**
     * Offset at which the stroke ended.
     */
    @NotNull
    @Column(name = "end_time", nullable = false)
    private Long endTime;

    /**
     * A dot belongs to a protocol.
     */
    @NotNull
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "protocol_id", referencedColumnName = "id"/*, updatable = false*/)
    @JsonIgnoreProperties("strokes")
    private Protocol protocol;

    @OneToMany(
        mappedBy = "stroke",
        cascade = CascadeType.ALL,
        fetch = FetchType.EAGER,
        orphanRemoval = true
    )
    //@Cascade({ org.hibernate.annotations.CascadeType.SAVE_UPDATE })
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnoreProperties("stroke")
    private List<Dot> dots = new ArrayList<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Stroke startTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Stroke endTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public Stroke protocol(Protocol protocol) {
        this.protocol = protocol;
        return this;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public List<Dot> getDots() {
        return dots;
    }

    public Stroke dots(List<Dot> dots) {
        this.dots = dots;
        return this;
    }

    public Stroke addDot(Dot dot) {
        this.dots.add(dot);
        dot.setStroke(this);
        return this;
    }

    public void setDots(List<Dot> dots) {
        this.dots = dots;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stroke)) {
            return false;
        }
        return id != null && id.equals(((Stroke) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Stroke{" +
            "id=" + getId() +
            ", startTime='" + getStartTime() + "'" +
            ", endTime=" + getEndTime() +
            "}";
    }
}
