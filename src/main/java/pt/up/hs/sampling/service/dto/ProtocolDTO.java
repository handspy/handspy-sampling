package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Protocol} entity.
 *
 * @author José Carlos Paiva
 */
@ApiModel(description = "Envelope wrapping handwritten data collected using " +
    "a smartpen for analysis (part of the sample).")
public class ProtocolDTO extends AbstractAuditingDTO {

    private Long id;

    /**
     * ID of the project (from Projects microservice).
     */
    @ApiModelProperty(value = "ID of the project (from Projects microservice).")
    private Long projectId;

    /**
     * Task (from the Project Microservice) to which this protocol was written.
     */
    @ApiModelProperty(value = "Task (from the Project Microservice) to which this protocol was written.")
    private Long taskId;

    /**
     * Participant (from the Project Microservice) who wrote this protocol.
     */
    @ApiModelProperty(value = "Participant (from the Project Microservice) who wrote this protocol.")
    private Long participantId;

    /**
     * Number of the page (if the protocol contains multiple pages)
     */
    @ApiModelProperty(value = "Number of the page (if the protocol contains multiple pages).")
    private Integer pageNumber;

    /**
     * Language of the protocol.
     */
    @ApiModelProperty(value = "Language of the protocol.")
    @Size(max = 5)
    private String language;

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

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProtocolDTO protocolDTO = (ProtocolDTO) o;
        if (protocolDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), protocolDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProtocolDTO{" +
            "id=" + getId() +
            ", projectId=" + getProjectId() +
            ", taskId=" + getTaskId() +
            ", participantId=" + getParticipantId() +
            ", pageNumber=" + getPageNumber() +
            ", language=" + getLanguage() +
            "}";
    }
}
