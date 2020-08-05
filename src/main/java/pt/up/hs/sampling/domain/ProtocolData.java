package pt.up.hs.sampling.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import pt.up.hs.sampling.domain.pojo.Stroke;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.*;

/**
 * Handwritten data collected using a smartpen for analysis (part of the
 * sample).
 *
 * @author José Carlos Paiva
 */
@Entity
@Table(name = "protocol_data")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ProtocolData extends AbstractAuditingEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "protocol_id", unique = true, nullable = false)
    private Long protocolId;

    @OneToOne(optional = false)
    @JoinColumn(name = "protocol_id")
    @MapsId
    private Protocol protocol;

    /**
     * Width of the layout in which the protocol has been written.
     */
    @Column(name = "width", nullable = false)
    private Double width;

    /**
     * Height of the layout in which the protocol has been written.
     */
    @Column(name = "height", nullable = false)
    private Double height;

    /**
     * Metadata of the capture.
     */
    // @Column(name = "metadata")
    @Type(type = "jsonb")
    private Map<String, Object> metadata = new HashMap<>();

    /**
     * Strokes captured.
     */
    @NotNull
    @Type(type = "jsonb")
    // @Column(columnDefinition = "jsonb")
    private List<Stroke> strokes = new ArrayList<>();

    /**
     * Is the preview of this protocol dirty/outdated?
     */
    @Column(name = "dirty_preview")
    @JsonIgnore
    private boolean dirtyPreview = true;

    public Long getProtocolId() {
        return protocolId;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public ProtocolData protocol(Protocol protocol) {
        this.protocol = protocol;
        return this;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public Double getWidth() {
        return width;
    }

    public ProtocolData width(Double width) {
        this.width = width;
        return this;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public ProtocolData height(Double height) {
        this.height = height;
        return this;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public List<Stroke> getStrokes() {
        return strokes;
    }

    public ProtocolData strokes(List<Stroke> strokes) {
        this.strokes = strokes;
        return this;
    }

    public ProtocolData addStroke(Stroke stroke) {
        this.strokes.add(stroke);
        return this;
    }

    public void setStrokes(List<Stroke> strokes) {
        this.strokes = strokes;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public ProtocolData metadata(Map<String, Object> metadata) {
        this.metadata = metadata;
        return this;
    }

    public ProtocolData addMetadata(String key, Object value) {
        this.metadata.put(key, value);
        return this;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public boolean isDirtyPreview() {
        return dirtyPreview;
    }

    public ProtocolData dirtyPreview(boolean dirtyPreview) {
        this.dirtyPreview = dirtyPreview;
        return this;
    }

    public void setDirtyPreview(boolean dirtyPreview) {
        this.dirtyPreview = dirtyPreview;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProtocolData that = (ProtocolData) o;
        return Objects.equals(getProtocolId(), that.getProtocolId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProtocolId());
    }

    @Override
    public String toString() {
        return "ProtocolData{" +
            ", protocol=" + protocol +
            ", width=" + width +
            ", height=" + height +
            ", strokes=" + strokes +
            ", dirtyPreview=" + dirtyPreview +
            '}';
    }
}
