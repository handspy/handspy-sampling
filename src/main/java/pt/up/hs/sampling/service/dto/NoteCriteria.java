package pt.up.hs.sampling.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link pt.up.hs.sampling.domain.Note} entity. This class is used
 * in {@link pt.up.hs.sampling.web.rest.NoteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class NoteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter taskId;

    private LongFilter participantId;

    private StringFilter text;

    private BooleanFilter self;

    public NoteCriteria() {
    }

    public NoteCriteria(NoteCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.taskId = other.taskId == null ? null : other.taskId.copy();
        this.participantId = other.participantId == null ? null : other.participantId.copy();
        this.text = other.text == null ? null : other.text.copy();
        this.self = other.self == null ? null : other.self.copy();
    }

    @Override
    public NoteCriteria copy() {
        return new NoteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getTaskId() {
        return taskId;
    }

    public void setTaskId(LongFilter taskId) {
        this.taskId = taskId;
    }

    public LongFilter getParticipantId() {
        return participantId;
    }

    public void setParticipantId(LongFilter participantId) {
        this.participantId = participantId;
    }

    public StringFilter getText() {
        return text;
    }

    public void setText(StringFilter text) {
        this.text = text;
    }

    public BooleanFilter getSelf() {
        return self;
    }

    public void setSelf(BooleanFilter self) {
        this.self = self;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final NoteCriteria that = (NoteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(taskId, that.taskId) &&
            Objects.equals(participantId, that.participantId) &&
            Objects.equals(text, that.text) &&
            Objects.equals(self, that.self);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            taskId,
            participantId,
            text,
            self
        );
    }

    @Override
    public String toString() {
        return "NoteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (taskId != null ? "taskId=" + taskId + ", " : "") +
                (participantId != null ? "participantId=" + participantId + ", " : "") +
                (text != null ? "text=" + text + ", " : "") +
                (self != null ? "self=" + self + ", " : "") +
            "}";
    }

}
