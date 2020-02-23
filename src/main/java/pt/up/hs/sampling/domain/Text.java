package pt.up.hs.sampling.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Typewritten data collected for analysis (part of the sample). It may be a\ntranscription of the protocol (automatic or manually entered by an analyst)\nor text typed by a participant using a typing device.\n\n@author José Carlos Paiva
 */
@Entity
@Table(name = "text")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Text implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Typewritten text collected
     */
    @Column(name = "text")
    private String text;

    /**
     * A text belongs to a sample.
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("texts")
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

    public Text text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Sample getSample() {
        return sample;
    }

    public Text sample(Sample sample) {
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
        if (!(o instanceof Text)) {
            return false;
        }
        return id != null && id.equals(((Text) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Text{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            "}";
    }
}
