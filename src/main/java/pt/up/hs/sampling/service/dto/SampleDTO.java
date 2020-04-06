package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Sample} entity.
 *
 * @author José Carlos Paiva
 */
@ApiModel(description = "Sample of a participant for analysis in a task.")
public class SampleDTO extends AbstractAuditingDTO {

    private Long id;

    /**
     * ID of the project (from Projects microservice).
     */
    @NotNull
    @ApiModelProperty(value = "ID of the project (from Projects microservice).", required = true)
    private Long projectId;

    /**
     * Task in which this sample is inserted.
     */
    @NotNull
    @ApiModelProperty(value = "Task in which this sample is inserted.", required = true)
    private Long task;

    /**
     * Participant to which this sample belongs.
     */
    @NotNull
    @ApiModelProperty(value = "Participant to which this sample belongs.", required = true)
    private Long participant;

    /**
     * Timestamp of collection of the sample
     */
    @ApiModelProperty(value = "Timestamp of collection of the sample")
    private Instant timestamp;

    /**
     * Language of the sample
     */
    @ApiModelProperty(value = "Language of the sample")
    private String language;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTask() {
        return task;
    }

    public void setTask(Long task) {
        this.task = task;
    }

    public Long getParticipant() {
        return participant;
    }

    public void setParticipant(Long participant) {
        this.participant = participant;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SampleDTO sampleDTO = (SampleDTO) o;
        if (sampleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sampleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SampleDTO{" +
            "id=" + getId() +
            ", projectId=" + getProjectId() +
            ", task=" + getTask() +
            ", participant=" + getParticipant() +
            ", timestamp='" + getTimestamp() + "'" +
            ", language='" + getLanguage() + "'" +
            "}";
    }
}
