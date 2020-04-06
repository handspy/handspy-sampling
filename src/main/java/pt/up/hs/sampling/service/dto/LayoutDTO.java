package pt.up.hs.sampling.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link pt.up.hs.sampling.domain.Layout} entity.
 *
 * @author José Carlos Paiva
 */
@ApiModel(description = "Layout of the paper used to collect data.")
public class LayoutDTO extends AbstractAuditingDTO {

    private Long id;

    /**
     * Name of the layout
     */
    @NotNull
    @ApiModelProperty(value = "Name of the layout", required = true)
    private String name;

    /**
     * Details about the layout
     */
    @Size(max = 500)
    @ApiModelProperty(value = "Details about the layout")
    private String description;

    /**
     * Width of the layout (mm)
     */
    @NotNull
    @ApiModelProperty(value = "Width of the layout (mm)", required = true)
    private Integer width;

    /**
     * Height of the layout (mm)
     */
    @NotNull
    @ApiModelProperty(value = "Height of the layout (mm)", required = true)
    private Integer height;

    /**
     * Left margin of the layout (mm)
     */
    @NotNull
    @ApiModelProperty(value = "Left margin of the layout (mm)", required = true)
    private Integer marginLeft;

    /**
     * Right margin of the layout (mm)
     */
    @NotNull
    @ApiModelProperty(value = "Right margin of the layout (mm)", required = true)
    private Integer marginRight;

    /**
     * Top margin of the layout (mm)
     */
    @NotNull
    @ApiModelProperty(value = "Top margin of the layout (mm)", required = true)
    private Integer marginTop;

    /**
     * Bottom margin of the layout (mm)
     */
    @NotNull
    @ApiModelProperty(value = "Bottom margin of the layout (mm)", required = true)
    private Integer marginBottom;

    /**
     * URL to download the layout (mm)
     */
    @ApiModelProperty(value = "URL to download the layout (mm)")
    private String url;


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

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(Integer marginLeft) {
        this.marginLeft = marginLeft;
    }

    public Integer getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(Integer marginRight) {
        this.marginRight = marginRight;
    }

    public Integer getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(Integer marginTop) {
        this.marginTop = marginTop;
    }

    public Integer getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(Integer marginBottom) {
        this.marginBottom = marginBottom;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LayoutDTO layoutDTO = (LayoutDTO) o;
        if (layoutDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), layoutDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "LayoutDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", width=" + getWidth() +
            ", height=" + getHeight() +
            ", marginLeft=" + getMarginLeft() +
            ", marginRight=" + getMarginRight() +
            ", marginTop=" + getMarginTop() +
            ", marginBottom=" + getMarginBottom() +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
