package pt.up.hs.sampling.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Note taken about a sample by an analyst.\n\n@author José Carlos Paiva
 */
@Entity
@Table(name = "note")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Text of the note
     */
    @Column(name = "text")
    private String text;

    /**
     * Is this note private
     */
    @Column(name = "self")
    private Boolean self;

    /**
     * A note belongs to a sample.
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("notes")
    private Sample sample;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public Note text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean isSelf() {
        return self;
    }

    public Note self(Boolean self) {
        this.self = self;
        return this;
    }

    public void setSelf(Boolean self) {
        this.self = self;
    }

    public Sample getSample() {
        return sample;
    }

    public Note sample(Sample sample) {
        this.sample = sample;
        return this;
    }

    public void setSample(Sample sample) {
        this.sample = sample;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Note)) {
            return false;
        }
        return id != null && id.equals(((Note) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Note{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", self='" + isSelf() + "'" +
            "}";
    }
}
