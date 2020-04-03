package pt.up.hs.sampling.web.rest;

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
import pt.up.hs.sampling.SamplingApp;
import pt.up.hs.sampling.config.SecurityBeanOverrideConfiguration;
import pt.up.hs.sampling.domain.Stroke;
import pt.up.hs.sampling.domain.Protocol;
import pt.up.hs.sampling.repository.StrokeRepository;
import pt.up.hs.sampling.service.StrokeQueryService;
import pt.up.hs.sampling.service.StrokeService;
import pt.up.hs.sampling.service.dto.StrokeDTO;
import pt.up.hs.sampling.service.mapper.StrokeMapper;
import pt.up.hs.sampling.web.rest.errors.ExceptionTranslator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pt.up.hs.sampling.web.rest.TestUtil.createFormattingConversionService;

/**
 * Integration tests for the {@link StrokeResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SamplingApp.class})
public class StrokeResourceIT {

    private static final Long DEFAULT_START_TIME = 300L;
    private static final Long UPDATED_START_TIME = 600L;
    private static final Long SMALLER_START_TIME = 10L;

    private static final Long DEFAULT_END_TIME = 800L;
    private static final Long UPDATED_END_TIME = 1990L;
    private static final Long SMALLER_END_TIME = 700L;

    @Autowired
    private StrokeRepository strokeRepository;

    @Autowired
    private StrokeMapper strokeMapper;

    @Autowired
    private StrokeService strokeService;

    @Autowired
    private StrokeQueryService strokeQueryService;

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

    private MockMvc restStrokeMockMvc;

    private static Protocol defaultProtocol;

    private Stroke stroke;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StrokeResource strokeResource = new StrokeResource(strokeService, strokeQueryService);
        this.restStrokeMockMvc = MockMvcBuilders.standaloneSetup(strokeResource)
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
    public static Stroke createEntity(EntityManager em, Protocol protocol) {
        return new Stroke()
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .protocol(protocol);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stroke createUpdatedEntity(EntityManager em) {
        return new Stroke()
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .protocol(defaultProtocol);
    }

    public static Protocol getProtocol(EntityManager em) {
        Protocol protocol;
        if (TestUtil.findAll(em, Protocol.class).isEmpty()) {
            protocol = ProtocolResourceIT.createEntity(em);
            em.persist(protocol);
            em.flush();
        } else {
            protocol = TestUtil.findAll(em, Protocol.class).get(0);
        }
        return protocol;
    }

    @BeforeEach
    public void initTest() {
        defaultProtocol = getProtocol(em);
        stroke = createEntity(em, defaultProtocol);
    }

    @Test
    @Transactional
    public void createStroke() throws Exception {
        int databaseSizeBeforeCreate = strokeRepository.findAll().size();

        // Create the Stroke
        StrokeDTO strokeDTO = strokeMapper.toDto(stroke);
        restStrokeMockMvc.perform(post("/api/projects/{projectId}/protocols/{protocolId}/strokes", defaultProtocol.getProjectId(), defaultProtocol.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(strokeDTO)))
            .andExpect(status().isCreated());

        // Validate the Stroke in the database
        List<Stroke> strokeList = strokeRepository.findAll();
        assertThat(strokeList).hasSize(databaseSizeBeforeCreate + 1);
        Stroke testStroke = strokeList.get(strokeList.size() - 1);
        assertThat(testStroke.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testStroke.getEndTime()).isEqualTo(DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    public void createStrokeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = strokeRepository.findAll().size();

        // Create the Stroke with an existing ID
        stroke.setId(1L);
        StrokeDTO strokeDTO = strokeMapper.toDto(stroke);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStrokeMockMvc.perform(post("/api/projects/{projectId}/protocols/{protocolId}/strokes", defaultProtocol.getProjectId(), defaultProtocol.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(strokeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Stroke in the database
        List<Stroke> strokeList = strokeRepository.findAll();
        assertThat(strokeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = strokeRepository.findAll().size();
        // set the field null
        stroke.setStartTime(null);

        // Create the Stroke, which fails.
        StrokeDTO strokeDTO = strokeMapper.toDto(stroke);

        restStrokeMockMvc.perform(post("/api/projects/{projectId}/protocols/{protocolId}/strokes", defaultProtocol.getProjectId(), defaultProtocol.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(strokeDTO)))
            .andExpect(status().isBadRequest());

        List<Stroke> strokeList = strokeRepository.findAll();
        assertThat(strokeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = strokeRepository.findAll().size();
        // set the field null
        stroke.setEndTime(null);

        // Create the Stroke, which fails.
        StrokeDTO strokeDTO = strokeMapper.toDto(stroke);

        restStrokeMockMvc.perform(post("/api/projects/{projectId}/protocols/{protocolId}/strokes", defaultProtocol.getProjectId(), defaultProtocol.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(strokeDTO)))
            .andExpect(status().isBadRequest());

        List<Stroke> strokeList = strokeRepository.findAll();
        assertThat(strokeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStrokes() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        // Get all the strokeList
        restStrokeMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/strokes?sort=id,desc", defaultProtocol.getProjectId(), defaultProtocol.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stroke.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.intValue())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.intValue())));
    }

    @Test
    @Transactional
    public void getStroke() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        // Get the stroke
        restStrokeMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/strokes/{id}", defaultProtocol.getProjectId(), defaultProtocol.getId(), stroke.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stroke.getId().intValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.intValue()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.intValue()));
    }


    @Test
    @Transactional
    public void getStrokesByIdFiltering() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        Long id = stroke.getId();

        defaultStrokeShouldBeFound("id.equals=" + id);
        defaultStrokeShouldNotBeFound("id.notEquals=" + id);

        defaultStrokeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStrokeShouldNotBeFound("id.greaterThan=" + id);

        defaultStrokeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStrokeShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    public void getAllStrokesByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        // Get all the strokeList where startTime equals to DEFAULT_START_TIME
        defaultStrokeShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the strokeList where startTime equals to UPDATED_START_TIME
        defaultStrokeShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllStrokesByStartTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        // Get all the strokeList where startTime not equals to DEFAULT_START_TIME
        defaultStrokeShouldNotBeFound("startTime.notEquals=" + DEFAULT_START_TIME);

        // Get all the strokeList where startTime not equals to UPDATED_START_TIME
        defaultStrokeShouldBeFound("startTime.notEquals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllStrokesByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        // Get all the strokeList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultStrokeShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the strokeList where startTime equals to UPDATED_START_TIME
        defaultStrokeShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllStrokesByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        // Get all the strokeList where startTime is not null
        defaultStrokeShouldBeFound("startTime.specified=true");

        // Get all the strokeList where startTime is null
        defaultStrokeShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllStrokesByStartTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        // Get all the strokeList where startTime is greater than or equal to DEFAULT_START_TIME
        defaultStrokeShouldBeFound("startTime.greaterThanOrEqual=" + DEFAULT_START_TIME);

        // Get all the strokeList where startTime is greater than or equal to UPDATED_START_TIME
        defaultStrokeShouldNotBeFound("startTime.greaterThanOrEqual=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllStrokesByStartTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        // Get all the strokeList where startTime is less than or equal to DEFAULT_START_TIME
        defaultStrokeShouldBeFound("startTime.lessThanOrEqual=" + DEFAULT_START_TIME);

        // Get all the strokeList where startTime is less than or equal to SMALLER_START_TIME
        defaultStrokeShouldNotBeFound("startTime.lessThanOrEqual=" + SMALLER_START_TIME);
    }

    @Test
    @Transactional
    public void getAllStrokesByStartTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        // Get all the strokeList where startTime is less than DEFAULT_START_TIME
        defaultStrokeShouldNotBeFound("startTime.lessThan=" + DEFAULT_START_TIME);

        // Get all the strokeList where startTime is less than UPDATED_START_TIME
        defaultStrokeShouldBeFound("startTime.lessThan=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    public void getAllStrokesByStartTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        // Get all the strokeList where startTime is greater than DEFAULT_START_TIME
        defaultStrokeShouldNotBeFound("startTime.greaterThan=" + DEFAULT_START_TIME);

        // Get all the strokeList where startTime is greater than SMALLER_START_TIME
        defaultStrokeShouldBeFound("startTime.greaterThan=" + SMALLER_START_TIME);
    }

    @Test
    @Transactional
    public void getAllStrokesByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        // Get all the strokeList where endTime equals to DEFAULT_END_TIME
        defaultStrokeShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the strokeList where endTime equals to UPDATED_END_TIME
        defaultStrokeShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllStrokesByEndTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        // Get all the strokeList where endTime not equals to DEFAULT_END_TIME
        defaultStrokeShouldNotBeFound("endTime.notEquals=" + DEFAULT_END_TIME);

        // Get all the strokeList where endTime not equals to UPDATED_END_TIME
        defaultStrokeShouldBeFound("endTime.notEquals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllStrokesByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        // Get all the strokeList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultStrokeShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the strokeList where endTime equals to UPDATED_END_TIME
        defaultStrokeShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllStrokesByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        // Get all the strokeList where endTime is not null
        defaultStrokeShouldBeFound("endTime.specified=true");

        // Get all the strokeList where endTime is null
        defaultStrokeShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllStrokesByEndTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        // Get all the strokeList where endTime is greater than or equal to DEFAULT_END_TIME
        defaultStrokeShouldBeFound("endTime.greaterThanOrEqual=" + DEFAULT_END_TIME);

        // Get all the strokeList where endTime is greater than or equal to UPDATED_END_TIME
        defaultStrokeShouldNotBeFound("endTime.greaterThanOrEqual=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllStrokesByEndTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        // Get all the strokeList where endTime is less than or equal to DEFAULT_END_TIME
        defaultStrokeShouldBeFound("endTime.lessThanOrEqual=" + DEFAULT_END_TIME);

        // Get all the strokeList where endTime is less than or equal to SMALLER_END_TIME
        defaultStrokeShouldNotBeFound("endTime.lessThanOrEqual=" + SMALLER_END_TIME);
    }

    @Test
    @Transactional
    public void getAllStrokesByEndTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        // Get all the strokeList where endTime is less than DEFAULT_END_TIME
        defaultStrokeShouldNotBeFound("endTime.lessThan=" + DEFAULT_END_TIME);

        // Get all the strokeList where endTime is less than UPDATED_END_TIME
        defaultStrokeShouldBeFound("endTime.lessThan=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllStrokesByEndTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        // Get all the strokeList where endTime is greater than DEFAULT_END_TIME
        defaultStrokeShouldNotBeFound("endTime.greaterThan=" + DEFAULT_END_TIME);

        // Get all the strokeList where endTime is greater than SMALLER_END_TIME
        defaultStrokeShouldBeFound("endTime.greaterThan=" + SMALLER_END_TIME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStrokeShouldBeFound(String filter) throws Exception {
        restStrokeMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/strokes?sort=id,desc&" + filter, defaultProtocol.getProjectId(), defaultProtocol.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stroke.getId().intValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.intValue())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.intValue())));

        // Check, that the count call also returns 1
        restStrokeMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/strokes/count?sort=id,desc&" + filter, defaultProtocol.getProjectId(), defaultProtocol.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStrokeShouldNotBeFound(String filter) throws Exception {
        restStrokeMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/strokes?sort=id,desc&" + filter, defaultProtocol.getProjectId(), defaultProtocol.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStrokeMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/strokes/count?sort=id,desc&" + filter, defaultProtocol.getProjectId(), defaultProtocol.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStroke() throws Exception {
        // Get the stroke
        restStrokeMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/strokes/{id}", defaultProtocol.getProjectId(), defaultProtocol.getId(), Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStroke() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        int databaseSizeBeforeUpdate = strokeRepository.findAll().size();

        // Update the stroke
        Stroke updatedStroke = strokeRepository.findById(stroke.getId()).get();
        // Disconnect from session so that the updates on updatedStroke are not directly saved in db
        em.detach(updatedStroke);
        updatedStroke
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME);
        StrokeDTO strokeDTO = strokeMapper.toDto(updatedStroke);

        restStrokeMockMvc.perform(put("/api/projects/{projectId}/protocols/{protocolId}/strokes", defaultProtocol.getProjectId(), defaultProtocol.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(strokeDTO)))
            .andExpect(status().isOk());

        // Validate the Stroke in the database
        List<Stroke> strokeList = strokeRepository.findAll();
        assertThat(strokeList).hasSize(databaseSizeBeforeUpdate);
        Stroke testStroke = strokeList.get(strokeList.size() - 1);
        assertThat(testStroke.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testStroke.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingStroke() throws Exception {
        int databaseSizeBeforeUpdate = strokeRepository.findAll().size();

        // Create the Stroke
        StrokeDTO strokeDTO = strokeMapper.toDto(stroke);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStrokeMockMvc.perform(put("/api/projects/{projectId}/protocols/{protocolId}/strokes", defaultProtocol.getProjectId(), defaultProtocol.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(strokeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Stroke in the database
        List<Stroke> strokeList = strokeRepository.findAll();
        assertThat(strokeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStroke() throws Exception {
        // Initialize the database
        strokeRepository.saveAndFlush(stroke);

        int databaseSizeBeforeDelete = strokeRepository.findAll().size();

        // Delete the stroke
        restStrokeMockMvc.perform(delete("/api/projects/{projectId}/protocols/{protocolId}/strokes/{id}", defaultProtocol.getProjectId(), defaultProtocol.getId(), stroke.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Stroke> strokeList = strokeRepository.findAll();
        assertThat(strokeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
