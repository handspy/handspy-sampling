package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Text} entity.
 *
 * @author José Carlos Paiva
 */
@ApiModel(description = "Envelope wrapping typewritten data collected for an" +
    "alysis (part of the sample). It may be a transcription of the protocol " +
    "(automatic or manually entered by an analyst) or text typed by a partic" +
    "ipant using a typing device.")
public class TextDTO extends AbstractAuditingDTO {

    private Long id;

    /**
     * ID of the project (from Projects microservice).
     */
    @ApiModelProperty(value = "ID of the project (from Projects microservice).")
    private Long projectId;

    /**
     * Task (from the Project Microservice) to which this text was written.
     */
    @ApiModelProperty(value = "Task (from the Project Microservice) to which this text was written.")
    private Long taskId;

    /**
     * Participant (from the Project Microservice) who wrote this text.
     */
    @ApiModelProperty(value = "Participant (from the Project Microservice) who wrote this text.")
    private Long participantId;

    /**
     * Typewritten text collected.
     */
    @ApiModelProperty(value = "Typewritten text collected")
    private String text;

    /**
     * Language of the text.
     */
    @ApiModelProperty(value = "Language of the text.")
    @Size(max = 5)
    private String language;

    private Set<AnnotationDTO> annotations = new HashSet<>();

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Set<AnnotationDTO> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Set<AnnotationDTO> annotations) {
        this.annotations = annotations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TextDTO textDTO = (TextDTO) o;
        if (textDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), textDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TextDTO{" +
            "id=" + getId() +
            ", projectId=" + getProjectId() +
            ", taskId=" + getTaskId() +
            ", participantId=" + getParticipantId() +
            ", language=" + getLanguage() +
            "}";
    }
}
