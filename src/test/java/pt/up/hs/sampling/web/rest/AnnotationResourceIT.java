package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.SamplingApp;
import pt.up.hs.sampling.config.SecurityBeanOverrideConfiguration;
import pt.up.hs.sampling.domain.Annotation;
import pt.up.hs.sampling.domain.AnnotationType;
import pt.up.hs.sampling.domain.Text;
import pt.up.hs.sampling.repository.AnnotationRepository;
import pt.up.hs.sampling.service.AnnotationService;
import pt.up.hs.sampling.service.dto.AnnotationDTO;
import pt.up.hs.sampling.service.mapper.AnnotationMapper;
import pt.up.hs.sampling.web.rest.errors.ExceptionTranslator;
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

    private static final Long DEFAULT_PROJECT_ID = 1L;
    private static final Long OTHER_PROJECT_ID = 2L;

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

    private static AnnotationType defaultAnnotationType;
    private static AnnotationType updatedAnnotationType;
    private static Text defaultText;

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
    public static Annotation createEntity() {
        AnnotationType annotationType = new AnnotationType();
        annotationType.setId(defaultAnnotationType.getId());
        return new Annotation()
            .text(defaultText)
            .annotationType(annotationType)
            .start(DEFAULT_START)
            .size(DEFAULT_SIZE)
            .note(DEFAULT_NOTE);
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Annotation createUpdatedEntity() {
        AnnotationType annotationType = new AnnotationType();
        annotationType.setId(updatedAnnotationType.getId());
        return new Annotation()
            .annotationType(annotationType)
            .text(defaultText)
            .start(UPDATED_START)
            .size(UPDATED_SIZE)
            .note(UPDATED_NOTE);
    }

    public static AnnotationType getAnnotationType(EntityManager em, boolean updated) {
        AnnotationType annotationType;
        if (updated) {
            annotationType = AnnotationTypeResourceIT.createUpdatedEntity();
        } else {
            annotationType = AnnotationTypeResourceIT.createEntity();
        }
        em.persist(annotationType);
        em.flush();
        return annotationType;
    }

    public static Text getText(EntityManager em, boolean updated) {
        Text text;
        if (TestUtil.findAll(em, Text.class).isEmpty()) {
            if (updated) {
                text = TextResourceIT.createUpdatedEntity(em);
            } else {
                text = TextResourceIT.createEntity(em);
            }
            em.persist(text);
            em.flush();
        } else {
            text = TestUtil.findAll(em, Text.class).get(0);
        }
        return text;
    }

    @BeforeEach
    public void initTest() {
        defaultAnnotationType = getAnnotationType(em, false);
        updatedAnnotationType = getAnnotationType(em, true);
        defaultText = getText(em, false);
        annotation = createEntity();
    }

    @Test
    @Transactional
    public void createAnnotation() throws Exception {
        int databaseSizeBeforeCreate = annotationRepository.findAll().size();

        // Create the Annotation
        AnnotationDTO annotationDTO = annotationMapper.toDto(annotation);
        restAnnotationMockMvc.perform(post("/api/projects/{projectId}/texts/{textId}/annotations", DEFAULT_PROJECT_ID, defaultText.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(annotationDTO)))
            .andExpect(status().isCreated());

        // Validate the Annotation in the database
        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeCreate + 1);
        Annotation testAnnotation = annotationList.get(annotationList.size() - 1);
        assertThat(testAnnotation.getAnnotationType().getId()).isEqualTo(defaultAnnotationType.getId());
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
        restAnnotationMockMvc.perform(post("/api/projects/{projectId}/texts/{textId}/annotations", DEFAULT_PROJECT_ID, defaultText.getId())
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
        annotation.setAnnotationType(null);

        // Create the Annotation, which fails.
        AnnotationDTO annotationDTO = annotationMapper.toDto(annotation);

        restAnnotationMockMvc.perform(post("/api/projects/{projectId}/texts/{textId}/annotations", DEFAULT_PROJECT_ID, defaultText.getId())
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

        restAnnotationMockMvc.perform(post("/api/projects/{projectId}/texts/{textId}/annotations", DEFAULT_PROJECT_ID, defaultText.getId())
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

        restAnnotationMockMvc.perform(post("/api/projects/{projectId}/texts/{textId}/annotations", DEFAULT_PROJECT_ID, defaultText.getId())
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
        restAnnotationMockMvc.perform(get("/api/projects/{projectId}/texts/{textId}/annotations?sort=id,desc", DEFAULT_PROJECT_ID, defaultText.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annotation.getId().intValue())))
            .andExpect(jsonPath("$.[*].annotationTypeId").value(hasItem(defaultAnnotationType.getId().intValue())))
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
        restAnnotationMockMvc.perform(get("/api/projects/{projectId}/texts/{textId}/annotations/{id}", DEFAULT_PROJECT_ID, defaultText.getId(), annotation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(annotation.getId().intValue()))
            .andExpect(jsonPath("$.annotationTypeId").value(defaultAnnotationType.getId().intValue()))
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
        defaultAnnotationShouldBeFound("annotationTypeId.equals=" + defaultAnnotationType.getId());

        // Get all the annotationList where type equals to UPDATED_TYPE
        defaultAnnotationShouldNotBeFound("annotationTypeId.equals=" + updatedAnnotationType.getId());
    }

    @Test
    @Transactional
    public void getAllAnnotationsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where type not equals to DEFAULT_TYPE
        defaultAnnotationShouldNotBeFound("annotationTypeId.notEquals=" + defaultAnnotationType.getId().intValue());

        // Get all the annotationList where type not equals to UPDATED_TYPE
        defaultAnnotationShouldBeFound("annotationTypeId.notEquals=" + updatedAnnotationType.getId());
    }

    @Test
    @Transactional
    public void getAllAnnotationsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultAnnotationShouldBeFound("annotationTypeId.in=" + defaultAnnotationType.getId() + "," + updatedAnnotationType.getId());

        // Get all the annotationList where type equals to UPDATED_TYPE
        defaultAnnotationShouldNotBeFound("annotationTypeId.in=" + updatedAnnotationType.getId());
    }

    @Test
    @Transactional
    public void getAllAnnotationsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        annotationRepository.saveAndFlush(annotation);

        // Get all the annotationList where type is not null
        defaultAnnotationShouldBeFound("annotationTypeId.specified=true");

        // Get all the annotationList where type is null
        defaultAnnotationShouldNotBeFound("annotationTypeId.specified=false");
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


    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnnotationShouldBeFound(String filter) throws Exception {
        restAnnotationMockMvc.perform(get("/api/projects/{projectId}/texts/{textId}/annotations?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, defaultText.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annotation.getId().intValue())))
            .andExpect(jsonPath("$.[*].annotationTypeId").value(hasItem(defaultAnnotationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].start").value(hasItem(DEFAULT_START)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));

        // Check, that the count call also returns 1
        restAnnotationMockMvc.perform(get("/api/projects/{projectId}/texts/{textId}/annotations/count?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, defaultText.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnnotationShouldNotBeFound(String filter) throws Exception {
        restAnnotationMockMvc.perform(get("/api/projects/{projectId}/texts/{textId}/annotations?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, defaultText.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnnotationMockMvc.perform(get("/api/projects/{projectId}/texts/{textId}/annotations/count?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, defaultText.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAnnotation() throws Exception {
        // Get the annotation
        restAnnotationMockMvc.perform(get("/api/projects/{projectId}/texts/{textId}/annotations/{id}", DEFAULT_PROJECT_ID, defaultText.getId(), Long.MAX_VALUE))
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
            .annotationType(updatedAnnotationType)
            .start(UPDATED_START)
            .size(UPDATED_SIZE)
            .note(UPDATED_NOTE);
        AnnotationDTO annotationDTO = annotationMapper.toDto(updatedAnnotation);

        restAnnotationMockMvc.perform(put("/api/projects/{projectId}/texts/{textId}/annotations", DEFAULT_PROJECT_ID, defaultText.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(annotationDTO)))
            .andExpect(status().isOk());

        // Validate the Annotation in the database
        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeUpdate);
        Annotation testAnnotation = annotationList.get(annotationList.size() - 1);
        assertThat(testAnnotation.getAnnotationType()).isEqualTo(updatedAnnotationType);
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
        restAnnotationMockMvc.perform(put("/api/projects/{projectId}/texts/{textId}/annotations", DEFAULT_PROJECT_ID, defaultText.getId())
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
        restAnnotationMockMvc.perform(delete("/api/projects/{projectId}/texts/{textId}/annotations/{id}", DEFAULT_PROJECT_ID, defaultText.getId(), annotation.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Annotation> annotationList = annotationRepository.findAll();
        assertThat(annotationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
