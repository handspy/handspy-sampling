package pt.up.hs.sampling.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Header for handwritten data collected using a smartpen for analysis (part of
 * the sample).
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
     * Number of the page (if the protocol contains multiple pages)
     */
    @Column(name = "page_number")
    private Integer pageNumber;

    /**
     * Task (from the Project Microservice) to which this protocol was written.
     */
    @Column(name = "task_id")
    private Long taskId;

    /**
     * Participant (from the Project Microservice) who wrote this protocol.
     */
    @Column(name = "participant_id")
    private Long participantId;

    /**
     * Language of the protocol.
     */
    @Size(max = 5)
    @Column(name = "language", length = 5)
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

    public Protocol projectId(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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

    public Long getTaskId() {
        return taskId;
    }

    public Protocol taskId(Long taskId) {
        this.taskId = taskId;
        return this;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public Protocol participantId(Long participantId) {
        this.participantId = participantId;
        return this;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public String getLanguage() {
        return language;
    }

    public Protocol language(String language) {
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
            ", pageNumber=" + getPageNumber() +
            ", language='" + getLanguage() + "'" +
            "}";
    }
}
