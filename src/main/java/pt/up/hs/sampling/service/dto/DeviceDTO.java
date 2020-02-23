package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import pt.up.hs.sampling.domain.enumeration.DeviceType;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Device} entity.
 */
@ApiModel(description = "Device used to capture data.\n\n@author José Carlos Paiva")
public class DeviceDTO implements Serializable {

    private Long id;

    /**
     * Name of the device
     */
    @NotNull
    @ApiModelProperty(value = "Name of the device", required = true)
    private String name;

    /**
     * Details about the device
     */
    @ApiModelProperty(value = "Details about the device")
    private String description;

    /**
     * Type of device
     */
    @ApiModelProperty(value = "Type of device")
    private DeviceType type;

    /**
     * Plugin name for conversion
     */
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$")
    @ApiModelProperty(value = "Plugin name for conversion")
    private String plugin;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DeviceType getType() {
        return type;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DeviceDTO deviceDTO = (DeviceDTO) o;
        if (deviceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), deviceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DeviceDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", plugin='" + getPlugin() + "'" +
            "}";
    }
}
