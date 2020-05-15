package pt.up.hs.sampling.domain.pojo;

import pt.up.hs.sampling.domain.enumeration.DotType;

import java.io.Serializable;
import java.util.Map;

public class Dot implements Serializable {

    private Double x;
    private Double y;
    private Long timestamp;
    private DotType type;
    private Double pressure;

    private Map<String, Object> metadata;

    public Dot() {
    }

    public Double getX() {
        return x;
    }

    public Dot x(Double x) {
        this.x = x;
        return this;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public Dot y(Double y) {
        this.y = y;
        return this;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public Dot timestamp(Long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public DotType getType() {
        return type;
    }

    public Dot type(DotType type) {
        this.type = type;
        return this;
    }

    public void setType(DotType type) {
        this.type = type;
    }

    public Double getPressure() {
        return pressure;
    }

    public Dot pressure(Double pressure) {
        this.pressure = pressure;
        return this;
    }

    public void setPressure(Double pressure) {
        this.pressure = pressure;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public Dot metadata(Map<String, Object> metadata) {
        this.metadata = metadata;
        return this;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}
