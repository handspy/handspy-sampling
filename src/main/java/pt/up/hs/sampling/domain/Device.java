package pt.up.hs.sampling.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import pt.up.hs.sampling.domain.enumeration.DeviceType;

/**
 * Device used to capture data.\n\n@author José Carlos Paiva
 */
@Entity
@Table(name = "device")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Name of the device
     */
    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Details about the device
     */
    @Column(name = "description")
    private String description;

    /**
     * Type of device
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DeviceType type;

    /**
     * Plugin name for conversion
     */
    @Pattern(regexp = "^[a-zA-Z0-9_-]*$")
    @Column(name = "plugin")
    private String plugin;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Device name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Device description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DeviceType getType() {
        return type;
    }

    public Device type(DeviceType type) {
        this.type = type;
        return this;
    }

    public void setType(DeviceType type) {
        this.type = type;
    }

    public String getPlugin() {
        return plugin;
    }

    public Device plugin(String plugin) {
        this.plugin = plugin;
        return this;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Device)) {
            return false;
        }
        return id != null && id.equals(((Device) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Device{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", plugin='" + getPlugin() + "'" +
            "}";
    }
}
