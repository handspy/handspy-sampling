package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;


/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Note} entity.
 *
 * @author José Carlos Paiva
 */
@ApiModel(description = "Note taken about a sample by an analyst.")
public class NoteDTO extends AbstractAuditingDTO {

    private Long id;

    /**
     * ID of the project (from Projects microservice).
     */
    @ApiModelProperty(value = "ID of the project (from Projects microservice).", required = true)
    private Long projectId;

    /**
     * Task (from the Project Microservice) to which this note is attached.
     */
    @NotNull
    @ApiModelProperty(value = "Task (from the Project Microservice) to which this note is attached.", required = true)
    private Long taskId;

    /**
     * Participant (from the Project Microservice) to which this note is attached.
     */
    @NotNull
    @ApiModelProperty(value = "Participant (from the Project Microservice) to which this note is attached.", required = true)
    private Long participantId;

    /**
     * Text of the note.
     */
    @ApiModelProperty(value = "Text of the note.")
    private String text;

    /**
     * Is this note private.
     */
    @ApiModelProperty(value = "Is this note private?")
    private Boolean self;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean isSelf() {
        return self;
    }

    public void setSelf(Boolean self) {
        this.self = self;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NoteDTO noteDTO = (NoteDTO) o;
        if (noteDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), noteDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "NoteDTO{" +
            "id=" + getId() +
            ", projectId=" + getProjectId() +
            ", taskId=" + getTaskId() +
            ", participantId=" + getParticipantId() +
            ", text='" + getText() + "'" +
            ", self='" + isSelf() + "'" +
            "}";
    }
}
