package pt.up.hs.sampling.domain.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Stroke implements Serializable {

    private Long startTime;
    private Long endTime;

    private List<Dot> dots;

    private Map<String, Object> metadata;

    public Stroke() {
        dots = new ArrayList<>();
    }

    public Stroke(Long startTime, Long endTime, List<Dot> dots) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.dots = dots;
    }

    public Long getStartTime() {
        return startTime;
    }

    public Stroke startTime(Long startTime) {
        this.startTime = startTime;
        return this;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public Stroke endTime(Long endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public List<Dot> getDots() {
        return dots;
    }

    public Stroke addDot(Dot dot) {
        dots.add(dot);
        return this;
    }

    public void setDots(List<Dot> dots) {
        this.dots = dots;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public Stroke metadata(Map<String, Object> metadata) {
        this.metadata = metadata;
        return this;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stroke stroke = (Stroke) o;
        return Objects.equals(getStartTime(), stroke.getStartTime()) &&
            Objects.equals(getEndTime(), stroke.getEndTime()) &&
            Objects.equals(getDots(), stroke.getDots()) &&
            Objects.equals(getMetadata(), stroke.getMetadata());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartTime(), getEndTime(), getDots(), getMetadata());
    }

    @Override
    public String toString() {
        return "Stroke{" +
            "startTime=" + startTime +
            ", endTime=" + endTime +
            ", dots=" + dots +
            ", metadata=" + metadata +
            '}';
    }
}
