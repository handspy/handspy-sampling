package pt.up.hs.sampling.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Handwritten data collected using a smartpen for analysis (part of the\nsample).\n\n@author José Carlos Paiva
 */
@Entity
@Table(name = "protocol")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Protocol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Layout in which the protocol has been written
     */
    @Column(name = "layout")
    private Long layout;

    /**
     * Device with which the protocol has been written
     */
    @Column(name = "device")
    private Long device;

    /**
     * Number of the page (if the protocol contains multiple pages)
     */
    @Column(name = "page_number")
    private Integer pageNumber;

    /**
     * A protocol belongs to a sample.
     */
    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("protocols")
    private Sample sample;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLayout() {
        return layout;
    }

    public Protocol layout(Long layout) {
        this.layout = layout;
        return this;
    }

    public void setLayout(Long layout) {
        this.layout = layout;
    }

    public Long getDevice() {
        return device;
    }

    public Protocol device(Long device) {
        this.device = device;
        return this;
    }

    public void setDevice(Long device) {
        this.device = device;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Protocol pageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Sample getSample() {
        return sample;
    }

    public Protocol sample(Sample sample) {
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
        if (!(o instanceof Protocol)) {
            return false;
        }
        return id != null && id.equals(((Protocol) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Protocol{" +
            "id=" + getId() +
            ", layout=" + getLayout() +
            ", device=" + getDevice() +
            ", pageNumber=" + getPageNumber() +
            "}";
    }
}
