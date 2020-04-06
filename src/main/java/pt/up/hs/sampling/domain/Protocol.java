package pt.up.hs.sampling.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Handwritten data collected using a smartpen for analysis (part of the
 * sample).
 *
 * @author José Carlos Paiva
 */
@Entity
@Table(name = "protocol")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Protocol extends AbstractAuditingEntity {

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
     * Width of the layout in which the protocol has been written.
     */
    @Column(name = "width", nullable = false)
    private Double width;

    /**
     * Height of the layout in which the protocol has been written.
     */
    @Column(name = "height", nullable = false)
    private Double height;

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
    @JsonIgnoreProperties("protocol")
    private List<Stroke> strokes = new ArrayList<>();

    @ManyToOne
    @JsonIgnoreProperties("protocols")
    private Sample sample;

    @Column(name = "dirty_preview")
    @JsonIgnore
    private boolean dirtyPreview = true;

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

    public Double getWidth() {
        return width;
    }

    public Protocol width(Double width) {
        this.width = width;
        return this;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public Protocol height(Double height) {
        this.height = height;
        return this;
    }

    public void setHeight(Double height) {
        this.height = height;
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

    public List<Stroke> getStrokes() {
        return strokes;
    }

    public Protocol strokes(List<Stroke> strokes) {
        this.strokes = strokes;
        return this;
    }

    public Protocol addStroke(Stroke stroke) {
        this.strokes.add(stroke);
        stroke.setProtocol(this);
        return this;
    }

    public void setStrokes(List<Stroke> strokes) {
        this.strokes = strokes;
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

    public boolean isDirtyPreview() {
        return dirtyPreview;
    }

    public Protocol dirtyPreview(boolean dirtyPreview) {
        this.dirtyPreview = dirtyPreview;
        return this;
    }

    public void setDirtyPreview(boolean dirtyPreview) {
        this.dirtyPreview = dirtyPreview;
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
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", pageNumber=" + getPageNumber() +
            "}";
    }
}
