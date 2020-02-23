package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.SamplingApp;
import pt.up.hs.sampling.config.SecurityBeanOverrideConfiguration;
import pt.up.hs.sampling.domain.Protocol;
import pt.up.hs.sampling.domain.Sample;
import pt.up.hs.sampling.repository.ProtocolRepository;
import pt.up.hs.sampling.service.ProtocolService;
import pt.up.hs.sampling.service.dto.ProtocolDTO;
import pt.up.hs.sampling.service.mapper.ProtocolMapper;
import pt.up.hs.sampling.web.rest.errors.ExceptionTranslator;
import pt.up.hs.sampling.service.dto.ProtocolCriteria;
import pt.up.hs.sampling.service.ProtocolQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static pt.up.hs.sampling.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProtocolResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SamplingApp.class})
public class ProtocolResourceIT {

    private static final Long DEFAULT_LAYOUT = 1L;
    private static final Long UPDATED_LAYOUT = 2L;
    private static final Long SMALLER_LAYOUT = 1L - 1L;

    private static final Long DEFAULT_DEVICE = 1L;
    private static final Long UPDATED_DEVICE = 2L;
    private static final Long SMALLER_DEVICE = 1L - 1L;

    private static final Integer DEFAULT_PAGE_NUMBER = 1;
    private static final Integer UPDATED_PAGE_NUMBER = 2;
    private static final Integer SMALLER_PAGE_NUMBER = 1 - 1;

    @Autowired
    private ProtocolRepository protocolRepository;

    @Autowired
    private ProtocolMapper protocolMapper;

    @Autowired
    private ProtocolService protocolService;

    @Autowired
    private ProtocolQueryService protocolQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restProtocolMockMvc;

    private Protocol protocol;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProtocolResource protocolResource = new ProtocolResource(protocolService, protocolQueryService);
        this.restProtocolMockMvc = MockMvcBuilders.standaloneSetup(protocolResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Protocol createEntity(EntityManager em) {
        Protocol protocol = new Protocol()
            .layout(DEFAULT_LAYOUT)
            .device(DEFAULT_DEVICE)
            .pageNumber(DEFAULT_PAGE_NUMBER);
        // Add required entity
        Sample sample;
        if (TestUtil.findAll(em, Sample.class).isEmpty()) {
            sample = SampleResourceIT.createEntity(em);
            em.persist(sample);
            em.flush();
        } else {
            sample = TestUtil.findAll(em, Sample.class).get(0);
        }
        protocol.setSample(sample);
        return protocol;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Protocol createUpdatedEntity(EntityManager em) {
        Protocol protocol = new Protocol()
            .layout(UPDATED_LAYOUT)
            .device(UPDATED_DEVICE)
            .pageNumber(UPDATED_PAGE_NUMBER);
        // Add required entity
        Sample sample;
        if (TestUtil.findAll(em, Sample.class).isEmpty()) {
            sample = SampleResourceIT.createUpdatedEntity(em);
            em.persist(sample);
            em.flush();
        } else {
            sample = TestUtil.findAll(em, Sample.class).get(0);
        }
        protocol.setSample(sample);
        return protocol;
    }

    @BeforeEach
    public void initTest() {
        protocol = createEntity(em);
    }

    @Test
    @Transactional
    public void createProtocol() throws Exception {
        int databaseSizeBeforeCreate = protocolRepository.findAll().size();

        // Create the Protocol
        ProtocolDTO protocolDTO = protocolMapper.toDto(protocol);
        restProtocolMockMvc.perform(post("/api/protocols")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(protocolDTO)))
            .andExpect(status().isCreated());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeCreate + 1);
        Protocol testProtocol = protocolList.get(protocolList.size() - 1);
        assertThat(testProtocol.getLayout()).isEqualTo(DEFAULT_LAYOUT);
        assertThat(testProtocol.getDevice()).isEqualTo(DEFAULT_DEVICE);
        assertThat(testProtocol.getPageNumber()).isEqualTo(DEFAULT_PAGE_NUMBER);
    }

    @Test
    @Transactional
    public void createProtocolWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = protocolRepository.findAll().size();

        // Create the Protocol with an existing ID
        protocol.setId(1L);
        ProtocolDTO protocolDTO = protocolMapper.toDto(protocol);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProtocolMockMvc.perform(post("/api/protocols")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(protocolDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllProtocols() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList
        restProtocolMockMvc.perform(get("/api/protocols?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(protocol.getId().intValue())))
            .andExpect(jsonPath("$.[*].layout").value(hasItem(DEFAULT_LAYOUT.intValue())))
            .andExpect(jsonPath("$.[*].device").value(hasItem(DEFAULT_DEVICE.intValue())))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)));
    }
    
    @Test
    @Transactional
    public void getProtocol() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get the protocol
        restProtocolMockMvc.perform(get("/api/protocols/{id}", protocol.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(protocol.getId().intValue()))
            .andExpect(jsonPath("$.layout").value(DEFAULT_LAYOUT.intValue()))
            .andExpect(jsonPath("$.device").value(DEFAULT_DEVICE.intValue()))
            .andExpect(jsonPath("$.pageNumber").value(DEFAULT_PAGE_NUMBER));
    }


    @Test
    @Transactional
    public void getProtocolsByIdFiltering() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        Long id = protocol.getId();

        defaultProtocolShouldBeFound("id.equals=" + id);
        defaultProtocolShouldNotBeFound("id.notEquals=" + id);

        defaultProtocolShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProtocolShouldNotBeFound("id.greaterThan=" + id);

        defaultProtocolShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProtocolShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProtocolsByLayoutIsEqualToSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where layout equals to DEFAULT_LAYOUT
        defaultProtocolShouldBeFound("layout.equals=" + DEFAULT_LAYOUT);

        // Get all the protocolList where layout equals to UPDATED_LAYOUT
        defaultProtocolShouldNotBeFound("layout.equals=" + UPDATED_LAYOUT);
    }

    @Test
    @Transactional
    public void getAllProtocolsByLayoutIsNotEqualToSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where layout not equals to DEFAULT_LAYOUT
        defaultProtocolShouldNotBeFound("layout.notEquals=" + DEFAULT_LAYOUT);

        // Get all the protocolList where layout not equals to UPDATED_LAYOUT
        defaultProtocolShouldBeFound("layout.notEquals=" + UPDATED_LAYOUT);
    }

    @Test
    @Transactional
    public void getAllProtocolsByLayoutIsInShouldWork() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where layout in DEFAULT_LAYOUT or UPDATED_LAYOUT
        defaultProtocolShouldBeFound("layout.in=" + DEFAULT_LAYOUT + "," + UPDATED_LAYOUT);

        // Get all the protocolList where layout equals to UPDATED_LAYOUT
        defaultProtocolShouldNotBeFound("layout.in=" + UPDATED_LAYOUT);
    }

    @Test
    @Transactional
    public void getAllProtocolsByLayoutIsNullOrNotNull() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where layout is not null
        defaultProtocolShouldBeFound("layout.specified=true");

        // Get all the protocolList where layout is null
        defaultProtocolShouldNotBeFound("layout.specified=false");
    }

    @Test
    @Transactional
    public void getAllProtocolsByLayoutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where layout is greater than or equal to DEFAULT_LAYOUT
        defaultProtocolShouldBeFound("layout.greaterThanOrEqual=" + DEFAULT_LAYOUT);

        // Get all the protocolList where layout is greater than or equal to UPDATED_LAYOUT
        defaultProtocolShouldNotBeFound("layout.greaterThanOrEqual=" + UPDATED_LAYOUT);
    }

    @Test
    @Transactional
    public void getAllProtocolsByLayoutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where layout is less than or equal to DEFAULT_LAYOUT
        defaultProtocolShouldBeFound("layout.lessThanOrEqual=" + DEFAULT_LAYOUT);

        // Get all the protocolList where layout is less than or equal to SMALLER_LAYOUT
        defaultProtocolShouldNotBeFound("layout.lessThanOrEqual=" + SMALLER_LAYOUT);
    }

    @Test
    @Transactional
    public void getAllProtocolsByLayoutIsLessThanSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where layout is less than DEFAULT_LAYOUT
        defaultProtocolShouldNotBeFound("layout.lessThan=" + DEFAULT_LAYOUT);

        // Get all the protocolList where layout is less than UPDATED_LAYOUT
        defaultProtocolShouldBeFound("layout.lessThan=" + UPDATED_LAYOUT);
    }

    @Test
    @Transactional
    public void getAllProtocolsByLayoutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where layout is greater than DEFAULT_LAYOUT
        defaultProtocolShouldNotBeFound("layout.greaterThan=" + DEFAULT_LAYOUT);

        // Get all the protocolList where layout is greater than SMALLER_LAYOUT
        defaultProtocolShouldBeFound("layout.greaterThan=" + SMALLER_LAYOUT);
    }


    @Test
    @Transactional
    public void getAllProtocolsByDeviceIsEqualToSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where device equals to DEFAULT_DEVICE
        defaultProtocolShouldBeFound("device.equals=" + DEFAULT_DEVICE);

        // Get all the protocolList where device equals to UPDATED_DEVICE
        defaultProtocolShouldNotBeFound("device.equals=" + UPDATED_DEVICE);
    }

    @Test
    @Transactional
    public void getAllProtocolsByDeviceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where device not equals to DEFAULT_DEVICE
        defaultProtocolShouldNotBeFound("device.notEquals=" + DEFAULT_DEVICE);

        // Get all the protocolList where device not equals to UPDATED_DEVICE
        defaultProtocolShouldBeFound("device.notEquals=" + UPDATED_DEVICE);
    }

    @Test
    @Transactional
    public void getAllProtocolsByDeviceIsInShouldWork() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where device in DEFAULT_DEVICE or UPDATED_DEVICE
        defaultProtocolShouldBeFound("device.in=" + DEFAULT_DEVICE + "," + UPDATED_DEVICE);

        // Get all the protocolList where device equals to UPDATED_DEVICE
        defaultProtocolShouldNotBeFound("device.in=" + UPDATED_DEVICE);
    }

    @Test
    @Transactional
    public void getAllProtocolsByDeviceIsNullOrNotNull() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where device is not null
        defaultProtocolShouldBeFound("device.specified=true");

        // Get all the protocolList where device is null
        defaultProtocolShouldNotBeFound("device.specified=false");
    }

    @Test
    @Transactional
    public void getAllProtocolsByDeviceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where device is greater than or equal to DEFAULT_DEVICE
        defaultProtocolShouldBeFound("device.greaterThanOrEqual=" + DEFAULT_DEVICE);

        // Get all the protocolList where device is greater than or equal to UPDATED_DEVICE
        defaultProtocolShouldNotBeFound("device.greaterThanOrEqual=" + UPDATED_DEVICE);
    }

    @Test
    @Transactional
    public void getAllProtocolsByDeviceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where device is less than or equal to DEFAULT_DEVICE
        defaultProtocolShouldBeFound("device.lessThanOrEqual=" + DEFAULT_DEVICE);

        // Get all the protocolList where device is less than or equal to SMALLER_DEVICE
        defaultProtocolShouldNotBeFound("device.lessThanOrEqual=" + SMALLER_DEVICE);
    }

    @Test
    @Transactional
    public void getAllProtocolsByDeviceIsLessThanSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where device is less than DEFAULT_DEVICE
        defaultProtocolShouldNotBeFound("device.lessThan=" + DEFAULT_DEVICE);

        // Get all the protocolList where device is less than UPDATED_DEVICE
        defaultProtocolShouldBeFound("device.lessThan=" + UPDATED_DEVICE);
    }

    @Test
    @Transactional
    public void getAllProtocolsByDeviceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where device is greater than DEFAULT_DEVICE
        defaultProtocolShouldNotBeFound("device.greaterThan=" + DEFAULT_DEVICE);

        // Get all the protocolList where device is greater than SMALLER_DEVICE
        defaultProtocolShouldBeFound("device.greaterThan=" + SMALLER_DEVICE);
    }


    @Test
    @Transactional
    public void getAllProtocolsByPageNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where pageNumber equals to DEFAULT_PAGE_NUMBER
        defaultProtocolShouldBeFound("pageNumber.equals=" + DEFAULT_PAGE_NUMBER);

        // Get all the protocolList where pageNumber equals to UPDATED_PAGE_NUMBER
        defaultProtocolShouldNotBeFound("pageNumber.equals=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProtocolsByPageNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where pageNumber not equals to DEFAULT_PAGE_NUMBER
        defaultProtocolShouldNotBeFound("pageNumber.notEquals=" + DEFAULT_PAGE_NUMBER);

        // Get all the protocolList where pageNumber not equals to UPDATED_PAGE_NUMBER
        defaultProtocolShouldBeFound("pageNumber.notEquals=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProtocolsByPageNumberIsInShouldWork() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where pageNumber in DEFAULT_PAGE_NUMBER or UPDATED_PAGE_NUMBER
        defaultProtocolShouldBeFound("pageNumber.in=" + DEFAULT_PAGE_NUMBER + "," + UPDATED_PAGE_NUMBER);

        // Get all the protocolList where pageNumber equals to UPDATED_PAGE_NUMBER
        defaultProtocolShouldNotBeFound("pageNumber.in=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProtocolsByPageNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where pageNumber is not null
        defaultProtocolShouldBeFound("pageNumber.specified=true");

        // Get all the protocolList where pageNumber is null
        defaultProtocolShouldNotBeFound("pageNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllProtocolsByPageNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where pageNumber is greater than or equal to DEFAULT_PAGE_NUMBER
        defaultProtocolShouldBeFound("pageNumber.greaterThanOrEqual=" + DEFAULT_PAGE_NUMBER);

        // Get all the protocolList where pageNumber is greater than or equal to UPDATED_PAGE_NUMBER
        defaultProtocolShouldNotBeFound("pageNumber.greaterThanOrEqual=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProtocolsByPageNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where pageNumber is less than or equal to DEFAULT_PAGE_NUMBER
        defaultProtocolShouldBeFound("pageNumber.lessThanOrEqual=" + DEFAULT_PAGE_NUMBER);

        // Get all the protocolList where pageNumber is less than or equal to SMALLER_PAGE_NUMBER
        defaultProtocolShouldNotBeFound("pageNumber.lessThanOrEqual=" + SMALLER_PAGE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProtocolsByPageNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where pageNumber is less than DEFAULT_PAGE_NUMBER
        defaultProtocolShouldNotBeFound("pageNumber.lessThan=" + DEFAULT_PAGE_NUMBER);

        // Get all the protocolList where pageNumber is less than UPDATED_PAGE_NUMBER
        defaultProtocolShouldBeFound("pageNumber.lessThan=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProtocolsByPageNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where pageNumber is greater than DEFAULT_PAGE_NUMBER
        defaultProtocolShouldNotBeFound("pageNumber.greaterThan=" + DEFAULT_PAGE_NUMBER);

        // Get all the protocolList where pageNumber is greater than SMALLER_PAGE_NUMBER
        defaultProtocolShouldBeFound("pageNumber.greaterThan=" + SMALLER_PAGE_NUMBER);
    }


    @Test
    @Transactional
    public void getAllProtocolsBySampleIsEqualToSomething() throws Exception {
        // Get already existing entity
        Sample sample = protocol.getSample();
        protocolRepository.saveAndFlush(protocol);
        Long sampleId = sample.getId();

        // Get all the protocolList where sample equals to sampleId
        defaultProtocolShouldBeFound("sampleId.equals=" + sampleId);

        // Get all the protocolList where sample equals to sampleId + 1
        defaultProtocolShouldNotBeFound("sampleId.equals=" + (sampleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProtocolShouldBeFound(String filter) throws Exception {
        restProtocolMockMvc.perform(get("/api/protocols?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(protocol.getId().intValue())))
            .andExpect(jsonPath("$.[*].layout").value(hasItem(DEFAULT_LAYOUT.intValue())))
            .andExpect(jsonPath("$.[*].device").value(hasItem(DEFAULT_DEVICE.intValue())))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)));

        // Check, that the count call also returns 1
        restProtocolMockMvc.perform(get("/api/protocols/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProtocolShouldNotBeFound(String filter) throws Exception {
        restProtocolMockMvc.perform(get("/api/protocols?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProtocolMockMvc.perform(get("/api/protocols/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProtocol() throws Exception {
        // Get the protocol
        restProtocolMockMvc.perform(get("/api/protocols/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProtocol() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        int databaseSizeBeforeUpdate = protocolRepository.findAll().size();

        // Update the protocol
        Protocol updatedProtocol = protocolRepository.findById(protocol.getId()).get();
        // Disconnect from session so that the updates on updatedProtocol are not directly saved in db
        em.detach(updatedProtocol);
        updatedProtocol
            .layout(UPDATED_LAYOUT)
            .device(UPDATED_DEVICE)
            .pageNumber(UPDATED_PAGE_NUMBER);
        ProtocolDTO protocolDTO = protocolMapper.toDto(updatedProtocol);

        restProtocolMockMvc.perform(put("/api/protocols")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(protocolDTO)))
            .andExpect(status().isOk());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeUpdate);
        Protocol testProtocol = protocolList.get(protocolList.size() - 1);
        assertThat(testProtocol.getLayout()).isEqualTo(UPDATED_LAYOUT);
        assertThat(testProtocol.getDevice()).isEqualTo(UPDATED_DEVICE);
        assertThat(testProtocol.getPageNumber()).isEqualTo(UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    public void updateNonExistingProtocol() throws Exception {
        int databaseSizeBeforeUpdate = protocolRepository.findAll().size();

        // Create the Protocol
        ProtocolDTO protocolDTO = protocolMapper.toDto(protocol);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProtocolMockMvc.perform(put("/api/protocols")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(protocolDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProtocol() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        int databaseSizeBeforeDelete = protocolRepository.findAll().size();

        // Delete the protocol
        restProtocolMockMvc.perform(delete("/api/protocols/{id}", protocol.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
