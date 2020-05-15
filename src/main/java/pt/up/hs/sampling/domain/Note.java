package pt.up.hs.sampling.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


/**
 * Note taken about a protocol/text by an analyst.
 *
 * @author José Carlos Paiva
 */
@Entity
@Table(name = "note")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Note extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Project (from the Project Microservice) to which this note is attached.
     */
    @NotNull
    @Column(name = "project_id", nullable = false)
    private Long projectId;

    /**
     * Task (from the Project Microservice) to which this note is attached.
     */
    @NotNull
    @Column(name = "task_id", nullable = false)
    private Long taskId;

    /**
     * Participant (from the Project Microservice) to which this note is attached.
     */
    @NotNull
    @Column(name = "participant_id", nullable = false)
    private Long participantId;

    /**
     * Text of the note
     */
    @Column(name = "text")
    private String text;

    /**
     * Is this note private
     */
    @Column(name = "self")
    private Boolean self;

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

    public Note projectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public Note taskId(Long taskId) {
        this.taskId = taskId;
        return this;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public Note participantId(Long participantId) {
        this.participantId = participantId;
        return this;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public String getText() {
        return text;
    }

    public Note text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean isSelf() {
        return self;
    }

    public Note self(Boolean self) {
        this.self = self;
        return this;
    }

    public void setSelf(Boolean self) {
        this.self = self;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Note)) {
            return false;
        }
        return id != null && id.equals(((Note) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Note{" +
            "id=" + getId() +
            ", projectId=" + getProjectId() +
            ", taskId=" + getTaskId() +
            ", participantId=" + getParticipantId() +
            ", text='" + getText() + "'" +
            ", self='" + isSelf() + "'" +
            "}";
    }
}
