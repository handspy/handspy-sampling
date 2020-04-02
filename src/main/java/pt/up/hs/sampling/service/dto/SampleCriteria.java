package pt.up.hs.sampling.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link pt.up.hs.sampling.domain.Sample} entity. This class is used
 * in {@link pt.up.hs.sampling.web.rest.SampleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /samples?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SampleCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter task;

    private LongFilter participant;

    private InstantFilter timestamp;

    private StringFilter language;

    public SampleCriteria() {
    }

    public SampleCriteria(SampleCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.task = other.task == null ? null : other.task.copy();
        this.participant = other.participant == null ? null : other.participant.copy();
        this.timestamp = other.timestamp == null ? null : other.timestamp.copy();
        this.language = other.language == null ? null : other.language.copy();
    }

    @Override
    public SampleCriteria copy() {
        return new SampleCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getTask() {
        return task;
    }

    public void setTask(LongFilter task) {
        this.task = task;
    }

    public LongFilter getParticipant() {
        return participant;
    }

    public void setParticipant(LongFilter participant) {
        this.participant = participant;
    }

    public InstantFilter getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(InstantFilter timestamp) {
        this.timestamp = timestamp;
    }

    public StringFilter getLanguage() {
        return language;
    }

    public void setLanguage(StringFilter language) {
        this.language = language;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SampleCriteria that = (SampleCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(task, that.task) &&
            Objects.equals(participant, that.participant) &&
            Objects.equals(timestamp, that.timestamp) &&
            Objects.equals(language, that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        task,
        participant,
        timestamp,
        language
        );
    }

    @Override
    public String toString() {
        return "SampleCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (task != null ? "task=" + task + ", " : "") +
                (participant != null ? "participant=" + participant + ", " : "") +
                (timestamp != null ? "timestamp=" + timestamp + ", " : "") +
                (language != null ? "language=" + language + ", " : "") +
            "}";
    }

}
