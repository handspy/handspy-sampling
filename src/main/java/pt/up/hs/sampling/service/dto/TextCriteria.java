package pt.up.hs.sampling.service.dto;

import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Criteria class for the {@link pt.up.hs.sampling.domain.Text} entity. This class is used
 * in {@link pt.up.hs.sampling.web.rest.TextResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /texts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TextCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter taskId;

    private LongFilter participantId;

    private StringFilter language;

    public TextCriteria() {
    }

    public TextCriteria(TextCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.taskId = other.taskId == null ? null : other.taskId.copy();
        this.participantId = other.participantId == null ? null : other.participantId.copy();
        this.language = other.language == null ? null : other.language.copy();
    }

    @Override
    public TextCriteria copy() {
        return new TextCriteria(this);
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
        final TextCriteria that = (TextCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(taskId, that.taskId) &&
            Objects.equals(participantId, that.participantId) &&
            Objects.equals(language, that.language);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            taskId,
            participantId,
            language
        );
    }

    @Override
    public String toString() {
        return "TextCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (taskId != null ? "taskId=" + taskId + ", " : "") +
                (participantId != null ? "participantId=" + participantId + ", " : "") +
                (language != null ? "language=" + language + ", " : "") +
            "}";
    }
}
