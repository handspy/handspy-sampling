package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.SamplingApp;
import pt.up.hs.sampling.config.SecurityBeanOverrideConfiguration;
import pt.up.hs.sampling.domain.AnnotationType;
import pt.up.hs.sampling.repository.AnnotationTypeRepository;
import pt.up.hs.sampling.service.AnnotationTypeService;
import pt.up.hs.sampling.service.dto.AnnotationTypeDTO;
import pt.up.hs.sampling.service.mapper.AnnotationTypeMapper;
import pt.up.hs.sampling.web.rest.errors.ExceptionTranslator;
import pt.up.hs.sampling.service.dto.AnnotationTypeCriteria;
import pt.up.hs.sampling.service.AnnotationTypeQueryService;

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
 * Integration tests for the {@link AnnotationTypeResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SamplingApp.class})
public class AnnotationTypeResourceIT {

    private static final Long DEFAULT_PROJECT_ID = 1L;
    private static final Long OTHER_PROJECT_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EMOTIONAL = false;
    private static final Boolean UPDATED_EMOTIONAL = true;

    private static final Double DEFAULT_WEIGHT = 1D;
    private static final Double UPDATED_WEIGHT = 2D;
    private static final Double SMALLER_WEIGHT = 1D - 1D;

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    @Autowired
    private AnnotationTypeRepository annotationTypeRepository;

    @Autowired
    private AnnotationTypeMapper annotationTypeMapper;

    @Autowired
    private AnnotationTypeService annotationTypeService;

    @Autowired
    private AnnotationTypeQueryService annotationTypeQueryService;

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

    private MockMvc restAnnotationTypeMockMvc;

    private AnnotationType annotationType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnnotationTypeResource annotationTypeResource = new AnnotationTypeResource(annotationTypeService, annotationTypeQueryService);
        this.restAnnotationTypeMockMvc = MockMvcBuilders.standaloneSetup(annotationTypeResource)
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
    public static AnnotationType createEntity() {
        return new AnnotationType()
            .name(DEFAULT_NAME)
            .label(DEFAULT_LABEL)
            .description(DEFAULT_DESCRIPTION)
            .emotional(DEFAULT_EMOTIONAL)
            .weight(DEFAULT_WEIGHT)
            .color(DEFAULT_COLOR)
            .projectId(DEFAULT_PROJECT_ID);
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnnotationType createUpdatedEntity() {
        return new AnnotationType()
            .name(UPDATED_NAME)
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .emotional(UPDATED_EMOTIONAL)
            .weight(UPDATED_WEIGHT)
            .color(UPDATED_COLOR)
            .projectId(DEFAULT_PROJECT_ID);
    }

    @BeforeEach
    public void initTest() {
        annotationType = createEntity();
    }

    @Test
    @Transactional
    public void createAnnotationType() throws Exception {
        int databaseSizeBeforeCreate = annotationTypeRepository.findAll().size();

        // Create the AnnotationType
        AnnotationTypeDTO annotationTypeDTO = annotationTypeMapper.toDto(annotationType);
        restAnnotationTypeMockMvc.perform(post("/api/projects/{projectId}/annotation-types", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(annotationTypeDTO)))
            .andExpect(status().isCreated());

        // Validate the AnnotationType in the database
        List<AnnotationType> annotationTypeList = annotationTypeRepository.findAll();
        assertThat(annotationTypeList).hasSize(databaseSizeBeforeCreate + 1);
        AnnotationType testAnnotationType = annotationTypeList.get(annotationTypeList.size() - 1);
        assertThat(testAnnotationType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAnnotationType.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testAnnotationType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testAnnotationType.isEmotional()).isEqualTo(DEFAULT_EMOTIONAL);
        assertThat(testAnnotationType.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testAnnotationType.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testAnnotationType.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void createAnnotationTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = annotationTypeRepository.findAll().size();

        // Create the AnnotationType with an existing ID
        annotationType.setId(1L);
        AnnotationTypeDTO annotationTypeDTO = annotationTypeMapper.toDto(annotationType);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnotationTypeMockMvc.perform(post("/api/projects/{projectId}/annotation-types", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(annotationTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AnnotationType in the database
        List<AnnotationType> annotationTypeList = annotationTypeRepository.findAll();
        assertThat(annotationTypeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = annotationTypeRepository.findAll().size();
        // set the field null
        annotationType.setName(null);

        // Create the AnnotationType, which fails.
        AnnotationTypeDTO annotationTypeDTO = annotationTypeMapper.toDto(annotationType);

        restAnnotationTypeMockMvc.perform(post("/api/projects/{projectId}/annotation-types", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(annotationTypeDTO)))
            .andExpect(status().isBadRequest());

        List<AnnotationType> annotationTypeList = annotationTypeRepository.findAll();
        assertThat(annotationTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = annotationTypeRepository.findAll().size();
        // set the field null
        annotationType.setLabel(null);

        // Create the AnnotationType, which fails.
        AnnotationTypeDTO annotationTypeDTO = annotationTypeMapper.toDto(annotationType);

        restAnnotationTypeMockMvc.perform(post("/api/projects/{projectId}/annotation-types", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(annotationTypeDTO)))
            .andExpect(status().isBadRequest());

        List<AnnotationType> annotationTypeList = annotationTypeRepository.findAll();
        assertThat(annotationTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = annotationTypeRepository.findAll().size();
        // set the field null
        annotationType.setColor(null);

        // Create the AnnotationType, which fails.
        AnnotationTypeDTO annotationTypeDTO = annotationTypeMapper.toDto(annotationType);

        restAnnotationTypeMockMvc.perform(post("/api/projects/{projectId}/annotation-types", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(annotationTypeDTO)))
            .andExpect(status().isBadRequest());

        List<AnnotationType> annotationTypeList = annotationTypeRepository.findAll();
        assertThat(annotationTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypes() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList
        restAnnotationTypeMockMvc.perform(get("/api/projects/{projectId}/annotation-types?sort=id,desc", DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annotationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].emotional").value(hasItem(DEFAULT_EMOTIONAL)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.intValue())));
    }

    @Test
    @Transactional
    public void getAnnotationType() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get the annotationType
        restAnnotationTypeMockMvc.perform(get("/api/projects/{projectId}/annotation-types/{id}", DEFAULT_PROJECT_ID, annotationType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(annotationType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.emotional").value(DEFAULT_EMOTIONAL))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.projectId").value(DEFAULT_PROJECT_ID.intValue()));
    }


    @Test
    @Transactional
    public void getAnnotationTypesByIdFiltering() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        Long id = annotationType.getId();

        defaultAnnotationTypeShouldBeFound("id.equals=" + id);
        defaultAnnotationTypeShouldNotBeFound("id.notEquals=" + id);

        defaultAnnotationTypeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultAnnotationTypeShouldNotBeFound("id.greaterThan=" + id);

        defaultAnnotationTypeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultAnnotationTypeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllAnnotationTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where name equals to DEFAULT_NAME
        defaultAnnotationTypeShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the annotationTypeList where name equals to UPDATED_NAME
        defaultAnnotationTypeShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where name not equals to DEFAULT_NAME
        defaultAnnotationTypeShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the annotationTypeList where name not equals to UPDATED_NAME
        defaultAnnotationTypeShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAnnotationTypeShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the annotationTypeList where name equals to UPDATED_NAME
        defaultAnnotationTypeShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where name is not null
        defaultAnnotationTypeShouldBeFound("name.specified=true");

        // Get all the annotationTypeList where name is null
        defaultAnnotationTypeShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where name contains DEFAULT_NAME
        defaultAnnotationTypeShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the annotationTypeList where name contains UPDATED_NAME
        defaultAnnotationTypeShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where name does not contain DEFAULT_NAME
        defaultAnnotationTypeShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the annotationTypeList where name does not contain UPDATED_NAME
        defaultAnnotationTypeShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllAnnotationTypesByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where label equals to DEFAULT_LABEL
        defaultAnnotationTypeShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the annotationTypeList where label equals to UPDATED_LABEL
        defaultAnnotationTypeShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where label not equals to DEFAULT_LABEL
        defaultAnnotationTypeShouldNotBeFound("label.notEquals=" + DEFAULT_LABEL);

        // Get all the annotationTypeList where label not equals to UPDATED_LABEL
        defaultAnnotationTypeShouldBeFound("label.notEquals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultAnnotationTypeShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the annotationTypeList where label equals to UPDATED_LABEL
        defaultAnnotationTypeShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where label is not null
        defaultAnnotationTypeShouldBeFound("label.specified=true");

        // Get all the annotationTypeList where label is null
        defaultAnnotationTypeShouldNotBeFound("label.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByLabelContainsSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where label contains DEFAULT_LABEL
        defaultAnnotationTypeShouldBeFound("label.contains=" + DEFAULT_LABEL);

        // Get all the annotationTypeList where label contains UPDATED_LABEL
        defaultAnnotationTypeShouldNotBeFound("label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where label does not contain DEFAULT_LABEL
        defaultAnnotationTypeShouldNotBeFound("label.doesNotContain=" + DEFAULT_LABEL);

        // Get all the annotationTypeList where label does not contain UPDATED_LABEL
        defaultAnnotationTypeShouldBeFound("label.doesNotContain=" + UPDATED_LABEL);
    }


    @Test
    @Transactional
    public void getAllAnnotationTypesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where description equals to DEFAULT_DESCRIPTION
        defaultAnnotationTypeShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the annotationTypeList where description equals to UPDATED_DESCRIPTION
        defaultAnnotationTypeShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where description not equals to DEFAULT_DESCRIPTION
        defaultAnnotationTypeShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the annotationTypeList where description not equals to UPDATED_DESCRIPTION
        defaultAnnotationTypeShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultAnnotationTypeShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the annotationTypeList where description equals to UPDATED_DESCRIPTION
        defaultAnnotationTypeShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where description is not null
        defaultAnnotationTypeShouldBeFound("description.specified=true");

        // Get all the annotationTypeList where description is null
        defaultAnnotationTypeShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where description contains DEFAULT_DESCRIPTION
        defaultAnnotationTypeShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the annotationTypeList where description contains UPDATED_DESCRIPTION
        defaultAnnotationTypeShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where description does not contain DEFAULT_DESCRIPTION
        defaultAnnotationTypeShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the annotationTypeList where description does not contain UPDATED_DESCRIPTION
        defaultAnnotationTypeShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllAnnotationTypesByEmotionalIsEqualToSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where emotional equals to DEFAULT_EMOTIONAL
        defaultAnnotationTypeShouldBeFound("emotional.equals=" + DEFAULT_EMOTIONAL);

        // Get all the annotationTypeList where emotional equals to UPDATED_EMOTIONAL
        defaultAnnotationTypeShouldNotBeFound("emotional.equals=" + UPDATED_EMOTIONAL);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByEmotionalIsNotEqualToSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where emotional not equals to DEFAULT_EMOTIONAL
        defaultAnnotationTypeShouldNotBeFound("emotional.notEquals=" + DEFAULT_EMOTIONAL);

        // Get all the annotationTypeList where emotional not equals to UPDATED_EMOTIONAL
        defaultAnnotationTypeShouldBeFound("emotional.notEquals=" + UPDATED_EMOTIONAL);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByEmotionalIsInShouldWork() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where emotional in DEFAULT_EMOTIONAL or UPDATED_EMOTIONAL
        defaultAnnotationTypeShouldBeFound("emotional.in=" + DEFAULT_EMOTIONAL + "," + UPDATED_EMOTIONAL);

        // Get all the annotationTypeList where emotional equals to UPDATED_EMOTIONAL
        defaultAnnotationTypeShouldNotBeFound("emotional.in=" + UPDATED_EMOTIONAL);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByEmotionalIsNullOrNotNull() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where emotional is not null
        defaultAnnotationTypeShouldBeFound("emotional.specified=true");

        // Get all the annotationTypeList where emotional is null
        defaultAnnotationTypeShouldNotBeFound("emotional.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where weight equals to DEFAULT_WEIGHT
        defaultAnnotationTypeShouldBeFound("weight.equals=" + DEFAULT_WEIGHT);

        // Get all the annotationTypeList where weight equals to UPDATED_WEIGHT
        defaultAnnotationTypeShouldNotBeFound("weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByWeightIsNotEqualToSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where weight not equals to DEFAULT_WEIGHT
        defaultAnnotationTypeShouldNotBeFound("weight.notEquals=" + DEFAULT_WEIGHT);

        // Get all the annotationTypeList where weight not equals to UPDATED_WEIGHT
        defaultAnnotationTypeShouldBeFound("weight.notEquals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where weight in DEFAULT_WEIGHT or UPDATED_WEIGHT
        defaultAnnotationTypeShouldBeFound("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT);

        // Get all the annotationTypeList where weight equals to UPDATED_WEIGHT
        defaultAnnotationTypeShouldNotBeFound("weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where weight is not null
        defaultAnnotationTypeShouldBeFound("weight.specified=true");

        // Get all the annotationTypeList where weight is null
        defaultAnnotationTypeShouldNotBeFound("weight.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where weight is greater than or equal to DEFAULT_WEIGHT
        defaultAnnotationTypeShouldBeFound("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT);

        // Get all the annotationTypeList where weight is greater than or equal to UPDATED_WEIGHT
        defaultAnnotationTypeShouldNotBeFound("weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where weight is less than or equal to DEFAULT_WEIGHT
        defaultAnnotationTypeShouldBeFound("weight.lessThanOrEqual=" + DEFAULT_WEIGHT);

        // Get all the annotationTypeList where weight is less than or equal to SMALLER_WEIGHT
        defaultAnnotationTypeShouldNotBeFound("weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where weight is less than DEFAULT_WEIGHT
        defaultAnnotationTypeShouldNotBeFound("weight.lessThan=" + DEFAULT_WEIGHT);

        // Get all the annotationTypeList where weight is less than UPDATED_WEIGHT
        defaultAnnotationTypeShouldBeFound("weight.lessThan=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where weight is greater than DEFAULT_WEIGHT
        defaultAnnotationTypeShouldNotBeFound("weight.greaterThan=" + DEFAULT_WEIGHT);

        // Get all the annotationTypeList where weight is greater than SMALLER_WEIGHT
        defaultAnnotationTypeShouldBeFound("weight.greaterThan=" + SMALLER_WEIGHT);
    }


    @Test
    @Transactional
    public void getAllAnnotationTypesByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where color equals to DEFAULT_COLOR
        defaultAnnotationTypeShouldBeFound("color.equals=" + DEFAULT_COLOR);

        // Get all the annotationTypeList where color equals to UPDATED_COLOR
        defaultAnnotationTypeShouldNotBeFound("color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where color not equals to DEFAULT_COLOR
        defaultAnnotationTypeShouldNotBeFound("color.notEquals=" + DEFAULT_COLOR);

        // Get all the annotationTypeList where color not equals to UPDATED_COLOR
        defaultAnnotationTypeShouldBeFound("color.notEquals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByColorIsInShouldWork() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where color in DEFAULT_COLOR or UPDATED_COLOR
        defaultAnnotationTypeShouldBeFound("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR);

        // Get all the annotationTypeList where color equals to UPDATED_COLOR
        defaultAnnotationTypeShouldNotBeFound("color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where color is not null
        defaultAnnotationTypeShouldBeFound("color.specified=true");

        // Get all the annotationTypeList where color is null
        defaultAnnotationTypeShouldNotBeFound("color.specified=false");
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByColorContainsSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where color contains DEFAULT_COLOR
        defaultAnnotationTypeShouldBeFound("color.contains=" + DEFAULT_COLOR);

        // Get all the annotationTypeList where color contains UPDATED_COLOR
        defaultAnnotationTypeShouldNotBeFound("color.contains=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    public void getAllAnnotationTypesByColorNotContainsSomething() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        // Get all the annotationTypeList where color does not contain DEFAULT_COLOR
        defaultAnnotationTypeShouldNotBeFound("color.doesNotContain=" + DEFAULT_COLOR);

        // Get all the annotationTypeList where color does not contain UPDATED_COLOR
        defaultAnnotationTypeShouldBeFound("color.doesNotContain=" + UPDATED_COLOR);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAnnotationTypeShouldBeFound(String filter) throws Exception {
        restAnnotationTypeMockMvc.perform(get("/api/projects/{projectId}/annotation-types?sort=id,desc&" + filter, DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annotationType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].emotional").value(hasItem(DEFAULT_EMOTIONAL)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.intValue())));

        // Check, that the count call also returns 1
        restAnnotationTypeMockMvc.perform(get("/api/projects/{projectId}/annotation-types/count?sort=id,desc&" + filter, DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAnnotationTypeShouldNotBeFound(String filter) throws Exception {
        restAnnotationTypeMockMvc.perform(get("/api/projects/{projectId}/annotation-types?sort=id,desc&" + filter, DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAnnotationTypeMockMvc.perform(get("/api/projects/{projectId}/annotation-types/count?sort=id,desc&" + filter, DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingAnnotationType() throws Exception {
        // Get the annotationType
        restAnnotationTypeMockMvc.perform(get("/api/projects/{projectId}/annotation-types/{id}", DEFAULT_PROJECT_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnnotationType() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        int databaseSizeBeforeUpdate = annotationTypeRepository.findAll().size();

        // Update the annotationType
        AnnotationType updatedAnnotationType = annotationTypeRepository.findById(annotationType.getId()).get();
        // Disconnect from session so that the updates on updatedAnnotationType are not directly saved in db
        em.detach(updatedAnnotationType);
        updatedAnnotationType
            .name(UPDATED_NAME)
            .label(UPDATED_LABEL)
            .description(UPDATED_DESCRIPTION)
            .emotional(UPDATED_EMOTIONAL)
            .weight(UPDATED_WEIGHT)
            .color(UPDATED_COLOR)
            .projectId(DEFAULT_PROJECT_ID);
        AnnotationTypeDTO annotationTypeDTO = annotationTypeMapper.toDto(updatedAnnotationType);

        restAnnotationTypeMockMvc.perform(put("/api/projects/{projectId}/annotation-types", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(annotationTypeDTO)))
            .andExpect(status().isOk());

        // Validate the AnnotationType in the database
        List<AnnotationType> annotationTypeList = annotationTypeRepository.findAll();
        assertThat(annotationTypeList).hasSize(databaseSizeBeforeUpdate);
        AnnotationType testAnnotationType = annotationTypeList.get(annotationTypeList.size() - 1);
        assertThat(testAnnotationType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAnnotationType.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testAnnotationType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testAnnotationType.isEmotional()).isEqualTo(UPDATED_EMOTIONAL);
        assertThat(testAnnotationType.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testAnnotationType.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testAnnotationType.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingAnnotationType() throws Exception {
        int databaseSizeBeforeUpdate = annotationTypeRepository.findAll().size();

        // Create the AnnotationType
        AnnotationTypeDTO annotationTypeDTO = annotationTypeMapper.toDto(annotationType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnotationTypeMockMvc.perform(put("/api/projects/{projectId}/annotation-types", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(annotationTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AnnotationType in the database
        List<AnnotationType> annotationTypeList = annotationTypeRepository.findAll();
        assertThat(annotationTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAnnotationType() throws Exception {
        // Initialize the database
        annotationTypeRepository.saveAndFlush(annotationType);

        int databaseSizeBeforeDelete = annotationTypeRepository.findAll().size();

        // Delete the annotationType
        restAnnotationTypeMockMvc.perform(delete("/api/projects/{projectId}/annotation-types/{id}", DEFAULT_PROJECT_ID, annotationType.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnnotationType> annotationTypeList = annotationTypeRepository.findAll();
        assertThat(annotationTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
