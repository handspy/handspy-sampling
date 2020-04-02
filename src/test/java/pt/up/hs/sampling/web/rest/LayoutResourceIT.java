package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.SamplingApp;
import pt.up.hs.sampling.config.SecurityBeanOverrideConfiguration;
import pt.up.hs.sampling.domain.Layout;
import pt.up.hs.sampling.repository.LayoutRepository;
import pt.up.hs.sampling.service.LayoutService;
import pt.up.hs.sampling.service.dto.LayoutDTO;
import pt.up.hs.sampling.service.mapper.LayoutMapper;
import pt.up.hs.sampling.web.rest.errors.ExceptionTranslator;
import pt.up.hs.sampling.service.dto.LayoutCriteria;
import pt.up.hs.sampling.service.LayoutQueryService;

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
 * Integration tests for the {@link LayoutResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SamplingApp.class})
public class LayoutResourceIT {

    private static final Long DEFAULT_PROJECT_ID = 1L;
    private static final Long OTHER_PROJECT_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_WIDTH = 1;
    private static final Integer UPDATED_WIDTH = 2;
    private static final Integer SMALLER_WIDTH = 1 - 1;

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;
    private static final Integer SMALLER_HEIGHT = 1 - 1;

    private static final Integer DEFAULT_MARGIN_LEFT = 1;
    private static final Integer UPDATED_MARGIN_LEFT = 2;
    private static final Integer SMALLER_MARGIN_LEFT = 1 - 1;

    private static final Integer DEFAULT_MARGIN_RIGHT = 1;
    private static final Integer UPDATED_MARGIN_RIGHT = 2;
    private static final Integer SMALLER_MARGIN_RIGHT = 1 - 1;

    private static final Integer DEFAULT_MARGIN_TOP = 1;
    private static final Integer UPDATED_MARGIN_TOP = 2;
    private static final Integer SMALLER_MARGIN_TOP = 1 - 1;

    private static final Integer DEFAULT_MARGIN_BOTTOM = 1;
    private static final Integer UPDATED_MARGIN_BOTTOM = 2;
    private static final Integer SMALLER_MARGIN_BOTTOM = 1 - 1;

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    @Autowired
    private LayoutRepository layoutRepository;

    @Autowired
    private LayoutMapper layoutMapper;

    @Autowired
    private LayoutService layoutService;

    @Autowired
    private LayoutQueryService layoutQueryService;

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

    private MockMvc restLayoutMockMvc;

    private Layout layout;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LayoutResource layoutResource = new LayoutResource(layoutService, layoutQueryService);
        this.restLayoutMockMvc = MockMvcBuilders.standaloneSetup(layoutResource)
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
    public static Layout createEntity(EntityManager em) {
        Layout layout = new Layout()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .width(DEFAULT_WIDTH)
            .height(DEFAULT_HEIGHT)
            .marginLeft(DEFAULT_MARGIN_LEFT)
            .marginRight(DEFAULT_MARGIN_RIGHT)
            .marginTop(DEFAULT_MARGIN_TOP)
            .marginBottom(DEFAULT_MARGIN_BOTTOM)
            .url(DEFAULT_URL);
        return layout;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Layout createUpdatedEntity(EntityManager em) {
        Layout layout = new Layout()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .marginLeft(UPDATED_MARGIN_LEFT)
            .marginRight(UPDATED_MARGIN_RIGHT)
            .marginTop(UPDATED_MARGIN_TOP)
            .marginBottom(UPDATED_MARGIN_BOTTOM)
            .url(UPDATED_URL);
        return layout;
    }

    @BeforeEach
    public void initTest() {
        layout = createEntity(em);
    }

    @Test
    @Transactional
    public void createLayout() throws Exception {
        int databaseSizeBeforeCreate = layoutRepository.findAll().size();

        // Create the Layout
        LayoutDTO layoutDTO = layoutMapper.toDto(layout);
        restLayoutMockMvc.perform(post("/api/layouts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(layoutDTO)))
            .andExpect(status().isCreated());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeCreate + 1);
        Layout testLayout = layoutList.get(layoutList.size() - 1);
        assertThat(testLayout.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLayout.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLayout.getWidth()).isEqualTo(DEFAULT_WIDTH);
        assertThat(testLayout.getHeight()).isEqualTo(DEFAULT_HEIGHT);
        assertThat(testLayout.getMarginLeft()).isEqualTo(DEFAULT_MARGIN_LEFT);
        assertThat(testLayout.getMarginRight()).isEqualTo(DEFAULT_MARGIN_RIGHT);
        assertThat(testLayout.getMarginTop()).isEqualTo(DEFAULT_MARGIN_TOP);
        assertThat(testLayout.getMarginBottom()).isEqualTo(DEFAULT_MARGIN_BOTTOM);
        assertThat(testLayout.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createLayoutWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = layoutRepository.findAll().size();

        // Create the Layout with an existing ID
        layout.setId(1L);
        LayoutDTO layoutDTO = layoutMapper.toDto(layout);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLayoutMockMvc.perform(post("/api/layouts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(layoutDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = layoutRepository.findAll().size();
        // set the field null
        layout.setName(null);

        // Create the Layout, which fails.
        LayoutDTO layoutDTO = layoutMapper.toDto(layout);

        restLayoutMockMvc.perform(post("/api/layouts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(layoutDTO)))
            .andExpect(status().isBadRequest());

        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkWidthIsRequired() throws Exception {
        int databaseSizeBeforeTest = layoutRepository.findAll().size();
        // set the field null
        layout.setWidth(null);

        // Create the Layout, which fails.
        LayoutDTO layoutDTO = layoutMapper.toDto(layout);

        restLayoutMockMvc.perform(post("/api/layouts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(layoutDTO)))
            .andExpect(status().isBadRequest());

        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHeightIsRequired() throws Exception {
        int databaseSizeBeforeTest = layoutRepository.findAll().size();
        // set the field null
        layout.setHeight(null);

        // Create the Layout, which fails.
        LayoutDTO layoutDTO = layoutMapper.toDto(layout);

        restLayoutMockMvc.perform(post("/api/layouts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(layoutDTO)))
            .andExpect(status().isBadRequest());

        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMarginLeftIsRequired() throws Exception {
        int databaseSizeBeforeTest = layoutRepository.findAll().size();
        // set the field null
        layout.setMarginLeft(null);

        // Create the Layout, which fails.
        LayoutDTO layoutDTO = layoutMapper.toDto(layout);

        restLayoutMockMvc.perform(post("/api/layouts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(layoutDTO)))
            .andExpect(status().isBadRequest());

        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMarginRightIsRequired() throws Exception {
        int databaseSizeBeforeTest = layoutRepository.findAll().size();
        // set the field null
        layout.setMarginRight(null);

        // Create the Layout, which fails.
        LayoutDTO layoutDTO = layoutMapper.toDto(layout);

        restLayoutMockMvc.perform(post("/api/layouts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(layoutDTO)))
            .andExpect(status().isBadRequest());

        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMarginTopIsRequired() throws Exception {
        int databaseSizeBeforeTest = layoutRepository.findAll().size();
        // set the field null
        layout.setMarginTop(null);

        // Create the Layout, which fails.
        LayoutDTO layoutDTO = layoutMapper.toDto(layout);

        restLayoutMockMvc.perform(post("/api/layouts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(layoutDTO)))
            .andExpect(status().isBadRequest());

        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMarginBottomIsRequired() throws Exception {
        int databaseSizeBeforeTest = layoutRepository.findAll().size();
        // set the field null
        layout.setMarginBottom(null);

        // Create the Layout, which fails.
        LayoutDTO layoutDTO = layoutMapper.toDto(layout);

        restLayoutMockMvc.perform(post("/api/layouts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(layoutDTO)))
            .andExpect(status().isBadRequest());

        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLayouts() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList
        restLayoutMockMvc.perform(get("/api/layouts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(layout.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].marginLeft").value(hasItem(DEFAULT_MARGIN_LEFT)))
            .andExpect(jsonPath("$.[*].marginRight").value(hasItem(DEFAULT_MARGIN_RIGHT)))
            .andExpect(jsonPath("$.[*].marginTop").value(hasItem(DEFAULT_MARGIN_TOP)))
            .andExpect(jsonPath("$.[*].marginBottom").value(hasItem(DEFAULT_MARGIN_BOTTOM)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    public void getLayout() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get the layout
        restLayoutMockMvc.perform(get("/api/layouts/{id}", layout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(layout.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.marginLeft").value(DEFAULT_MARGIN_LEFT))
            .andExpect(jsonPath("$.marginRight").value(DEFAULT_MARGIN_RIGHT))
            .andExpect(jsonPath("$.marginTop").value(DEFAULT_MARGIN_TOP))
            .andExpect(jsonPath("$.marginBottom").value(DEFAULT_MARGIN_BOTTOM))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }


    @Test
    @Transactional
    public void getLayoutsByIdFiltering() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        Long id = layout.getId();

        defaultLayoutShouldBeFound("id.equals=" + id);
        defaultLayoutShouldNotBeFound("id.notEquals=" + id);

        defaultLayoutShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLayoutShouldNotBeFound("id.greaterThan=" + id);

        defaultLayoutShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLayoutShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLayoutsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where name equals to DEFAULT_NAME
        defaultLayoutShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the layoutList where name equals to UPDATED_NAME
        defaultLayoutShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLayoutsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where name not equals to DEFAULT_NAME
        defaultLayoutShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the layoutList where name not equals to UPDATED_NAME
        defaultLayoutShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLayoutsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where name in DEFAULT_NAME or UPDATED_NAME
        defaultLayoutShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the layoutList where name equals to UPDATED_NAME
        defaultLayoutShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLayoutsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where name is not null
        defaultLayoutShouldBeFound("name.specified=true");

        // Get all the layoutList where name is null
        defaultLayoutShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllLayoutsByNameContainsSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where name contains DEFAULT_NAME
        defaultLayoutShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the layoutList where name contains UPDATED_NAME
        defaultLayoutShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLayoutsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where name does not contain DEFAULT_NAME
        defaultLayoutShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the layoutList where name does not contain UPDATED_NAME
        defaultLayoutShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllLayoutsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where description equals to DEFAULT_DESCRIPTION
        defaultLayoutShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the layoutList where description equals to UPDATED_DESCRIPTION
        defaultLayoutShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLayoutsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where description not equals to DEFAULT_DESCRIPTION
        defaultLayoutShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the layoutList where description not equals to UPDATED_DESCRIPTION
        defaultLayoutShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLayoutsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultLayoutShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the layoutList where description equals to UPDATED_DESCRIPTION
        defaultLayoutShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLayoutsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where description is not null
        defaultLayoutShouldBeFound("description.specified=true");

        // Get all the layoutList where description is null
        defaultLayoutShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllLayoutsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where description contains DEFAULT_DESCRIPTION
        defaultLayoutShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the layoutList where description contains UPDATED_DESCRIPTION
        defaultLayoutShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLayoutsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where description does not contain DEFAULT_DESCRIPTION
        defaultLayoutShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the layoutList where description does not contain UPDATED_DESCRIPTION
        defaultLayoutShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllLayoutsByWidthIsEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where width equals to DEFAULT_WIDTH
        defaultLayoutShouldBeFound("width.equals=" + DEFAULT_WIDTH);

        // Get all the layoutList where width equals to UPDATED_WIDTH
        defaultLayoutShouldNotBeFound("width.equals=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    public void getAllLayoutsByWidthIsNotEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where width not equals to DEFAULT_WIDTH
        defaultLayoutShouldNotBeFound("width.notEquals=" + DEFAULT_WIDTH);

        // Get all the layoutList where width not equals to UPDATED_WIDTH
        defaultLayoutShouldBeFound("width.notEquals=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    public void getAllLayoutsByWidthIsInShouldWork() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where width in DEFAULT_WIDTH or UPDATED_WIDTH
        defaultLayoutShouldBeFound("width.in=" + DEFAULT_WIDTH + "," + UPDATED_WIDTH);

        // Get all the layoutList where width equals to UPDATED_WIDTH
        defaultLayoutShouldNotBeFound("width.in=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    public void getAllLayoutsByWidthIsNullOrNotNull() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where width is not null
        defaultLayoutShouldBeFound("width.specified=true");

        // Get all the layoutList where width is null
        defaultLayoutShouldNotBeFound("width.specified=false");
    }

    @Test
    @Transactional
    public void getAllLayoutsByWidthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where width is greater than or equal to DEFAULT_WIDTH
        defaultLayoutShouldBeFound("width.greaterThanOrEqual=" + DEFAULT_WIDTH);

        // Get all the layoutList where width is greater than or equal to UPDATED_WIDTH
        defaultLayoutShouldNotBeFound("width.greaterThanOrEqual=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    public void getAllLayoutsByWidthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where width is less than or equal to DEFAULT_WIDTH
        defaultLayoutShouldBeFound("width.lessThanOrEqual=" + DEFAULT_WIDTH);

        // Get all the layoutList where width is less than or equal to SMALLER_WIDTH
        defaultLayoutShouldNotBeFound("width.lessThanOrEqual=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    public void getAllLayoutsByWidthIsLessThanSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where width is less than DEFAULT_WIDTH
        defaultLayoutShouldNotBeFound("width.lessThan=" + DEFAULT_WIDTH);

        // Get all the layoutList where width is less than UPDATED_WIDTH
        defaultLayoutShouldBeFound("width.lessThan=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    public void getAllLayoutsByWidthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where width is greater than DEFAULT_WIDTH
        defaultLayoutShouldNotBeFound("width.greaterThan=" + DEFAULT_WIDTH);

        // Get all the layoutList where width is greater than SMALLER_WIDTH
        defaultLayoutShouldBeFound("width.greaterThan=" + SMALLER_WIDTH);
    }


    @Test
    @Transactional
    public void getAllLayoutsByHeightIsEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where height equals to DEFAULT_HEIGHT
        defaultLayoutShouldBeFound("height.equals=" + DEFAULT_HEIGHT);

        // Get all the layoutList where height equals to UPDATED_HEIGHT
        defaultLayoutShouldNotBeFound("height.equals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllLayoutsByHeightIsNotEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where height not equals to DEFAULT_HEIGHT
        defaultLayoutShouldNotBeFound("height.notEquals=" + DEFAULT_HEIGHT);

        // Get all the layoutList where height not equals to UPDATED_HEIGHT
        defaultLayoutShouldBeFound("height.notEquals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllLayoutsByHeightIsInShouldWork() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where height in DEFAULT_HEIGHT or UPDATED_HEIGHT
        defaultLayoutShouldBeFound("height.in=" + DEFAULT_HEIGHT + "," + UPDATED_HEIGHT);

        // Get all the layoutList where height equals to UPDATED_HEIGHT
        defaultLayoutShouldNotBeFound("height.in=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllLayoutsByHeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where height is not null
        defaultLayoutShouldBeFound("height.specified=true");

        // Get all the layoutList where height is null
        defaultLayoutShouldNotBeFound("height.specified=false");
    }

    @Test
    @Transactional
    public void getAllLayoutsByHeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where height is greater than or equal to DEFAULT_HEIGHT
        defaultLayoutShouldBeFound("height.greaterThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the layoutList where height is greater than or equal to UPDATED_HEIGHT
        defaultLayoutShouldNotBeFound("height.greaterThanOrEqual=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllLayoutsByHeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where height is less than or equal to DEFAULT_HEIGHT
        defaultLayoutShouldBeFound("height.lessThanOrEqual=" + DEFAULT_HEIGHT);

        // Get all the layoutList where height is less than or equal to SMALLER_HEIGHT
        defaultLayoutShouldNotBeFound("height.lessThanOrEqual=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllLayoutsByHeightIsLessThanSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where height is less than DEFAULT_HEIGHT
        defaultLayoutShouldNotBeFound("height.lessThan=" + DEFAULT_HEIGHT);

        // Get all the layoutList where height is less than UPDATED_HEIGHT
        defaultLayoutShouldBeFound("height.lessThan=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    public void getAllLayoutsByHeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where height is greater than DEFAULT_HEIGHT
        defaultLayoutShouldNotBeFound("height.greaterThan=" + DEFAULT_HEIGHT);

        // Get all the layoutList where height is greater than SMALLER_HEIGHT
        defaultLayoutShouldBeFound("height.greaterThan=" + SMALLER_HEIGHT);
    }


    @Test
    @Transactional
    public void getAllLayoutsByMarginLeftIsEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginLeft equals to DEFAULT_MARGIN_LEFT
        defaultLayoutShouldBeFound("marginLeft.equals=" + DEFAULT_MARGIN_LEFT);

        // Get all the layoutList where marginLeft equals to UPDATED_MARGIN_LEFT
        defaultLayoutShouldNotBeFound("marginLeft.equals=" + UPDATED_MARGIN_LEFT);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginLeftIsNotEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginLeft not equals to DEFAULT_MARGIN_LEFT
        defaultLayoutShouldNotBeFound("marginLeft.notEquals=" + DEFAULT_MARGIN_LEFT);

        // Get all the layoutList where marginLeft not equals to UPDATED_MARGIN_LEFT
        defaultLayoutShouldBeFound("marginLeft.notEquals=" + UPDATED_MARGIN_LEFT);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginLeftIsInShouldWork() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginLeft in DEFAULT_MARGIN_LEFT or UPDATED_MARGIN_LEFT
        defaultLayoutShouldBeFound("marginLeft.in=" + DEFAULT_MARGIN_LEFT + "," + UPDATED_MARGIN_LEFT);

        // Get all the layoutList where marginLeft equals to UPDATED_MARGIN_LEFT
        defaultLayoutShouldNotBeFound("marginLeft.in=" + UPDATED_MARGIN_LEFT);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginLeftIsNullOrNotNull() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginLeft is not null
        defaultLayoutShouldBeFound("marginLeft.specified=true");

        // Get all the layoutList where marginLeft is null
        defaultLayoutShouldNotBeFound("marginLeft.specified=false");
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginLeftIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginLeft is greater than or equal to DEFAULT_MARGIN_LEFT
        defaultLayoutShouldBeFound("marginLeft.greaterThanOrEqual=" + DEFAULT_MARGIN_LEFT);

        // Get all the layoutList where marginLeft is greater than or equal to UPDATED_MARGIN_LEFT
        defaultLayoutShouldNotBeFound("marginLeft.greaterThanOrEqual=" + UPDATED_MARGIN_LEFT);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginLeftIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginLeft is less than or equal to DEFAULT_MARGIN_LEFT
        defaultLayoutShouldBeFound("marginLeft.lessThanOrEqual=" + DEFAULT_MARGIN_LEFT);

        // Get all the layoutList where marginLeft is less than or equal to SMALLER_MARGIN_LEFT
        defaultLayoutShouldNotBeFound("marginLeft.lessThanOrEqual=" + SMALLER_MARGIN_LEFT);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginLeftIsLessThanSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginLeft is less than DEFAULT_MARGIN_LEFT
        defaultLayoutShouldNotBeFound("marginLeft.lessThan=" + DEFAULT_MARGIN_LEFT);

        // Get all the layoutList where marginLeft is less than UPDATED_MARGIN_LEFT
        defaultLayoutShouldBeFound("marginLeft.lessThan=" + UPDATED_MARGIN_LEFT);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginLeftIsGreaterThanSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginLeft is greater than DEFAULT_MARGIN_LEFT
        defaultLayoutShouldNotBeFound("marginLeft.greaterThan=" + DEFAULT_MARGIN_LEFT);

        // Get all the layoutList where marginLeft is greater than SMALLER_MARGIN_LEFT
        defaultLayoutShouldBeFound("marginLeft.greaterThan=" + SMALLER_MARGIN_LEFT);
    }


    @Test
    @Transactional
    public void getAllLayoutsByMarginRightIsEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginRight equals to DEFAULT_MARGIN_RIGHT
        defaultLayoutShouldBeFound("marginRight.equals=" + DEFAULT_MARGIN_RIGHT);

        // Get all the layoutList where marginRight equals to UPDATED_MARGIN_RIGHT
        defaultLayoutShouldNotBeFound("marginRight.equals=" + UPDATED_MARGIN_RIGHT);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginRightIsNotEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginRight not equals to DEFAULT_MARGIN_RIGHT
        defaultLayoutShouldNotBeFound("marginRight.notEquals=" + DEFAULT_MARGIN_RIGHT);

        // Get all the layoutList where marginRight not equals to UPDATED_MARGIN_RIGHT
        defaultLayoutShouldBeFound("marginRight.notEquals=" + UPDATED_MARGIN_RIGHT);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginRightIsInShouldWork() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginRight in DEFAULT_MARGIN_RIGHT or UPDATED_MARGIN_RIGHT
        defaultLayoutShouldBeFound("marginRight.in=" + DEFAULT_MARGIN_RIGHT + "," + UPDATED_MARGIN_RIGHT);

        // Get all the layoutList where marginRight equals to UPDATED_MARGIN_RIGHT
        defaultLayoutShouldNotBeFound("marginRight.in=" + UPDATED_MARGIN_RIGHT);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginRightIsNullOrNotNull() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginRight is not null
        defaultLayoutShouldBeFound("marginRight.specified=true");

        // Get all the layoutList where marginRight is null
        defaultLayoutShouldNotBeFound("marginRight.specified=false");
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginRightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginRight is greater than or equal to DEFAULT_MARGIN_RIGHT
        defaultLayoutShouldBeFound("marginRight.greaterThanOrEqual=" + DEFAULT_MARGIN_RIGHT);

        // Get all the layoutList where marginRight is greater than or equal to UPDATED_MARGIN_RIGHT
        defaultLayoutShouldNotBeFound("marginRight.greaterThanOrEqual=" + UPDATED_MARGIN_RIGHT);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginRightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginRight is less than or equal to DEFAULT_MARGIN_RIGHT
        defaultLayoutShouldBeFound("marginRight.lessThanOrEqual=" + DEFAULT_MARGIN_RIGHT);

        // Get all the layoutList where marginRight is less than or equal to SMALLER_MARGIN_RIGHT
        defaultLayoutShouldNotBeFound("marginRight.lessThanOrEqual=" + SMALLER_MARGIN_RIGHT);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginRightIsLessThanSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginRight is less than DEFAULT_MARGIN_RIGHT
        defaultLayoutShouldNotBeFound("marginRight.lessThan=" + DEFAULT_MARGIN_RIGHT);

        // Get all the layoutList where marginRight is less than UPDATED_MARGIN_RIGHT
        defaultLayoutShouldBeFound("marginRight.lessThan=" + UPDATED_MARGIN_RIGHT);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginRightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginRight is greater than DEFAULT_MARGIN_RIGHT
        defaultLayoutShouldNotBeFound("marginRight.greaterThan=" + DEFAULT_MARGIN_RIGHT);

        // Get all the layoutList where marginRight is greater than SMALLER_MARGIN_RIGHT
        defaultLayoutShouldBeFound("marginRight.greaterThan=" + SMALLER_MARGIN_RIGHT);
    }


    @Test
    @Transactional
    public void getAllLayoutsByMarginTopIsEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginTop equals to DEFAULT_MARGIN_TOP
        defaultLayoutShouldBeFound("marginTop.equals=" + DEFAULT_MARGIN_TOP);

        // Get all the layoutList where marginTop equals to UPDATED_MARGIN_TOP
        defaultLayoutShouldNotBeFound("marginTop.equals=" + UPDATED_MARGIN_TOP);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginTopIsNotEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginTop not equals to DEFAULT_MARGIN_TOP
        defaultLayoutShouldNotBeFound("marginTop.notEquals=" + DEFAULT_MARGIN_TOP);

        // Get all the layoutList where marginTop not equals to UPDATED_MARGIN_TOP
        defaultLayoutShouldBeFound("marginTop.notEquals=" + UPDATED_MARGIN_TOP);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginTopIsInShouldWork() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginTop in DEFAULT_MARGIN_TOP or UPDATED_MARGIN_TOP
        defaultLayoutShouldBeFound("marginTop.in=" + DEFAULT_MARGIN_TOP + "," + UPDATED_MARGIN_TOP);

        // Get all the layoutList where marginTop equals to UPDATED_MARGIN_TOP
        defaultLayoutShouldNotBeFound("marginTop.in=" + UPDATED_MARGIN_TOP);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginTopIsNullOrNotNull() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginTop is not null
        defaultLayoutShouldBeFound("marginTop.specified=true");

        // Get all the layoutList where marginTop is null
        defaultLayoutShouldNotBeFound("marginTop.specified=false");
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginTopIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginTop is greater than or equal to DEFAULT_MARGIN_TOP
        defaultLayoutShouldBeFound("marginTop.greaterThanOrEqual=" + DEFAULT_MARGIN_TOP);

        // Get all the layoutList where marginTop is greater than or equal to UPDATED_MARGIN_TOP
        defaultLayoutShouldNotBeFound("marginTop.greaterThanOrEqual=" + UPDATED_MARGIN_TOP);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginTopIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginTop is less than or equal to DEFAULT_MARGIN_TOP
        defaultLayoutShouldBeFound("marginTop.lessThanOrEqual=" + DEFAULT_MARGIN_TOP);

        // Get all the layoutList where marginTop is less than or equal to SMALLER_MARGIN_TOP
        defaultLayoutShouldNotBeFound("marginTop.lessThanOrEqual=" + SMALLER_MARGIN_TOP);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginTopIsLessThanSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginTop is less than DEFAULT_MARGIN_TOP
        defaultLayoutShouldNotBeFound("marginTop.lessThan=" + DEFAULT_MARGIN_TOP);

        // Get all the layoutList where marginTop is less than UPDATED_MARGIN_TOP
        defaultLayoutShouldBeFound("marginTop.lessThan=" + UPDATED_MARGIN_TOP);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginTopIsGreaterThanSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginTop is greater than DEFAULT_MARGIN_TOP
        defaultLayoutShouldNotBeFound("marginTop.greaterThan=" + DEFAULT_MARGIN_TOP);

        // Get all the layoutList where marginTop is greater than SMALLER_MARGIN_TOP
        defaultLayoutShouldBeFound("marginTop.greaterThan=" + SMALLER_MARGIN_TOP);
    }


    @Test
    @Transactional
    public void getAllLayoutsByMarginBottomIsEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginBottom equals to DEFAULT_MARGIN_BOTTOM
        defaultLayoutShouldBeFound("marginBottom.equals=" + DEFAULT_MARGIN_BOTTOM);

        // Get all the layoutList where marginBottom equals to UPDATED_MARGIN_BOTTOM
        defaultLayoutShouldNotBeFound("marginBottom.equals=" + UPDATED_MARGIN_BOTTOM);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginBottomIsNotEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginBottom not equals to DEFAULT_MARGIN_BOTTOM
        defaultLayoutShouldNotBeFound("marginBottom.notEquals=" + DEFAULT_MARGIN_BOTTOM);

        // Get all the layoutList where marginBottom not equals to UPDATED_MARGIN_BOTTOM
        defaultLayoutShouldBeFound("marginBottom.notEquals=" + UPDATED_MARGIN_BOTTOM);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginBottomIsInShouldWork() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginBottom in DEFAULT_MARGIN_BOTTOM or UPDATED_MARGIN_BOTTOM
        defaultLayoutShouldBeFound("marginBottom.in=" + DEFAULT_MARGIN_BOTTOM + "," + UPDATED_MARGIN_BOTTOM);

        // Get all the layoutList where marginBottom equals to UPDATED_MARGIN_BOTTOM
        defaultLayoutShouldNotBeFound("marginBottom.in=" + UPDATED_MARGIN_BOTTOM);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginBottomIsNullOrNotNull() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginBottom is not null
        defaultLayoutShouldBeFound("marginBottom.specified=true");

        // Get all the layoutList where marginBottom is null
        defaultLayoutShouldNotBeFound("marginBottom.specified=false");
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginBottomIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginBottom is greater than or equal to DEFAULT_MARGIN_BOTTOM
        defaultLayoutShouldBeFound("marginBottom.greaterThanOrEqual=" + DEFAULT_MARGIN_BOTTOM);

        // Get all the layoutList where marginBottom is greater than or equal to UPDATED_MARGIN_BOTTOM
        defaultLayoutShouldNotBeFound("marginBottom.greaterThanOrEqual=" + UPDATED_MARGIN_BOTTOM);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginBottomIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginBottom is less than or equal to DEFAULT_MARGIN_BOTTOM
        defaultLayoutShouldBeFound("marginBottom.lessThanOrEqual=" + DEFAULT_MARGIN_BOTTOM);

        // Get all the layoutList where marginBottom is less than or equal to SMALLER_MARGIN_BOTTOM
        defaultLayoutShouldNotBeFound("marginBottom.lessThanOrEqual=" + SMALLER_MARGIN_BOTTOM);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginBottomIsLessThanSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginBottom is less than DEFAULT_MARGIN_BOTTOM
        defaultLayoutShouldNotBeFound("marginBottom.lessThan=" + DEFAULT_MARGIN_BOTTOM);

        // Get all the layoutList where marginBottom is less than UPDATED_MARGIN_BOTTOM
        defaultLayoutShouldBeFound("marginBottom.lessThan=" + UPDATED_MARGIN_BOTTOM);
    }

    @Test
    @Transactional
    public void getAllLayoutsByMarginBottomIsGreaterThanSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where marginBottom is greater than DEFAULT_MARGIN_BOTTOM
        defaultLayoutShouldNotBeFound("marginBottom.greaterThan=" + DEFAULT_MARGIN_BOTTOM);

        // Get all the layoutList where marginBottom is greater than SMALLER_MARGIN_BOTTOM
        defaultLayoutShouldBeFound("marginBottom.greaterThan=" + SMALLER_MARGIN_BOTTOM);
    }


    @Test
    @Transactional
    public void getAllLayoutsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where url equals to DEFAULT_URL
        defaultLayoutShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the layoutList where url equals to UPDATED_URL
        defaultLayoutShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllLayoutsByUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where url not equals to DEFAULT_URL
        defaultLayoutShouldNotBeFound("url.notEquals=" + DEFAULT_URL);

        // Get all the layoutList where url not equals to UPDATED_URL
        defaultLayoutShouldBeFound("url.notEquals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllLayoutsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where url in DEFAULT_URL or UPDATED_URL
        defaultLayoutShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the layoutList where url equals to UPDATED_URL
        defaultLayoutShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllLayoutsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where url is not null
        defaultLayoutShouldBeFound("url.specified=true");

        // Get all the layoutList where url is null
        defaultLayoutShouldNotBeFound("url.specified=false");
    }
                @Test
    @Transactional
    public void getAllLayoutsByUrlContainsSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where url contains DEFAULT_URL
        defaultLayoutShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the layoutList where url contains UPDATED_URL
        defaultLayoutShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    public void getAllLayoutsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        // Get all the layoutList where url does not contain DEFAULT_URL
        defaultLayoutShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the layoutList where url does not contain UPDATED_URL
        defaultLayoutShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLayoutShouldBeFound(String filter) throws Exception {
        restLayoutMockMvc.perform(get("/api/layouts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(layout.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].marginLeft").value(hasItem(DEFAULT_MARGIN_LEFT)))
            .andExpect(jsonPath("$.[*].marginRight").value(hasItem(DEFAULT_MARGIN_RIGHT)))
            .andExpect(jsonPath("$.[*].marginTop").value(hasItem(DEFAULT_MARGIN_TOP)))
            .andExpect(jsonPath("$.[*].marginBottom").value(hasItem(DEFAULT_MARGIN_BOTTOM)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));

        // Check, that the count call also returns 1
        restLayoutMockMvc.perform(get("/api/layouts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLayoutShouldNotBeFound(String filter) throws Exception {
        restLayoutMockMvc.perform(get("/api/layouts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLayoutMockMvc.perform(get("/api/layouts/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingLayout() throws Exception {
        // Get the layout
        restLayoutMockMvc.perform(get("/api/layouts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLayout() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();

        // Update the layout
        Layout updatedLayout = layoutRepository.findById(layout.getId()).get();
        // Disconnect from session so that the updates on updatedLayout are not directly saved in db
        em.detach(updatedLayout);
        updatedLayout
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .marginLeft(UPDATED_MARGIN_LEFT)
            .marginRight(UPDATED_MARGIN_RIGHT)
            .marginTop(UPDATED_MARGIN_TOP)
            .marginBottom(UPDATED_MARGIN_BOTTOM)
            .url(UPDATED_URL);
        LayoutDTO layoutDTO = layoutMapper.toDto(updatedLayout);

        restLayoutMockMvc.perform(put("/api/layouts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(layoutDTO)))
            .andExpect(status().isOk());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
        Layout testLayout = layoutList.get(layoutList.size() - 1);
        assertThat(testLayout.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLayout.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLayout.getWidth()).isEqualTo(UPDATED_WIDTH);
        assertThat(testLayout.getHeight()).isEqualTo(UPDATED_HEIGHT);
        assertThat(testLayout.getMarginLeft()).isEqualTo(UPDATED_MARGIN_LEFT);
        assertThat(testLayout.getMarginRight()).isEqualTo(UPDATED_MARGIN_RIGHT);
        assertThat(testLayout.getMarginTop()).isEqualTo(UPDATED_MARGIN_TOP);
        assertThat(testLayout.getMarginBottom()).isEqualTo(UPDATED_MARGIN_BOTTOM);
        assertThat(testLayout.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingLayout() throws Exception {
        int databaseSizeBeforeUpdate = layoutRepository.findAll().size();

        // Create the Layout
        LayoutDTO layoutDTO = layoutMapper.toDto(layout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLayoutMockMvc.perform(put("/api/layouts")
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(layoutDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Layout in the database
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLayout() throws Exception {
        // Initialize the database
        layoutRepository.saveAndFlush(layout);

        int databaseSizeBeforeDelete = layoutRepository.findAll().size();

        // Delete the layout
        restLayoutMockMvc.perform(delete("/api/layouts/{id}", layout.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Layout> layoutList = layoutRepository.findAll();
        assertThat(layoutList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
