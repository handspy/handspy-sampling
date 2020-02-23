package pt.up.hs.sampling.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * An annotation added in a text.\n\n@author José Carlos Paiva
 */
@Entity
@Table(name = "annotation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Annotation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Type of this annotation
     */
    @NotNull
    @Column(name = "type", nullable = false)
    private Long type;

    /**
     * Start position of the annotation
     */
    @NotNull
    @Column(name = "start", nullable = false)
    private Integer start;

    /**
     * Size of the annotation
     */
    @NotNull
    @Column(name = "size", nullable = false)
    private Integer size;

    /**
     * Note about annotation
     */
    @Column(name = "note")
    private String note;

    /**
     * An annotation is made in a text.
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("annotations")
    private Text text;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getType() {
        return type;
    }

    public Annotation type(Long type) {
        this.type = type;
        return this;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Integer getStart() {
        return start;
    }

    public Annotation start(Integer start) {
        this.start = start;
        return this;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getSize() {
        return size;
    }

    public Annotation size(Integer size) {
        this.size = size;
        return this;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getNote() {
        return note;
    }

    public Annotation note(String note) {
        this.note = note;
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Text getText() {
        return text;
    }

    public Annotation text(Text text) {
        this.text = text;
        return this;
    }

    public void setText(Text text) {
        this.text = text;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Annotation)) {
            return false;
        }
        return id != null && id.equals(((Annotation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Annotation{" +
            "id=" + getId() +
            ", type=" + getType() +
            ", start=" + getStart() +
            ", size=" + getSize() +
            ", note='" + getNote() + "'" +
            "}";
    }
}
