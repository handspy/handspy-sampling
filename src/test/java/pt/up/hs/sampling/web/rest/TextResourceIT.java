package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.SamplingApp;
import pt.up.hs.sampling.config.SecurityBeanOverrideConfiguration;
import pt.up.hs.sampling.domain.Text;
import pt.up.hs.sampling.domain.Sample;
import pt.up.hs.sampling.repository.TextRepository;
import pt.up.hs.sampling.service.TextService;
import pt.up.hs.sampling.service.dto.TextDTO;
import pt.up.hs.sampling.service.mapper.TextMapper;
import pt.up.hs.sampling.web.rest.errors.ExceptionTranslator;
import pt.up.hs.sampling.service.dto.TextCriteria;
import pt.up.hs.sampling.service.TextQueryService;

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
 * Integration tests for the {@link TextResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SamplingApp.class})
public class TextResourceIT {

    private static final Long DEFAULT_PROJECT_ID = 1L;
    private static final Long OTHER_PROJECT_ID = 2L;

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    @Autowired
    private TextRepository textRepository;

    @Autowired
    private TextMapper textMapper;

    @Autowired
    private TextService textService;

    @Autowired
    private TextQueryService textQueryService;

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

    private MockMvc restTextMockMvc;

    private Text text;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TextResource textResource = new TextResource(textService, textQueryService);
        this.restTextMockMvc = MockMvcBuilders.standaloneSetup(textResource)
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
    public static Text createEntity(EntityManager em) {
        return new Text()
            .text(DEFAULT_TEXT)
            .projectId(DEFAULT_PROJECT_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Text createUpdatedEntity(EntityManager em) {
        return new Text()
            .text(UPDATED_TEXT)
            .projectId(OTHER_PROJECT_ID);
    }

    @BeforeEach
    public void initTest() {
        text = createEntity(em);
    }

    @Test
    @Transactional
    public void createText() throws Exception {
        int databaseSizeBeforeCreate = textRepository.findAll().size();

        // Create the Text
        TextDTO textDTO = textMapper.toDto(text);
        restTextMockMvc.perform(post("/api/projects/{projectId}/texts", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(textDTO)))
            .andExpect(status().isCreated());

        // Validate the Text in the database
        List<Text> textList = textRepository.findAll();
        assertThat(textList).hasSize(databaseSizeBeforeCreate + 1);
        Text testText = textList.get(textList.size() - 1);
        assertThat(testText.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testText.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void createTextWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = textRepository.findAll().size();

        // Create the Text with an existing ID
        text.setId(1L);
        TextDTO textDTO = textMapper.toDto(text);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTextMockMvc.perform(post("/api/projects/{projectId}/texts", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(textDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Text in the database
        List<Text> textList = textRepository.findAll();
        assertThat(textList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkProjectIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = textRepository.findAll().size();
        // set the field null
        text.setProjectId(null);

        // Create the Text, which fails.
        TextDTO textDTO = textMapper.toDto(text);

        restTextMockMvc.perform(post("/api/projects/{projectId}/texts", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(textDTO)))
            .andExpect(status().isBadRequest());

        List<Text> textList = textRepository.findAll();
        assertThat(textList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTexts() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        // Get all the textList
        restTextMockMvc.perform(get("/api/projects/{projectId}/texts?sort=id,desc", DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(text.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.intValue())));
    }

    @Test
    @Transactional
    public void getText() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        // Get the text
        restTextMockMvc.perform(get("/api/projects/{projectId}/texts/{id}", DEFAULT_PROJECT_ID, text.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(text.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.projectId").value(DEFAULT_PROJECT_ID.intValue()));
    }


    @Test
    @Transactional
    public void getTextsByIdFiltering() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        Long id = text.getId();

        defaultTextShouldBeFound("id.equals=" + id);
        defaultTextShouldNotBeFound("id.notEquals=" + id);

        defaultTextShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTextShouldNotBeFound("id.greaterThan=" + id);

        defaultTextShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTextShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTextsByTextIsEqualToSomething() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        // Get all the textList where text equals to DEFAULT_TEXT
        defaultTextShouldBeFound("text.equals=" + DEFAULT_TEXT);

        // Get all the textList where text equals to UPDATED_TEXT
        defaultTextShouldNotBeFound("text.equals=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllTextsByTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        // Get all the textList where text not equals to DEFAULT_TEXT
        defaultTextShouldNotBeFound("text.notEquals=" + DEFAULT_TEXT);

        // Get all the textList where text not equals to UPDATED_TEXT
        defaultTextShouldBeFound("text.notEquals=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllTextsByTextIsInShouldWork() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        // Get all the textList where text in DEFAULT_TEXT or UPDATED_TEXT
        defaultTextShouldBeFound("text.in=" + DEFAULT_TEXT + "," + UPDATED_TEXT);

        // Get all the textList where text equals to UPDATED_TEXT
        defaultTextShouldNotBeFound("text.in=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllTextsByTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        // Get all the textList where text is not null
        defaultTextShouldBeFound("text.specified=true");

        // Get all the textList where text is null
        defaultTextShouldNotBeFound("text.specified=false");
    }
                @Test
    @Transactional
    public void getAllTextsByTextContainsSomething() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        // Get all the textList where text contains DEFAULT_TEXT
        defaultTextShouldBeFound("text.contains=" + DEFAULT_TEXT);

        // Get all the textList where text contains UPDATED_TEXT
        defaultTextShouldNotBeFound("text.contains=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllTextsByTextNotContainsSomething() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        // Get all the textList where text does not contain DEFAULT_TEXT
        defaultTextShouldNotBeFound("text.doesNotContain=" + DEFAULT_TEXT);

        // Get all the textList where text does not contain UPDATED_TEXT
        defaultTextShouldBeFound("text.doesNotContain=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllTextsBySampleIsEqualToSomething() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);
        Sample sample = SampleResourceIT.createEntity(em);
        em.persist(sample);
        em.flush();
        text.setSample(sample);
        textRepository.saveAndFlush(text);
        Long sampleId = sample.getId();

        // Get all the textList where sample equals to sampleId
        defaultTextShouldBeFound("sampleId.equals=" + sampleId);

        // Get all the textList where sample equals to sampleId + 1
        defaultTextShouldNotBeFound("sampleId.equals=" + (sampleId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTextShouldBeFound(String filter) throws Exception {
        restTextMockMvc.perform(get("/api/projects/{projectId}/texts?sort=id,desc&" + filter, DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(text.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.intValue())));

        // Check, that the count call also returns 1
        restTextMockMvc.perform(get("/api/projects/{projectId}/texts/count?sort=id,desc&" + filter, DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTextShouldNotBeFound(String filter) throws Exception {
        restTextMockMvc.perform(get("/api/projects/{projectId}/texts?sort=id,desc&" + filter, DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTextMockMvc.perform(get("/api/projects/{projectId}/texts/count?sort=id,desc&" + filter, DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingText() throws Exception {
        // Get the text
        restTextMockMvc.perform(get("/api/projects/{projectId}/texts/{id}", DEFAULT_PROJECT_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateText() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        int databaseSizeBeforeUpdate = textRepository.findAll().size();

        // Update the text
        Text updatedText = textRepository.findById(text.getId()).get();
        // Disconnect from session so that the updates on updatedText are not directly saved in db
        em.detach(updatedText);
        updatedText
            .text(UPDATED_TEXT);
        TextDTO textDTO = textMapper.toDto(updatedText);

        restTextMockMvc.perform(put("/api/projects/{projectId}/texts", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(textDTO)))
            .andExpect(status().isOk());

        // Validate the Text in the database
        List<Text> textList = textRepository.findAll();
        assertThat(textList).hasSize(databaseSizeBeforeUpdate);
        Text testText = textList.get(textList.size() - 1);
        assertThat(testText.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testText.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingText() throws Exception {
        int databaseSizeBeforeUpdate = textRepository.findAll().size();

        // Create the Text
        TextDTO textDTO = textMapper.toDto(text);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTextMockMvc.perform(put("/api/projects/{projectId}/texts", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(textDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Text in the database
        List<Text> textList = textRepository.findAll();
        assertThat(textList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteText() throws Exception {
        // Initialize the database
        textRepository.saveAndFlush(text);

        int databaseSizeBeforeDelete = textRepository.findAll().size();

        // Delete the text
        restTextMockMvc.perform(delete("/api/projects/{projectId}/texts/{id}", DEFAULT_PROJECT_ID, text.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Text> textList = textRepository.findAll();
        assertThat(textList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
