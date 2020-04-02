package pt.up.hs.sampling.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Permission} entity.
 */
public class PermissionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String user;

    @NotNull
    private Long projectId;

    @NotNull
    @Size(max = 50)
    private String permission;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PermissionDTO permissionDTO = (PermissionDTO) o;
        if (permissionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), permissionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PermissionDTO{" +
            "id=" + getId() +
            ", user='" + getUser() + "'" +
            ", projectId=" + getProjectId() +
            ", permission='" + getPermission() + "'" +
            "}";
    }
}
