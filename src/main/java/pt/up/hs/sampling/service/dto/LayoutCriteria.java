package pt.up.hs.sampling.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link pt.up.hs.sampling.domain.Layout} entity. This class is used
 * in {@link pt.up.hs.sampling.web.rest.LayoutResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /layouts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LayoutCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private IntegerFilter width;

    private IntegerFilter height;

    private IntegerFilter marginLeft;

    private IntegerFilter marginRight;

    private IntegerFilter marginTop;

    private IntegerFilter marginBottom;

    private StringFilter url;

    public LayoutCriteria() {
    }

    public LayoutCriteria(LayoutCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.width = other.width == null ? null : other.width.copy();
        this.height = other.height == null ? null : other.height.copy();
        this.marginLeft = other.marginLeft == null ? null : other.marginLeft.copy();
        this.marginRight = other.marginRight == null ? null : other.marginRight.copy();
        this.marginTop = other.marginTop == null ? null : other.marginTop.copy();
        this.marginBottom = other.marginBottom == null ? null : other.marginBottom.copy();
        this.url = other.url == null ? null : other.url.copy();
    }

    @Override
    public LayoutCriteria copy() {
        return new LayoutCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getWidth() {
        return width;
    }

    public void setWidth(IntegerFilter width) {
        this.width = width;
    }

    public IntegerFilter getHeight() {
        return height;
    }

    public void setHeight(IntegerFilter height) {
        this.height = height;
    }

    public IntegerFilter getMarginLeft() {
        return marginLeft;
    }

    public void setMarginLeft(IntegerFilter marginLeft) {
        this.marginLeft = marginLeft;
    }

    public IntegerFilter getMarginRight() {
        return marginRight;
    }

    public void setMarginRight(IntegerFilter marginRight) {
        this.marginRight = marginRight;
    }

    public IntegerFilter getMarginTop() {
        return marginTop;
    }

    public void setMarginTop(IntegerFilter marginTop) {
        this.marginTop = marginTop;
    }

    public IntegerFilter getMarginBottom() {
        return marginBottom;
    }

    public void setMarginBottom(IntegerFilter marginBottom) {
        this.marginBottom = marginBottom;
    }

    public StringFilter getUrl() {
        return url;
    }

    public void setUrl(StringFilter url) {
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
        final LayoutCriteria that = (LayoutCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(width, that.width) &&
            Objects.equals(height, that.height) &&
            Objects.equals(marginLeft, that.marginLeft) &&
            Objects.equals(marginRight, that.marginRight) &&
            Objects.equals(marginTop, that.marginTop) &&
            Objects.equals(marginBottom, that.marginBottom) &&
            Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        description,
        width,
        height,
        marginLeft,
        marginRight,
        marginTop,
        marginBottom,
        url
        );
    }

    @Override
    public String toString() {
        return "LayoutCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (width != null ? "width=" + width + ", " : "") +
                (height != null ? "height=" + height + ", " : "") +
                (marginLeft != null ? "marginLeft=" + marginLeft + ", " : "") +
                (marginRight != null ? "marginRight=" + marginRight + ", " : "") +
                (marginTop != null ? "marginTop=" + marginTop + ", " : "") +
                (marginBottom != null ? "marginBottom=" + marginBottom + ", " : "") +
                (url != null ? "url=" + url + ", " : "") +
            "}";
    }

}
