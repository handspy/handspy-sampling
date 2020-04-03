package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.SamplingApp;
import pt.up.hs.sampling.config.SecurityBeanOverrideConfiguration;
import pt.up.hs.sampling.domain.Dot;
import pt.up.hs.sampling.domain.Protocol;
import pt.up.hs.sampling.domain.Stroke;
import pt.up.hs.sampling.repository.DotRepository;
import pt.up.hs.sampling.service.DotService;
import pt.up.hs.sampling.service.dto.DotDTO;
import pt.up.hs.sampling.service.mapper.DotMapper;
import pt.up.hs.sampling.web.rest.errors.ExceptionTranslator;
import pt.up.hs.sampling.service.DotQueryService;

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

import pt.up.hs.sampling.domain.enumeration.DotType;

/**
 * Integration tests for the {@link DotResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SamplingApp.class})
public class DotResourceIT {

    private static final Long DEFAULT_TIMESTAMP = 1L;
    private static final Long UPDATED_TIMESTAMP = 2L;

    private static final Double DEFAULT_X = 1D;
    private static final Double UPDATED_X = 2D;
    private static final Double SMALLER_X = 1D - 1D;

    private static final Double DEFAULT_Y = 1D;
    private static final Double UPDATED_Y = 2D;
    private static final Double SMALLER_Y = 1D - 1D;

    private static final DotType DEFAULT_TYPE = DotType.DOWN;
    private static final DotType UPDATED_TYPE = DotType.MOVE;

    private static final Double DEFAULT_PRESSURE = 1D;
    private static final Double UPDATED_PRESSURE = 2D;
    private static final Double SMALLER_PRESSURE = 1D - 1D;

    @Autowired
    private DotRepository dotRepository;

    @Autowired
    private DotMapper dotMapper;

    @Autowired
    private DotService dotService;

    @Autowired
    private DotQueryService dotQueryService;

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

    private MockMvc restDotMockMvc;

    private static Stroke defaultStroke;

    private Dot dot;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DotResource dotResource = new DotResource(dotService, dotQueryService);
        this.restDotMockMvc = MockMvcBuilders.standaloneSetup(dotResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dot createEntity(EntityManager em) {
        return new Dot()
            .timestamp(DEFAULT_TIMESTAMP)
            .x(DEFAULT_X)
            .y(DEFAULT_Y)
            .type(DEFAULT_TYPE)
            .pressure(DEFAULT_PRESSURE)
            .stroke(defaultStroke);
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dot createUpdatedEntity(EntityManager em) {
        return new Dot()
            .timestamp(UPDATED_TIMESTAMP)
            .x(UPDATED_X)
            .y(UPDATED_Y)
            .type(UPDATED_TYPE)
            .pressure(UPDATED_PRESSURE)
            .stroke(defaultStroke);
    }

    public static Stroke getStroke(EntityManager em) {
        Stroke stroke;
        if (TestUtil.findAll(em, Stroke.class).isEmpty()) {
            stroke = StrokeResourceIT.createEntity(em, getProtocol(em));
            em.persist(stroke);
            em.flush();
        } else {
            stroke = TestUtil.findAll(em, Stroke.class).get(0);
        }
        return stroke;
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
        defaultStroke = getStroke(em);
        dot = createEntity(em);
    }

    @Test
    @Transactional
    public void createDot() throws Exception {
        int databaseSizeBeforeCreate = dotRepository.findAll().size();

        // Create the Dot
        DotDTO dotDTO = dotMapper.toDto(dot);
        restDotMockMvc.perform(post("/api/projects/{projectId}/protocols/{protocolId}/strokes/{strokeId}/dots", defaultStroke.getProtocol().getProjectId(), defaultStroke.getProtocol().getId(), defaultStroke.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dotDTO)))
            .andExpect(status().isCreated());

        // Validate the Dot in the database
        List<Dot> dotList = dotRepository.findAll();
        assertThat(dotList).hasSize(databaseSizeBeforeCreate + 1);
        Dot testDot = dotList.get(dotList.size() - 1);
        assertThat(testDot.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testDot.getX()).isEqualTo(DEFAULT_X);
        assertThat(testDot.getY()).isEqualTo(DEFAULT_Y);
        assertThat(testDot.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDot.getPressure()).isEqualTo(DEFAULT_PRESSURE);
    }

    @Test
    @Transactional
    public void createDotWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dotRepository.findAll().size();

        // Create the Dot with an existing ID
        dot.setId(1L);
        DotDTO dotDTO = dotMapper.toDto(dot);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDotMockMvc.perform(post("/api/projects/{projectId}/protocols/{protocolId}/strokes/{strokeId}/dots", defaultStroke.getProtocol().getProjectId(), defaultStroke.getProtocol().getId(), defaultStroke.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dotDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dot in the database
        List<Dot> dotList = dotRepository.findAll();
        assertThat(dotList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = dotRepository.findAll().size();
        // set the field null
        dot.setTimestamp(null);

        // Create the Dot, which fails.
        DotDTO dotDTO = dotMapper.toDto(dot);

        restDotMockMvc.perform(post("/api/projects/{projectId}/protocols/{protocolId}/strokes/{strokeId}/dots", defaultStroke.getProtocol().getProjectId(), defaultStroke.getProtocol().getId(), defaultStroke.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dotDTO)))
            .andExpect(status().isBadRequest());

        List<Dot> dotList = dotRepository.findAll();
        assertThat(dotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkXIsRequired() throws Exception {
        int databaseSizeBeforeTest = dotRepository.findAll().size();
        // set the field null
        dot.setX(null);

        // Create the Dot, which fails.
        DotDTO dotDTO = dotMapper.toDto(dot);

        restDotMockMvc.perform(post("/api/projects/{projectId}/protocols/{protocolId}/strokes/{strokeId}/dots", defaultStroke.getProtocol().getProjectId(), defaultStroke.getProtocol().getId(), defaultStroke.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dotDTO)))
            .andExpect(status().isBadRequest());

        List<Dot> dotList = dotRepository.findAll();
        assertThat(dotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYIsRequired() throws Exception {
        int databaseSizeBeforeTest = dotRepository.findAll().size();
        // set the field null
        dot.setY(null);

        // Create the Dot, which fails.
        DotDTO dotDTO = dotMapper.toDto(dot);

        restDotMockMvc.perform(post("/api/projects/{projectId}/protocols/{protocolId}/strokes/{strokeId}/dots", defaultStroke.getProtocol().getProjectId(), defaultStroke.getProtocol().getId(), defaultStroke.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dotDTO)))
            .andExpect(status().isBadRequest());

        List<Dot> dotList = dotRepository.findAll();
        assertThat(dotList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDots() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList
        restDotMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/strokes/{strokeId}/dots?sort=id,desc", defaultStroke.getProtocol().getProjectId(), defaultStroke.getProtocol().getId(), defaultStroke.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dot.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.intValue())))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X)))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].pressure").value(hasItem(DEFAULT_PRESSURE)));
    }

    @Test
    @Transactional
    public void getDot() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get the dot
        restDotMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/strokes/{strokeId}/dots/{id}", defaultStroke.getProtocol().getProjectId(), defaultStroke.getProtocol().getId(), defaultStroke.getId(), dot.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dot.getId().intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.x").value(DEFAULT_X))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.pressure").value(DEFAULT_PRESSURE));
    }

    @Test
    @Transactional
    public void getDotsByIdFiltering() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        Long id = dot.getId();

        defaultDotShouldBeFound("id.equals=" + id);
        defaultDotShouldNotBeFound("id.notEquals=" + id);

        defaultDotShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDotShouldNotBeFound("id.greaterThan=" + id);

        defaultDotShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDotShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDotsByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where timestamp equals to DEFAULT_TIMESTAMP
        defaultDotShouldBeFound("timestamp.equals=" + DEFAULT_TIMESTAMP);

        // Get all the dotList where timestamp equals to UPDATED_TIMESTAMP
        defaultDotShouldNotBeFound("timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllDotsByTimestampIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where timestamp not equals to DEFAULT_TIMESTAMP
        defaultDotShouldNotBeFound("timestamp.notEquals=" + DEFAULT_TIMESTAMP);

        // Get all the dotList where timestamp not equals to UPDATED_TIMESTAMP
        defaultDotShouldBeFound("timestamp.notEquals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllDotsByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where timestamp in DEFAULT_TIMESTAMP or UPDATED_TIMESTAMP
        defaultDotShouldBeFound("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP);

        // Get all the dotList where timestamp equals to UPDATED_TIMESTAMP
        defaultDotShouldNotBeFound("timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    public void getAllDotsByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where timestamp is not null
        defaultDotShouldBeFound("timestamp.specified=true");

        // Get all the dotList where timestamp is null
        defaultDotShouldNotBeFound("timestamp.specified=false");
    }

    @Test
    @Transactional
    public void getAllDotsByXIsEqualToSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where x equals to DEFAULT_X
        defaultDotShouldBeFound("x.equals=" + DEFAULT_X);

        // Get all the dotList where x equals to UPDATED_X
        defaultDotShouldNotBeFound("x.equals=" + UPDATED_X);
    }

    @Test
    @Transactional
    public void getAllDotsByXIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where x not equals to DEFAULT_X
        defaultDotShouldNotBeFound("x.notEquals=" + DEFAULT_X);

        // Get all the dotList where x not equals to UPDATED_X
        defaultDotShouldBeFound("x.notEquals=" + UPDATED_X);
    }

    @Test
    @Transactional
    public void getAllDotsByXIsInShouldWork() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where x in DEFAULT_X or UPDATED_X
        defaultDotShouldBeFound("x.in=" + DEFAULT_X + "," + UPDATED_X);

        // Get all the dotList where x equals to UPDATED_X
        defaultDotShouldNotBeFound("x.in=" + UPDATED_X);
    }

    @Test
    @Transactional
    public void getAllDotsByXIsNullOrNotNull() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where x is not null
        defaultDotShouldBeFound("x.specified=true");

        // Get all the dotList where x is null
        defaultDotShouldNotBeFound("x.specified=false");
    }

    @Test
    @Transactional
    public void getAllDotsByXIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where x is greater than or equal to DEFAULT_X
        defaultDotShouldBeFound("x.greaterThanOrEqual=" + DEFAULT_X);

        // Get all the dotList where x is greater than or equal to UPDATED_X
        defaultDotShouldNotBeFound("x.greaterThanOrEqual=" + UPDATED_X);
    }

    @Test
    @Transactional
    public void getAllDotsByXIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where x is less than or equal to DEFAULT_X
        defaultDotShouldBeFound("x.lessThanOrEqual=" + DEFAULT_X);

        // Get all the dotList where x is less than or equal to SMALLER_X
        defaultDotShouldNotBeFound("x.lessThanOrEqual=" + SMALLER_X);
    }

    @Test
    @Transactional
    public void getAllDotsByXIsLessThanSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where x is less than DEFAULT_X
        defaultDotShouldNotBeFound("x.lessThan=" + DEFAULT_X);

        // Get all the dotList where x is less than UPDATED_X
        defaultDotShouldBeFound("x.lessThan=" + UPDATED_X);
    }

    @Test
    @Transactional
    public void getAllDotsByXIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where x is greater than DEFAULT_X
        defaultDotShouldNotBeFound("x.greaterThan=" + DEFAULT_X);

        // Get all the dotList where x is greater than SMALLER_X
        defaultDotShouldBeFound("x.greaterThan=" + SMALLER_X);
    }


    @Test
    @Transactional
    public void getAllDotsByYIsEqualToSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where y equals to DEFAULT_Y
        defaultDotShouldBeFound("y.equals=" + DEFAULT_Y);

        // Get all the dotList where y equals to UPDATED_Y
        defaultDotShouldNotBeFound("y.equals=" + UPDATED_Y);
    }

    @Test
    @Transactional
    public void getAllDotsByYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where y not equals to DEFAULT_Y
        defaultDotShouldNotBeFound("y.notEquals=" + DEFAULT_Y);

        // Get all the dotList where y not equals to UPDATED_Y
        defaultDotShouldBeFound("y.notEquals=" + UPDATED_Y);
    }

    @Test
    @Transactional
    public void getAllDotsByYIsInShouldWork() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where y in DEFAULT_Y or UPDATED_Y
        defaultDotShouldBeFound("y.in=" + DEFAULT_Y + "," + UPDATED_Y);

        // Get all the dotList where y equals to UPDATED_Y
        defaultDotShouldNotBeFound("y.in=" + UPDATED_Y);
    }

    @Test
    @Transactional
    public void getAllDotsByYIsNullOrNotNull() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where y is not null
        defaultDotShouldBeFound("y.specified=true");

        // Get all the dotList where y is null
        defaultDotShouldNotBeFound("y.specified=false");
    }

    @Test
    @Transactional
    public void getAllDotsByYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where y is greater than or equal to DEFAULT_Y
        defaultDotShouldBeFound("y.greaterThanOrEqual=" + DEFAULT_Y);

        // Get all the dotList where y is greater than or equal to UPDATED_Y
        defaultDotShouldNotBeFound("y.greaterThanOrEqual=" + UPDATED_Y);
    }

    @Test
    @Transactional
    public void getAllDotsByYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where y is less than or equal to DEFAULT_Y
        defaultDotShouldBeFound("y.lessThanOrEqual=" + DEFAULT_Y);

        // Get all the dotList where y is less than or equal to SMALLER_Y
        defaultDotShouldNotBeFound("y.lessThanOrEqual=" + SMALLER_Y);
    }

    @Test
    @Transactional
    public void getAllDotsByYIsLessThanSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where y is less than DEFAULT_Y
        defaultDotShouldNotBeFound("y.lessThan=" + DEFAULT_Y);

        // Get all the dotList where y is less than UPDATED_Y
        defaultDotShouldBeFound("y.lessThan=" + UPDATED_Y);
    }

    @Test
    @Transactional
    public void getAllDotsByYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where y is greater than DEFAULT_Y
        defaultDotShouldNotBeFound("y.greaterThan=" + DEFAULT_Y);

        // Get all the dotList where y is greater than SMALLER_Y
        defaultDotShouldBeFound("y.greaterThan=" + SMALLER_Y);
    }


    @Test
    @Transactional
    public void getAllDotsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where type equals to DEFAULT_TYPE
        defaultDotShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the dotList where type equals to UPDATED_TYPE
        defaultDotShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllDotsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where type not equals to DEFAULT_TYPE
        defaultDotShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the dotList where type not equals to UPDATED_TYPE
        defaultDotShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllDotsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultDotShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the dotList where type equals to UPDATED_TYPE
        defaultDotShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllDotsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where type is not null
        defaultDotShouldBeFound("type.specified=true");

        // Get all the dotList where type is null
        defaultDotShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllDotsByPressureIsEqualToSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where pressure equals to DEFAULT_PRESSURE
        defaultDotShouldBeFound("pressure.equals=" + DEFAULT_PRESSURE);

        // Get all the dotList where pressure equals to UPDATED_PRESSURE
        defaultDotShouldNotBeFound("pressure.equals=" + UPDATED_PRESSURE);
    }

    @Test
    @Transactional
    public void getAllDotsByPressureIsNotEqualToSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where pressure not equals to DEFAULT_PRESSURE
        defaultDotShouldNotBeFound("pressure.notEquals=" + DEFAULT_PRESSURE);

        // Get all the dotList where pressure not equals to UPDATED_PRESSURE
        defaultDotShouldBeFound("pressure.notEquals=" + UPDATED_PRESSURE);
    }

    @Test
    @Transactional
    public void getAllDotsByPressureIsInShouldWork() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where pressure in DEFAULT_PRESSURE or UPDATED_PRESSURE
        defaultDotShouldBeFound("pressure.in=" + DEFAULT_PRESSURE + "," + UPDATED_PRESSURE);

        // Get all the dotList where pressure equals to UPDATED_PRESSURE
        defaultDotShouldNotBeFound("pressure.in=" + UPDATED_PRESSURE);
    }

    @Test
    @Transactional
    public void getAllDotsByPressureIsNullOrNotNull() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where pressure is not null
        defaultDotShouldBeFound("pressure.specified=true");

        // Get all the dotList where pressure is null
        defaultDotShouldNotBeFound("pressure.specified=false");
    }

    @Test
    @Transactional
    public void getAllDotsByPressureIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where pressure is greater than or equal to DEFAULT_PRESSURE
        defaultDotShouldBeFound("pressure.greaterThanOrEqual=" + DEFAULT_PRESSURE);

        // Get all the dotList where pressure is greater than or equal to UPDATED_PRESSURE
        defaultDotShouldNotBeFound("pressure.greaterThanOrEqual=" + UPDATED_PRESSURE);
    }

    @Test
    @Transactional
    public void getAllDotsByPressureIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where pressure is less than or equal to DEFAULT_PRESSURE
        defaultDotShouldBeFound("pressure.lessThanOrEqual=" + DEFAULT_PRESSURE);

        // Get all the dotList where pressure is less than or equal to SMALLER_PRESSURE
        defaultDotShouldNotBeFound("pressure.lessThanOrEqual=" + SMALLER_PRESSURE);
    }

    @Test
    @Transactional
    public void getAllDotsByPressureIsLessThanSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where pressure is less than DEFAULT_PRESSURE
        defaultDotShouldNotBeFound("pressure.lessThan=" + DEFAULT_PRESSURE);

        // Get all the dotList where pressure is less than UPDATED_PRESSURE
        defaultDotShouldBeFound("pressure.lessThan=" + UPDATED_PRESSURE);
    }

    @Test
    @Transactional
    public void getAllDotsByPressureIsGreaterThanSomething() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        // Get all the dotList where pressure is greater than DEFAULT_PRESSURE
        defaultDotShouldNotBeFound("pressure.greaterThan=" + DEFAULT_PRESSURE);

        // Get all the dotList where pressure is greater than SMALLER_PRESSURE
        defaultDotShouldBeFound("pressure.greaterThan=" + SMALLER_PRESSURE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDotShouldBeFound(String filter) throws Exception {
        restDotMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/strokes/{strokeId}/dots?sort=id,desc&" + filter, defaultStroke.getProtocol().getProjectId(), defaultStroke.getProtocol().getId(), defaultStroke.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dot.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.intValue())))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X)))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].pressure").value(hasItem(DEFAULT_PRESSURE)));

        // Check, that the count call also returns 1
        restDotMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/strokes/{strokeId}/dots/count?sort=id,desc&" + filter, defaultStroke.getProtocol().getProjectId(), defaultStroke.getProtocol().getId(), defaultStroke.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDotShouldNotBeFound(String filter) throws Exception {
        restDotMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/strokes/{strokeId}/dots?sort=id,desc&" + filter, defaultStroke.getProtocol().getProjectId(), defaultStroke.getProtocol().getId(), defaultStroke.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDotMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/strokes/{strokeId}/dots/count?sort=id,desc&" + filter, defaultStroke.getProtocol().getProjectId(), defaultStroke.getProtocol().getId(), defaultStroke.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDot() throws Exception {
        // Get the dot
        restDotMockMvc.perform(get("/api/projects/{projectId}/protocols/{protocolId}/strokes/{strokeId}/dots/{id}", defaultStroke.getProtocol().getProjectId(), defaultStroke.getProtocol().getId(), defaultStroke.getId(), Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDot() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        int databaseSizeBeforeUpdate = dotRepository.findAll().size();

        // Update the dot
        Dot updatedDot = dotRepository.findById(dot.getId()).get();
        // Disconnect from session so that the updates on updatedDot are not directly saved in db
        em.detach(updatedDot);
        updatedDot
            .timestamp(UPDATED_TIMESTAMP)
            .x(UPDATED_X)
            .y(UPDATED_Y)
            .type(UPDATED_TYPE)
            .pressure(UPDATED_PRESSURE);
        DotDTO dotDTO = dotMapper.toDto(updatedDot);

        restDotMockMvc.perform(put("/api/projects/{projectId}/protocols/{protocolId}/strokes/{strokeId}/dots", defaultStroke.getProtocol().getProjectId(), defaultStroke.getProtocol().getId(), defaultStroke.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dotDTO)))
            .andExpect(status().isOk());

        // Validate the Dot in the database
        List<Dot> dotList = dotRepository.findAll();
        assertThat(dotList).hasSize(databaseSizeBeforeUpdate);
        Dot testDot = dotList.get(dotList.size() - 1);
        assertThat(testDot.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testDot.getX()).isEqualTo(UPDATED_X);
        assertThat(testDot.getY()).isEqualTo(UPDATED_Y);
        assertThat(testDot.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDot.getPressure()).isEqualTo(UPDATED_PRESSURE);
    }

    @Test
    @Transactional
    public void updateNonExistingDot() throws Exception {
        int databaseSizeBeforeUpdate = dotRepository.findAll().size();

        // Create the Dot
        DotDTO dotDTO = dotMapper.toDto(dot);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDotMockMvc.perform(put("/api/projects/{projectId}/protocols/{protocolId}/strokes/{strokeId}/dots", defaultStroke.getProtocol().getProjectId(), defaultStroke.getProtocol().getId(), defaultStroke.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(dotDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dot in the database
        List<Dot> dotList = dotRepository.findAll();
        assertThat(dotList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDot() throws Exception {
        // Initialize the database
        dotRepository.saveAndFlush(dot);

        int databaseSizeBeforeDelete = dotRepository.findAll().size();

        // Delete the dot
        restDotMockMvc.perform(delete("/api/projects/{projectId}/protocols/{protocolId}/strokes/{strokeId}/dots/{id}", defaultStroke.getProtocol().getProjectId(), defaultStroke.getProtocol().getId(), defaultStroke.getId(), dot.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Dot> dotList = dotRepository.findAll();
        assertThat(dotList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
