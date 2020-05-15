package pt.up.hs.sampling.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * Sample of a participant for analysis in a task.
 *
 * @author José Carlos Paiva
 */
@Entity
@Table(name = "sample")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sample extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * ID of the project from the Project Microservice.
     */
    @NotNull
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    /**
     * Task (from the Project Microservice) in which this sample is inserted.
     */
    @NotNull
    @Column(name = "task", nullable = false)
    private Long task;

    /**
     * Participant (from the Project Microservice) to which this sample belongs.
     */
    @NotNull
    @Column(name = "participant", nullable = false)
    private Long participant;

    /**
     * Timestamp of collection of the sample
     */
    @Column(name = "timestamp")
    private Instant timestamp;

    /**
     * Language of the sample
     */
    @Column(name = "language")
    private String language;

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

    public Sample projectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getTask() {
        return task;
    }

    public Sample task(Long task) {
        this.task = task;
        return this;
    }

    public void setTask(Long task) {
        this.task = task;
    }

    public Long getParticipant() {
        return participant;
    }

    public Sample participant(Long participant) {
        this.participant = participant;
        return this;
    }

    public void setParticipant(Long participant) {
        this.participant = participant;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public Sample timestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getLanguage() {
        return language;
    }

    public Sample language(String language) {
        this.language = language;
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sample)) {
            return false;
        }
        return id != null && id.equals(((Sample) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Sample{" +
            "id=" + getId() +
            ", task=" + getTask() +
            ", participant=" + getParticipant() +
            ", timestamp='" + getTimestamp() + "'" +
            ", language='" + getLanguage() + "'" +
            ", projectId=" + getProjectId() +
            "}";
    }
}
