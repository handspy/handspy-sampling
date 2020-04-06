package pt.up.hs.sampling.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Typewritten data collected for analysis (part of the sample). It may be a
 * transcription of the protocol (automatic or manually entered by an analyst)
 * or text typed by a participant using a typing device.
 *
 * @author José Carlos Paiva
 */
@Entity
@Table(name = "text")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Text extends AbstractAuditingEntity {

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
     * Typewritten text collected
     */
    @Column(name = "text")
    private String text;

    @ManyToOne
    @JsonIgnoreProperties("texts")
    private Sample sample;

    @OneToMany(
        mappedBy = "text",
        cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Annotation> annotations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public Text text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getProjectId() {
        return projectId;
    }

    public Text projectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Sample getSample() {
        return sample;
    }

    public Text sample(Sample sample) {
        this.sample = sample;
        return this;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }

    public Set<Annotation> getAnnotations() {
        return annotations;
    }

    public Text annotations(Set<Annotation> annotations) {
        this.annotations = annotations;
        return this;
    }

    public Text addAnnotation(Annotation annotation) {
        this.annotations.add(annotation);
        annotation.setText(this);
        return this;
    }

    public void setAnnotations(Set<Annotation> annotations) {
        this.annotations = annotations;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Text)) {
            return false;
        }
        return id != null && id.equals(((Text) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Text{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", projectId=" + getProjectId() +
            "}";
    }
}
