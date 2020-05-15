package pt.up.hs.sampling.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
     * Task (from the Project Microservice) to which this text was written.
     */
    @Column(name = "task_id")
    private Long taskId;

    /**
     * Participant (from the Project Microservice) who wrote this text.
     */
    @Column(name = "participant_id")
    private Long participantId;

    /**
     * Typewritten text collected
     */
    @Column(name = "text")
    private String text;

    /**
     * Language of the text.
     */
    @Size(max = 5)
    @Column(name = "language", length = 5)
    private String language;

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

    public Long getTaskId() {
        return taskId;
    }

    public Text taskId(Long taskId) {
        this.taskId = taskId;
        return this;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public Text participantId(Long participantId) {
        this.participantId = participantId;
        return this;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public String getLanguage() {
        return language;
    }

    public Text language(String language) {
        this.language = language;
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
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
            ", taskId=" + getTaskId() +
            ", participantId=" + getParticipantId() +
            ", projectId=" + getProjectId() +
            ", language='" + getLanguage() + "'" +
            "}";
    }
}
