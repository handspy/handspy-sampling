package pt.up.hs.sampling.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handwritten data collected using a smartpen for analysis (part of the
 * sample).
 *
 * @author José Carlos Paiva
 */
@Entity
@Table(name = "protocol")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Protocol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * ID of the project in the Project Microservice.
     */
    @NotNull
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    /**
     * Layout in which the protocol has been written
     */
    @Column(name = "layout")
    private Long layout;

    /**
     * Number of the page (if the protocol contains multiple pages)
     */
    @Column(name = "page_number")
    private Integer pageNumber;

    @OneToMany(
        mappedBy = "protocol",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<Dot> dots = new ArrayList<>();

    @ManyToOne
    @JsonIgnoreProperties("protocols")
    private Sample sample;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public Protocol projectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getLayout() {
        return layout;
    }

    public Protocol layout(Long layout) {
        this.layout = layout;
        return this;
    }

    public void setLayout(Long layout) {
        this.layout = layout;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Protocol pageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<Dot> getDots() {
        return dots;
    }

    public Protocol dots(List<Dot> dots) {
        this.dots = dots;
        return this;
    }

    public Protocol addDot(Dot dot) {
        this.dots.add(dot);
        dot.setProtocol(this);
        return this;
    }

    public void setDots(List<Dot> dots) {
        this.dots = dots;
    }

    public Sample getSample() {
        return sample;
    }

    public Protocol sample(Sample sample) {
        this.sample = sample;
        return this;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Protocol)) {
            return false;
        }
        return id != null && id.equals(((Protocol) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Protocol{" +
            "id=" + getId() +
            ", projectId=" + getProjectId() +
            ", layout=" + getLayout() +
            ", pageNumber=" + getPageNumber() +
            "}";
    }
}
