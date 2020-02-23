package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.SamplingApp;
import pt.up.hs.sampling.config.SecurityBeanOverrideConfiguration;
import pt.up.hs.sampling.domain.Annotation;
import pt.up.hs.sampling.domain.Text;
import pt.up.hs.sampling.repository.AnnotationRepository;
import pt.up.hs.sampling.service.AnnotationService;
import pt.up.hs.sampling.service.dto.AnnotationDTO;
import pt.up.hs.sampling.service.mapper.AnnotationMapper;
import pt.up.hs.sampling.web.rest.errors.ExceptionTranslator;
import pt.up.hs.sampling.service.dto.AnnotationCriteria;
import pt.up.hs.sampling.service.AnnotationQueryService;

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
 * Integration tests for the {@link AnnotationResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SamplingApp.class})
public class AnnotationResourceIT {

    private static final Long DEFAULT_TYPE = 1L;
    private static final Long UPDATED_TYPE = 2L;
    private static final Long SMALLER_TYPE = 1L - 1L;

    private static final Integer DEFAULT_START = 1;
    private static final Integer UPDATED_START = 2;
    private static final Integer SMALLER_START = 1 - 1;

    private static final Integer DEFAULT_SIZE = 1;
    private static final Integer UPDATED_SIZE = 2;
    private static final Integer SMALLER_SIZE = 1 - 1;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    @Autowired
    private AnnotationRepository annotationRepository;

    @Autowired
    private AnnotationMapper annotationMapper;

    @Autowired
    private AnnotationService annotationService;

    @Autowired
    private AnnotationQueryService annotationQueryService;

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

    private MockMvc restAnnotationMockMvc;

    private Annotation annotation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnnotationResource annotationResource = new AnnotationResource(annotationService, annotationQueryService);
        this.restAnnotationMockMvc = MockMvcBuilders.standaloneSetup(annotationResource)
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
    public static Annotation createEntity(EntityManager em) {
        Annotation annotation = new Annotation()
            .type(DEFAULT_TYPE)
            .start(DEFAULT_START)
            .size(DEFAULT_SIZE)
            .note(DEFAULT_NOTE);
        // Add required entity
        Text text;
        if (TestUtil.findAll(em, Text.class).isEmpty()) {
            text = TextResourceIT.createEntity(em);
            em.persist(text);
            em.flush();
        } else {
            text = TestUtil.findAll(em, Text.class).get(0);
        }
        annotation.setText(text);
        return annotation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Annotation createUpdatedEntity(EntityManager em) {
        Annotation annotation = new Annotation()
            .type(UPDATED_TYPE)
            .start(UPDATED_START)
            .size(UPDATED_SIZE)
            .note(UPDATED_NOTE);
        // Add required entity
        Text text;
        if (TestUtil.findAll(em, Text.class).isEmpty()) {
            text = TextResourceIT.createUpdatedEntity(em);
            em.persist(text);
            em.flush();
        } else {
            text = TestUtil.findAll(em, Text.class).get(0);
        }
        annotation.setText(text);
        return annotation;
    }

    @BeforeEach
    public void initTest() {
        annotation = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnnotation() throws Exception {
        int databaseSizeBeforeCreate = annotationRepository.findAll().size();

        // Create the Annotation
        AnnotationDTO annotationDTO = annotationMapper.toDto(annotation);
        restAnnotationMockMvc.perform(post("/api/annotations")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(annotationDTO)))
            .andExpect(status().isCreated());

        // Validate the Annotation in the database
        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeCreate + 1);
        Annotation testAnnotation = annotationList.get(annotationList.size() - 1);
        assertThat(testAnnotation.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAnnotation.getStart()).isEqualTo(DEFAULT_START);
        assertThat(testAnnotation.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testAnnotation.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    public void createAnnotationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = annotationRepository.findAll().size();

        // Create the Annotation with an existing ID
        annotation.setId(1L);
        AnnotationDTO annotationDTO = annotationMapper.toDto(annotation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnotationMockMvc.perform(post("/api/annotations")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(annotationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Annotation in the database
        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = annotationRepository.findAll().size();
        // set the field null
        annotation.setType(null);

        // Create the Annotation, which fails.
        AnnotationDTO annotationDTO = annotationMapper.toDto(annotation);

        restAnnotationMockMvc.perform(post("/api/annotations")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(annotationDTO)))
            .andExpect(status().isBadRequest());

        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStartIsRequired() throws Exception {
        int databaseSizeBeforeTest = annotationRepository.findAll().size();
        // set the field null
        annotation.setStart(null);

        // Create the Annotation, which fails.
        AnnotationDTO annotationDTO = annotationMapper.toDto(annotation);

        restAnnotationMockMvc.perform(post("/api/annotations")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(annotationDTO)))
            .andExpect(status().isBadRequest());

        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = annotationRepository.findAll().size();
        // set the field null
        annotation.setSize(null);

        // Create the Annotation, which fails.
        AnnotationDTO annotationDTO = annotationMapper.toDto(annotation);

        restAnnotationMockMvc.perform(post("/api/annotations")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(annotationDTO)))
            .andExpect(status().isBadRequest());

        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnnotations() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList
        restAnnotationMockMvc.perform(get("/api/annotations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annotation.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }
    
    @Test
    @Transactional
    public void getAnnotation() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get the annotation
        restAnnotationMockMvc.perform(get("/api/annotations/{id}", annotation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(annotation.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.intValue()))
            .andExpect(jsonPath("$.start").value(DEFAULT_START))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }


    @Test
    @Transactional
    public void getAnnotationsByIdFiltering() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        Long id = annotation.getId();

        defaultAnnotationShouldBeFound("id.equals=" + id);
        defaultAnnotationShouldNotBeFound("id.notEquals=" + id);

        defaultAnnotationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAnnotationShouldNotBeFound("id.greaterThan=" + id);

        defaultAnnotationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAnnotationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAnnotationsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where type equals to DEFAULT_TYPE
        defaultAnnotationShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the annotationList where type equals to UPDATED_TYPE
        defaultAnnotationShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where type not equals to DEFAULT_TYPE
        defaultAnnotationShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the annotationList where type not equals to UPDATED_TYPE
        defaultAnnotationShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultAnnotationShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the annotationList where type equals to UPDATED_TYPE
        defaultAnnotationShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where type is not null
        defaultAnnotationShouldBeFound("type.specified=true");

        // Get all the annotationList where type is null
        defaultAnnotationShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnnotationsByTypeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where type is greater than or equal to DEFAULT_TYPE
        defaultAnnotationShouldBeFound("type.greaterThanOrEqual=" + DEFAULT_TYPE);

        // Get all the annotationList where type is greater than or equal to UPDATED_TYPE
        defaultAnnotationShouldNotBeFound("type.greaterThanOrEqual=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByTypeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where type is less than or equal to DEFAULT_TYPE
        defaultAnnotationShouldBeFound("type.lessThanOrEqual=" + DEFAULT_TYPE);

        // Get all the annotationList where type is less than or equal to SMALLER_TYPE
        defaultAnnotationShouldNotBeFound("type.lessThanOrEqual=" + SMALLER_TYPE);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByTypeIsLessThanSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where type is less than DEFAULT_TYPE
        defaultAnnotationShouldNotBeFound("type.lessThan=" + DEFAULT_TYPE);

        // Get all the annotationList where type is less than UPDATED_TYPE
        defaultAnnotationShouldBeFound("type.lessThan=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByTypeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where type is greater than DEFAULT_TYPE
        defaultAnnotationShouldNotBeFound("type.greaterThan=" + DEFAULT_TYPE);

        // Get all the annotationList where type is greater than SMALLER_TYPE
        defaultAnnotationShouldBeFound("type.greaterThan=" + SMALLER_TYPE);
    }


    @Test
    @Transactional
    public void getAllAnnotationsByStartIsEqualToSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where start equals to DEFAULT_START
        defaultAnnotationShouldBeFound("start.equals=" + DEFAULT_START);

        // Get all the annotationList where start equals to UPDATED_START
        defaultAnnotationShouldNotBeFound("start.equals=" + UPDATED_START);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByStartIsNotEqualToSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where start not equals to DEFAULT_START
        defaultAnnotationShouldNotBeFound("start.notEquals=" + DEFAULT_START);

        // Get all the annotationList where start not equals to UPDATED_START
        defaultAnnotationShouldBeFound("start.notEquals=" + UPDATED_START);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByStartIsInShouldWork() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where start in DEFAULT_START or UPDATED_START
        defaultAnnotationShouldBeFound("start.in=" + DEFAULT_START + "," + UPDATED_START);

        // Get all the annotationList where start equals to UPDATED_START
        defaultAnnotationShouldNotBeFound("start.in=" + UPDATED_START);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where start is not null
        defaultAnnotationShouldBeFound("start.specified=true");

        // Get all the annotationList where start is null
        defaultAnnotationShouldNotBeFound("start.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnnotationsByStartIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where start is greater than or equal to DEFAULT_START
        defaultAnnotationShouldBeFound("start.greaterThanOrEqual=" + DEFAULT_START);

        // Get all the annotationList where start is greater than or equal to UPDATED_START
        defaultAnnotationShouldNotBeFound("start.greaterThanOrEqual=" + UPDATED_START);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByStartIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where start is less than or equal to DEFAULT_START
        defaultAnnotationShouldBeFound("start.lessThanOrEqual=" + DEFAULT_START);

        // Get all the annotationList where start is less than or equal to SMALLER_START
        defaultAnnotationShouldNotBeFound("start.lessThanOrEqual=" + SMALLER_START);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByStartIsLessThanSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where start is less than DEFAULT_START
        defaultAnnotationShouldNotBeFound("start.lessThan=" + DEFAULT_START);

        // Get all the annotationList where start is less than UPDATED_START
        defaultAnnotationShouldBeFound("start.lessThan=" + UPDATED_START);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByStartIsGreaterThanSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where start is greater than DEFAULT_START
        defaultAnnotationShouldNotBeFound("start.greaterThan=" + DEFAULT_START);

        // Get all the annotationList where start is greater than SMALLER_START
        defaultAnnotationShouldBeFound("start.greaterThan=" + SMALLER_START);
    }


    @Test
    @Transactional
    public void getAllAnnotationsBySizeIsEqualToSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where size equals to DEFAULT_SIZE
        defaultAnnotationShouldBeFound("size.equals=" + DEFAULT_SIZE);

        // Get all the annotationList where size equals to UPDATED_SIZE
        defaultAnnotationShouldNotBeFound("size.equals=" + UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void getAllAnnotationsBySizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where size not equals to DEFAULT_SIZE
        defaultAnnotationShouldNotBeFound("size.notEquals=" + DEFAULT_SIZE);

        // Get all the annotationList where size not equals to UPDATED_SIZE
        defaultAnnotationShouldBeFound("size.notEquals=" + UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void getAllAnnotationsBySizeIsInShouldWork() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where size in DEFAULT_SIZE or UPDATED_SIZE
        defaultAnnotationShouldBeFound("size.in=" + DEFAULT_SIZE + "," + UPDATED_SIZE);

        // Get all the annotationList where size equals to UPDATED_SIZE
        defaultAnnotationShouldNotBeFound("size.in=" + UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void getAllAnnotationsBySizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where size is not null
        defaultAnnotationShouldBeFound("size.specified=true");

        // Get all the annotationList where size is null
        defaultAnnotationShouldNotBeFound("size.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnnotationsBySizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where size is greater than or equal to DEFAULT_SIZE
        defaultAnnotationShouldBeFound("size.greaterThanOrEqual=" + DEFAULT_SIZE);

        // Get all the annotationList where size is greater than or equal to UPDATED_SIZE
        defaultAnnotationShouldNotBeFound("size.greaterThanOrEqual=" + UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void getAllAnnotationsBySizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where size is less than or equal to DEFAULT_SIZE
        defaultAnnotationShouldBeFound("size.lessThanOrEqual=" + DEFAULT_SIZE);

        // Get all the annotationList where size is less than or equal to SMALLER_SIZE
        defaultAnnotationShouldNotBeFound("size.lessThanOrEqual=" + SMALLER_SIZE);
    }

    @Test
    @Transactional
    public void getAllAnnotationsBySizeIsLessThanSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where size is less than DEFAULT_SIZE
        defaultAnnotationShouldNotBeFound("size.lessThan=" + DEFAULT_SIZE);

        // Get all the annotationList where size is less than UPDATED_SIZE
        defaultAnnotationShouldBeFound("size.lessThan=" + UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void getAllAnnotationsBySizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where size is greater than DEFAULT_SIZE
        defaultAnnotationShouldNotBeFound("size.greaterThan=" + DEFAULT_SIZE);

        // Get all the annotationList where size is greater than SMALLER_SIZE
        defaultAnnotationShouldBeFound("size.greaterThan=" + SMALLER_SIZE);
    }


    @Test
    @Transactional
    public void getAllAnnotationsByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where note equals to DEFAULT_NOTE
        defaultAnnotationShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the annotationList where note equals to UPDATED_NOTE
        defaultAnnotationShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where note not equals to DEFAULT_NOTE
        defaultAnnotationShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the annotationList where note not equals to UPDATED_NOTE
        defaultAnnotationShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultAnnotationShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the annotationList where note equals to UPDATED_NOTE
        defaultAnnotationShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where note is not null
        defaultAnnotationShouldBeFound("note.specified=true");

        // Get all the annotationList where note is null
        defaultAnnotationShouldNotBeFound("note.specified=false");
    }
                @Test
    @Transactional
    public void getAllAnnotationsByNoteContainsSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where note contains DEFAULT_NOTE
        defaultAnnotationShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the annotationList where note contains UPDATED_NOTE
        defaultAnnotationShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllAnnotationsByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where note does not contain DEFAULT_NOTE
        defaultAnnotationShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the annotationList where note does not contain UPDATED_NOTE
        defaultAnnotationShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }


    @Test
    @Transactional
    public void getAllAnnotationsByTextIsEqualToSomething() throws Exception {
        // Get already existing entity
        Text text = annotation.getText();
        annotationRepository.saveAndFlush(annotation);
        Long textId = text.getId();

        // Get all the annotationList where text equals to textId
        defaultAnnotationShouldBeFound("textId.equals=" + textId);

        // Get all the annotationList where text equals to textId + 1
        defaultAnnotationShouldNotBeFound("textId.equals=" + (textId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnnotationShouldBeFound(String filter) throws Exception {
        restAnnotationMockMvc.perform(get("/api/annotations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annotation.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restAnnotationMockMvc.perform(get("/api/annotations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnnotationShouldNotBeFound(String filter) throws Exception {
        restAnnotationMockMvc.perform(get("/api/annotations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnnotationMockMvc.perform(get("/api/annotations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAnnotation() throws Exception {
        // Get the annotation
        restAnnotationMockMvc.perform(get("/api/annotations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnnotation() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        int databaseSizeBeforeUpdate = annotationRepository.findAll().size();

        // Update the annotation
        Annotation updatedAnnotation = annotationRepository.findById(annotation.getId()).get();
        // Disconnect from session so that the updates on updatedAnnotation are not directly saved in db
        em.detach(updatedAnnotation);
        updatedAnnotation
            .type(UPDATED_TYPE)
            .start(UPDATED_START)
            .size(UPDATED_SIZE)
            .note(UPDATED_NOTE);
        AnnotationDTO annotationDTO = annotationMapper.toDto(updatedAnnotation);

        restAnnotationMockMvc.perform(put("/api/annotations")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(annotationDTO)))
            .andExpect(status().isOk());

        // Validate the Annotation in the database
        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeUpdate);
        Annotation testAnnotation = annotationList.get(annotationList.size() - 1);
        assertThat(testAnnotation.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAnnotation.getStart()).isEqualTo(UPDATED_START);
        assertThat(testAnnotation.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testAnnotation.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void updateNonExistingAnnotation() throws Exception {
        int databaseSizeBeforeUpdate = annotationRepository.findAll().size();

        // Create the Annotation
        AnnotationDTO annotationDTO = annotationMapper.toDto(annotation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnotationMockMvc.perform(put("/api/annotations")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(annotationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Annotation in the database
        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAnnotation() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        int databaseSizeBeforeDelete = annotationRepository.findAll().size();

        // Delete the annotation
        restAnnotationMockMvc.perform(delete("/api/annotations/{id}", annotation.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
