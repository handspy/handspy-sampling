package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Text} entity.
 *
 * @author José Carlos Paiva
 */
@ApiModel(description = "Typewritten data collected for analysis (part of th" +
    "e sample). It may be a transcription of the protocol (automatic or manu" +
    "ally entered by an analyst) or text typed by a participant using a typi" +
    "ng device.")
public class TextDTO extends AbstractAuditingDTO {

    private Long id;

    /**
     * ID of the project (from Projects microservice).
     */
    @NotNull
    @ApiModelProperty(value = "ID of the project (from Projects microservice).")
    private Long projectId;

    /**
     * ID of the sample to which this text is linked (if any).
     */
    @ApiModelProperty(value = "ID of the sample to which this text is linked" +
        " (if any).")
    private Long sampleId;

    /**
     * Typewritten text collected.
     */
    @ApiModelProperty(value = "Typewritten text collected")
    private String text;

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

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
            ", sampleId=" + getSampleId() +
            ", text='" + getText() + "'" +
            "}";
    }
}
