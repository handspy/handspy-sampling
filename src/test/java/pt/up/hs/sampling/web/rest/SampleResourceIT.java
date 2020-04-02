package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.SamplingApp;
import pt.up.hs.sampling.config.SecurityBeanOverrideConfiguration;
import pt.up.hs.sampling.domain.Sample;
import pt.up.hs.sampling.repository.SampleRepository;
import pt.up.hs.sampling.service.SampleService;
import pt.up.hs.sampling.service.dto.SampleDTO;
import pt.up.hs.sampling.service.mapper.SampleMapper;
import pt.up.hs.sampling.web.rest.errors.ExceptionTranslator;
import pt.up.hs.sampling.service.dto.SampleCriteria;
import pt.up.hs.sampling.service.SampleQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static pt.up.hs.sampling.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link SampleResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SamplingApp.class})
public class SampleResourceIT {

    private static final Long DEFAULT_PROJECT_ID = 1L;
    private static final Long OTHER_PROJECT_ID = 2L;

    private static final Long DEFAULT_TASK = 1L;
    private static final Long UPDATED_TASK = 2L;
    private static final Long SMALLER_TASK = 1L - 1L;

    private static final Long DEFAULT_PARTICIPANT = 1L;
    private static final Long UPDATED_PARTICIPANT = 2L;
    private static final Long SMALLER_PARTICIPANT = 1L - 1L;

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private SampleMapper sampleMapper;

    @Autowired
    private SampleService sampleService;

    @Autowired
    private SampleQueryService sampleQueryService;

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

    private MockMvc restSampleMockMvc;

    private Sample sample;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SampleResource sampleResource = new SampleResource(sampleService, sampleQueryService);
        this.restSampleMockMvc = MockMvcBuilders.standaloneSetup(sampleResource)
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
    public static Sample createEntity(EntityManager em) {
        return new Sample()
            .task(DEFAULT_TASK)
            .participant(DEFAULT_PARTICIPANT)
            .timestamp(DEFAULT_TIMESTAMP)
            .language(DEFAULT_LANGUAGE)
            .projectId(DEFAULT_PROJECT_ID);
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Sample createUpdatedEntity(EntityManager em) {
        return new Sample()
            .task(UPDATED_TASK)
            .participant(UPDATED_PARTICIPANT)
            .timestamp(UPDATED_TIMESTAMP)
            .language(UPDATED_LANGUAGE)
            .projectId(OTHER_PROJECT_ID);
    }

    @BeforeEach
    public void initTest() {
        sample = createEntity(em);
    }

    @Test
    @Transactional
    public void createSample() throws Exception {
        int databaseSizeBeforeCreate = sampleRepository.findAll().size();

        // Create the Sample
        SampleDTO sampleDTO = sampleMapper.toDto(sample);
        restSampleMockMvc.perform(post("/api/projects/{projectId}/samples", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sampleDTO)))
            .andExpect(status().isCreated());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeCreate + 1);
        Sample testSample = sampleList.get(sampleList.size() - 1);
        assertThat(testSample.getTask()).isEqualTo(DEFAULT_TASK);
        assertThat(testSample.getParticipant()).isEqualTo(DEFAULT_PARTICIPANT);
        assertThat(testSample.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testSample.getLanguage()).isEqualTo(DEFAULT_LANGUAGE);
        assertThat(testSample.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void createSampleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sampleRepository.findAll().size();

        // Create the Sample with an existing ID
        sample.setId(1L);
        SampleDTO sampleDTO = sampleMapper.toDto(sample);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSampleMockMvc.perform(post("/api/projects/{projectId}/samples", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sampleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTaskIsRequired() throws Exception {
        int databaseSizeBeforeTest = sampleRepository.findAll().size();
        // set the field null
        sample.setTask(null);

        // Create the Sample, which fails.
        SampleDTO sampleDTO = sampleMapper.toDto(sample);

        restSampleMockMvc.perform(post("/api/projects/{projectId}/samples", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sampleDTO)))
            .andExpect(status().isBadRequest());

        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkParticipantIsRequired() throws Exception {
        int databaseSizeBeforeTest = sampleRepository.findAll().size();
        // set the field null
        sample.setParticipant(null);

        // Create the Sample, which fails.
        SampleDTO sampleDTO = sampleMapper.toDto(sample);

        restSampleMockMvc.perform(post("/api/projects/{projectId}/samples", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sampleDTO)))
            .andExpect(status().isBadRequest());

        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProjectIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = sampleRepository.findAll().size();
        // set the field null
        sample.setProjectId(null);

        // Create the Sample, which fails.
        SampleDTO sampleDTO = sampleMapper.toDto(sample);

        restSampleMockMvc.perform(post("/api/projects/{projectId}/samples", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sampleDTO)))
            .andExpect(status().isBadRequest());

        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSamples() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList
        restSampleMockMvc.perform(get("/api/projects/{projectId}/samples?sort=id,desc", DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sample.getId().intValue())))
            .andExpect(jsonPath("$.[*].task").value(hasItem(DEFAULT_TASK.intValue())))
            .andExpect(jsonPath("$.[*].participant").value(hasItem(DEFAULT_PARTICIPANT.intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.intValue())));
    }

    @Test
    @Transactional
    public void getSample() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get the sample
        restSampleMockMvc.perform(get("/api/projects/{projectId}/samples/{id}", DEFAULT_PROJECT_ID, sample.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sample.getId().intValue()))
            .andExpect(jsonPath("$.task").value(DEFAULT_TASK.intValue()))
            .andExpect(jsonPath("$.participant").value(DEFAULT_PARTICIPANT.intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE))
            .andExpect(jsonPath("$.projectId").value(DEFAULT_PROJECT_ID.intValue()));
    }


    @Test
    @Transactional
    public void getSamplesByIdFiltering() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        Long id = sample.getId();

        defaultSampleShouldBeFound("id.equals=" + id);
        defaultSampleShouldNotBeFound("id.notEquals=" + id);

        defaultSampleShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSampleShouldNotBeFound("id.greaterThan=" + id);

        defaultSampleShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSampleShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllSamplesByTaskIsEqualToSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where task equals to DEFAULT_TASK
        defaultSampleShouldBeFound("task.equals=" + DEFAULT_TASK);

        // Get all the sampleList where task equals to UPDATED_TASK
        defaultSampleShouldNotBeFound("task.equals=" + UPDATED_TASK);
    }

    @Test
    @Transactional
    public void getAllSamplesByTaskIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where task not equals to DEFAULT_TASK
        defaultSampleShouldNotBeFound("task.notEquals=" + DEFAULT_TASK);

        // Get all the sampleList where task not equals to UPDATED_TASK
        defaultSampleShouldBeFound("task.notEquals=" + UPDATED_TASK);
    }

    @Test
    @Transactional
    public void getAllSamplesByTaskIsInShouldWork() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where task in DEFAULT_TASK or UPDATED_TASK
        defaultSampleShouldBeFound("task.in=" + DEFAULT_TASK + "," + UPDATED_TASK);

        // Get all the sampleList where task equals to UPDATED_TASK
        defaultSampleShouldNotBeFound("task.in=" + UPDATED_TASK);
    }

    @Test
    @Transactional
    public void getAllSamplesByTaskIsNullOrNotNull() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where task is not null
        defaultSampleShouldBeFound("task.specified=true");

        // Get all the sampleList where task is null
        defaultSampleShouldNotBeFound("task.specified=false");
    }

    @Test
    @Transactional
    public void getAllSamplesByTaskIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where task is greater than or equal to DEFAULT_TASK
        defaultSampleShouldBeFound("task.greaterThanOrEqual=" + DEFAULT_TASK);

        // Get all the sampleList where task is greater than or equal to UPDATED_TASK
        defaultSampleShouldNotBeFound("task.greaterThanOrEqual=" + UPDATED_TASK);
    }

    @Test
    @Transactional
    public void getAllSamplesByTaskIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where task is less than or equal to DEFAULT_TASK
        defaultSampleShouldBeFound("task.lessThanOrEqual=" + DEFAULT_TASK);

        // Get all the sampleList where task is less than or equal to SMALLER_TASK
        defaultSampleShouldNotBeFound("task.lessThanOrEqual=" + SMALLER_TASK);
    }

    @Test
    @Transactional
    public void getAllSamplesByTaskIsLessThanSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where task is less than DEFAULT_TASK
        defaultSampleShouldNotBeFound("task.lessThan=" + DEFAULT_TASK);

        // Get all the sampleList where task is less than UPDATED_TASK
        defaultSampleShouldBeFound("task.lessThan=" + UPDATED_TASK);
    }

    @Test
    @Transactional
    public void getAllSamplesByTaskIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where task is greater than DEFAULT_TASK
        defaultSampleShouldNotBeFound("task.greaterThan=" + DEFAULT_TASK);

        // Get all the sampleList where task is greater than SMALLER_TASK
        defaultSampleShouldBeFound("task.greaterThan=" + SMALLER_TASK);
    }


    @Test
    @Transactional
    public void getAllSamplesByParticipantIsEqualToSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where participant equals to DEFAULT_PARTICIPANT
        defaultSampleShouldBeFound("participant.equals=" + DEFAULT_PARTICIPANT);

        // Get all the sampleList where participant equals to UPDATED_PARTICIPANT
        defaultSampleShouldNotBeFound("participant.equals=" + UPDATED_PARTICIPANT);
    }

    @Test
    @Transactional
    public void getAllSamplesByParticipantIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where participant not equals to DEFAULT_PARTICIPANT
        defaultSampleShouldNotBeFound("participant.notEquals=" + DEFAULT_PARTICIPANT);

        // Get all the sampleList where participant not equals to UPDATED_PARTICIPANT
        defaultSampleShouldBeFound("participant.notEquals=" + UPDATED_PARTICIPANT);
    }

    @Test
    @Transactional
    public void getAllSamplesByParticipantIsInShouldWork() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where participant in DEFAULT_PARTICIPANT or UPDATED_PARTICIPANT
        defaultSampleShouldBeFound("participant.in=" + DEFAULT_PARTICIPANT + "," + UPDATED_PARTICIPANT);

        // Get all the sampleList where participant equals to UPDATED_PARTICIPANT
        defaultSampleShouldNotBeFound("participant.in=" + UPDATED_PARTICIPANT);
    }

    @Test
    @Transactional
    public void getAllSamplesByParticipantIsNullOrNotNull() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where participant is not null
        defaultSampleShouldBeFound("participant.specified=true");

        // Get all the sampleList where participant is null
        defaultSampleShouldNotBeFound("participant.specified=false");
    }

    @Test
    @Transactional
    public void getAllSamplesByParticipantIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where participant is greater than or equal to DEFAULT_PARTICIPANT
        defaultSampleShouldBeFound("participant.greaterThanOrEqual=" + DEFAULT_PARTICIPANT);

        // Get all the sampleList where participant is greater than or equal to UPDATED_PARTICIPANT
        defaultSampleShouldNotBeFound("participant.greaterThanOrEqual=" + UPDATED_PARTICIPANT);
    }

    @Test
    @Transactional
    public void getAllSamplesByParticipantIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where participant is less than or equal to DEFAULT_PARTICIPANT
        defaultSampleShouldBeFound("participant.lessThanOrEqual=" + DEFAULT_PARTICIPANT);

        // Get all the sampleList where participant is less than or equal to SMALLER_PARTICIPANT
        defaultSampleShouldNotBeFound("participant.lessThanOrEqual=" + SMALLER_PARTICIPANT);
    }

    @Test
    @Transactional
    public void getAllSamplesByParticipantIsLessThanSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where participant is less than DEFAULT_PARTICIPANT
        defaultSampleShouldNotBeFound("participant.lessThan=" + DEFAULT_PARTICIPANT);

        // Get all the sampleList where participant is less than UPDATED_PARTICIPANT
        defaultSampleShouldBeFound("participant.lessThan=" + UPDATED_PARTICIPANT);
    }

    @Test
    @Transactional
    public void getAllSamplesByParticipantIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where participant is greater than DEFAULT_PARTICIPANT
        defaultSampleShouldNotBeFound("participant.greaterThan=" + DEFAULT_PARTICIPANT);

        // Get all the sampleList where participant is greater than SMALLER_PARTICIPANT
        defaultSampleShouldBeFound("participant.greaterThan=" + SMALLER_PARTICIPANT);
    }


    @Test
    @Transactional
    public void getAllSamplesByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where timestamp equals to DEFAULT_TIMESTAMP
        defaultSampleShouldBeFound("timestamp.equals=" + DEFAULT_TIMESTAMP);

        // Get all the sampleList where timestamp equals to UPDATED_TIMESTAMP
        defaultSampleShouldNotBeFound("timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllSamplesByTimestampIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where timestamp not equals to DEFAULT_TIMESTAMP
        defaultSampleShouldNotBeFound("timestamp.notEquals=" + DEFAULT_TIMESTAMP);

        // Get all the sampleList where timestamp not equals to UPDATED_TIMESTAMP
        defaultSampleShouldBeFound("timestamp.notEquals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllSamplesByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where timestamp in DEFAULT_TIMESTAMP or UPDATED_TIMESTAMP
        defaultSampleShouldBeFound("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP);

        // Get all the sampleList where timestamp equals to UPDATED_TIMESTAMP
        defaultSampleShouldNotBeFound("timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllSamplesByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where timestamp is not null
        defaultSampleShouldBeFound("timestamp.specified=true");

        // Get all the sampleList where timestamp is null
        defaultSampleShouldNotBeFound("timestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllSamplesByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where language equals to DEFAULT_LANGUAGE
        defaultSampleShouldBeFound("language.equals=" + DEFAULT_LANGUAGE);

        // Get all the sampleList where language equals to UPDATED_LANGUAGE
        defaultSampleShouldNotBeFound("language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    public void getAllSamplesByLanguageIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where language not equals to DEFAULT_LANGUAGE
        defaultSampleShouldNotBeFound("language.notEquals=" + DEFAULT_LANGUAGE);

        // Get all the sampleList where language not equals to UPDATED_LANGUAGE
        defaultSampleShouldBeFound("language.notEquals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    public void getAllSamplesByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where language in DEFAULT_LANGUAGE or UPDATED_LANGUAGE
        defaultSampleShouldBeFound("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE);

        // Get all the sampleList where language equals to UPDATED_LANGUAGE
        defaultSampleShouldNotBeFound("language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    public void getAllSamplesByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where language is not null
        defaultSampleShouldBeFound("language.specified=true");

        // Get all the sampleList where language is null
        defaultSampleShouldNotBeFound("language.specified=false");
    }
                @Test
    @Transactional
    public void getAllSamplesByLanguageContainsSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where language contains DEFAULT_LANGUAGE
        defaultSampleShouldBeFound("language.contains=" + DEFAULT_LANGUAGE);

        // Get all the sampleList where language contains UPDATED_LANGUAGE
        defaultSampleShouldNotBeFound("language.contains=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    public void getAllSamplesByLanguageNotContainsSomething() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        // Get all the sampleList where language does not contain DEFAULT_LANGUAGE
        defaultSampleShouldNotBeFound("language.doesNotContain=" + DEFAULT_LANGUAGE);

        // Get all the sampleList where language does not contain UPDATED_LANGUAGE
        defaultSampleShouldBeFound("language.doesNotContain=" + UPDATED_LANGUAGE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSampleShouldBeFound(String filter) throws Exception {
        restSampleMockMvc.perform(get("/api/projects/{projectId}/samples?sort=id,desc&" + filter, DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sample.getId().intValue())))
            .andExpect(jsonPath("$.[*].task").value(hasItem(DEFAULT_TASK.intValue())))
            .andExpect(jsonPath("$.[*].participant").value(hasItem(DEFAULT_PARTICIPANT.intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.intValue())));

        // Check, that the count call also returns 1
        restSampleMockMvc.perform(get("/api/projects/{projectId}/samples/count?sort=id,desc&" + filter, DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSampleShouldNotBeFound(String filter) throws Exception {
        restSampleMockMvc.perform(get("/api/projects/{projectId}/samples?sort=id,desc&" + filter, DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSampleMockMvc.perform(get("/api/projects/{projectId}/samples/count?sort=id,desc&" + filter, DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingSample() throws Exception {
        // Get the sample
        restSampleMockMvc.perform(get("/api/projects/{projectId}/samples/{id}", DEFAULT_PROJECT_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSample() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        int databaseSizeBeforeUpdate = sampleRepository.findAll().size();

        // Update the sample
        Sample updatedSample = sampleRepository.findById(sample.getId()).get();
        // Disconnect from session so that the updates on updatedSample are not directly saved in db
        em.detach(updatedSample);
        updatedSample
            .task(UPDATED_TASK)
            .participant(UPDATED_PARTICIPANT)
            .timestamp(UPDATED_TIMESTAMP)
            .language(UPDATED_LANGUAGE)
            .projectId(OTHER_PROJECT_ID);
        SampleDTO sampleDTO = sampleMapper.toDto(updatedSample);

        restSampleMockMvc.perform(put("/api/projects/{projectId}/samples", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sampleDTO)))
            .andExpect(status().isOk());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeUpdate);
        Sample testSample = sampleList.get(sampleList.size() - 1);
        assertThat(testSample.getTask()).isEqualTo(UPDATED_TASK);
        assertThat(testSample.getParticipant()).isEqualTo(UPDATED_PARTICIPANT);
        assertThat(testSample.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testSample.getLanguage()).isEqualTo(UPDATED_LANGUAGE);
        assertThat(testSample.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingSample() throws Exception {
        int databaseSizeBeforeUpdate = sampleRepository.findAll().size();

        // Create the Sample
        SampleDTO sampleDTO = sampleMapper.toDto(sample);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSampleMockMvc.perform(put("/api/projects/{projectId}/samples", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sampleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Sample in the database
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSample() throws Exception {
        // Initialize the database
        sampleRepository.saveAndFlush(sample);

        int databaseSizeBeforeDelete = sampleRepository.findAll().size();

        // Delete the sample
        restSampleMockMvc.perform(delete("/api/projects/{projectId}/samples/{id}", DEFAULT_PROJECT_ID, sample.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Sample> sampleList = sampleRepository.findAll();
        assertThat(sampleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
