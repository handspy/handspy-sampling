package pt.up.hs.sampling.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Layout of the paper used to collect data.\n\n@author José Carlos Paiva
 */
@Entity
@Table(name = "layout")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Layout implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Name of the layout
     */
    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    /**
     * Details about the layout
     */
    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    /**
     * Width of the layout (mm)
     */
    @NotNull
    @Column(name = "width", nullable = false)
    private Integer width;

    /**
     * Height of the layout (mm)
     */
    @NotNull
    @Column(name = "height", nullable = false)
    private Integer height;

    /**
     * Left margin of the layout (mm)
     */
    @NotNull
    @Column(name = "margin_left", nullable = false)
    private Integer marginLeft;

    /**
     * Right margin of the layout (mm)
     */
    @NotNull
    @Column(name = "margin_right", nullable = false)
    private Integer marginRight;

    /**
     * Top margin of the layout (mm)
     */
    @NotNull
    @Column(name = "margin_top", nullable = false)
    private Integer marginTop;

    /**
     * Bottom margin of the layout (mm)
     */
    @NotNull
    @Column(name = "margin_bottom", nullable = false)
    private Integer marginBottom;

    /**
     * URL to download the layout (mm)
     */
    @Column(name = "url")
    private String url;

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

    public Layout name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Layout description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWidth() {
        return width;
    }

    public Layout width(Integer width) {
        this.width = width;
        return this;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public Layout height(Integer height) {
        this.height = height;
        return this;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getMarginLeft() {
        return marginLeft;
    }

    public Layout marginLeft(Integer marginLeft) {
        this.marginLeft = marginLeft;
        return this;
    }

    public void setMarginLeft(Integer marginLeft) {
        this.marginLeft = marginLeft;
    }

    public Integer getMarginRight() {
        return marginRight;
    }

    public Layout marginRight(Integer marginRight) {
        this.marginRight = marginRight;
        return this;
    }

    public void setMarginRight(Integer marginRight) {
        this.marginRight = marginRight;
    }

    public Integer getMarginTop() {
        return marginTop;
    }

    public Layout marginTop(Integer marginTop) {
        this.marginTop = marginTop;
        return this;
    }

    public void setMarginTop(Integer marginTop) {
        this.marginTop = marginTop;
    }

    public Integer getMarginBottom() {
        return marginBottom;
    }

    public Layout marginBottom(Integer marginBottom) {
        this.marginBottom = marginBottom;
        return this;
    }

    public void setMarginBottom(Integer marginBottom) {
        this.marginBottom = marginBottom;
    }

    public String getUrl() {
        return url;
    }

    public Layout url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Layout)) {
            return false;
        }
        return id != null && id.equals(((Layout) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Layout{" +
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
