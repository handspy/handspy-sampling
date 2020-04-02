package pt.up.hs.sampling.web.rest;

import pt.up.hs.sampling.SamplingApp;
import pt.up.hs.sampling.config.SecurityBeanOverrideConfiguration;
import pt.up.hs.sampling.domain.Note;
import pt.up.hs.sampling.domain.Sample;
import pt.up.hs.sampling.repository.NoteRepository;
import pt.up.hs.sampling.service.NoteService;
import pt.up.hs.sampling.service.dto.NoteDTO;
import pt.up.hs.sampling.service.mapper.NoteMapper;
import pt.up.hs.sampling.web.rest.errors.ExceptionTranslator;
import pt.up.hs.sampling.service.dto.NoteCriteria;
import pt.up.hs.sampling.service.NoteQueryService;

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
 * Integration tests for the {@link NoteResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SamplingApp.class})
public class NoteResourceIT {

    private static final Long DEFAULT_PROJECT_ID = 1L;
    private static final Long OTHER_PROJECT_ID = 2L;

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SELF = false;
    private static final Boolean UPDATED_SELF = true;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteQueryService noteQueryService;

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

    private MockMvc restNoteMockMvc;

    private static Sample defaultSample;

    private Note note;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final NoteResource noteResource = new NoteResource(noteService, noteQueryService);
        this.restNoteMockMvc = MockMvcBuilders.standaloneSetup(noteResource)
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
    public static Note createEntity(EntityManager em) {
        return new Note()
            .text(DEFAULT_TEXT)
            .self(DEFAULT_SELF)
            .sample(defaultSample);
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Note createUpdatedEntity(EntityManager em) {
        return new Note()
            .text(UPDATED_TEXT)
            .self(UPDATED_SELF)
            .sample(defaultSample);
    }

    /**
     * Get sample to link note.
     *
     * @param em {@link EntityManager} entity manager.
     * @return {@link Sample} sample.
     */
    public static Sample getSample(EntityManager em) {
        Sample sample;
        if (TestUtil.findAll(em, Sample.class).isEmpty()) {
            sample = SampleResourceIT.createEntity(em);
            em.persist(sample);
            em.flush();
        } else {
            sample = TestUtil.findAll(em, Sample.class).get(0);
        }
        return sample;
    }

    @BeforeEach
    public void initTest() {
        defaultSample = getSample(em);
        note = createEntity(em);
    }

    @Test
    @Transactional
    public void createNote() throws Exception {
        int databaseSizeBeforeCreate = noteRepository.findAll().size();

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);
        restNoteMockMvc.perform(post("/api/projects/{projectId}/samples/{sampleId}/notes", DEFAULT_PROJECT_ID, defaultSample.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(noteDTO)))
            .andExpect(status().isCreated());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeCreate + 1);
        Note testNote = noteList.get(noteList.size() - 1);
        assertThat(testNote.getText()).isEqualTo(DEFAULT_TEXT);
        assertThat(testNote.isSelf()).isEqualTo(DEFAULT_SELF);
    }

    @Test
    @Transactional
    public void createNoteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = noteRepository.findAll().size();

        // Create the Note with an existing ID
        note.setId(1L);
        NoteDTO noteDTO = noteMapper.toDto(note);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNoteMockMvc.perform(post("/api/projects/{projectId}/samples/{sampleId}/notes", DEFAULT_PROJECT_ID, defaultSample.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(noteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllNotes() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList
        restNoteMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/notes?sort=id,desc", DEFAULT_PROJECT_ID, defaultSample.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(note.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].self").value(hasItem(DEFAULT_SELF)));
    }

    @Test
    @Transactional
    public void getNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get the note
        restNoteMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/notes/{id}", DEFAULT_PROJECT_ID, defaultSample.getId(), note.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(note.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT))
            .andExpect(jsonPath("$.self").value(DEFAULT_SELF));
    }


    @Test
    @Transactional
    public void getNotesByIdFiltering() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        Long id = note.getId();

        defaultNoteShouldBeFound("id.equals=" + id);
        defaultNoteShouldNotBeFound("id.notEquals=" + id);

        defaultNoteShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNoteShouldNotBeFound("id.greaterThan=" + id);

        defaultNoteShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNoteShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllNotesByTextIsEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where text equals to DEFAULT_TEXT
        defaultNoteShouldBeFound("text.equals=" + DEFAULT_TEXT);

        // Get all the noteList where text equals to UPDATED_TEXT
        defaultNoteShouldNotBeFound("text.equals=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllNotesByTextIsNotEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where text not equals to DEFAULT_TEXT
        defaultNoteShouldNotBeFound("text.notEquals=" + DEFAULT_TEXT);

        // Get all the noteList where text not equals to UPDATED_TEXT
        defaultNoteShouldBeFound("text.notEquals=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllNotesByTextIsInShouldWork() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where text in DEFAULT_TEXT or UPDATED_TEXT
        defaultNoteShouldBeFound("text.in=" + DEFAULT_TEXT + "," + UPDATED_TEXT);

        // Get all the noteList where text equals to UPDATED_TEXT
        defaultNoteShouldNotBeFound("text.in=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllNotesByTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where text is not null
        defaultNoteShouldBeFound("text.specified=true");

        // Get all the noteList where text is null
        defaultNoteShouldNotBeFound("text.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotesByTextContainsSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where text contains DEFAULT_TEXT
        defaultNoteShouldBeFound("text.contains=" + DEFAULT_TEXT);

        // Get all the noteList where text contains UPDATED_TEXT
        defaultNoteShouldNotBeFound("text.contains=" + UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void getAllNotesByTextNotContainsSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where text does not contain DEFAULT_TEXT
        defaultNoteShouldNotBeFound("text.doesNotContain=" + DEFAULT_TEXT);

        // Get all the noteList where text does not contain UPDATED_TEXT
        defaultNoteShouldBeFound("text.doesNotContain=" + UPDATED_TEXT);
    }


    @Test
    @Transactional
    public void getAllNotesBySelfIsEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where self equals to DEFAULT_SELF
        defaultNoteShouldBeFound("self.equals=" + DEFAULT_SELF);

        // Get all the noteList where self equals to UPDATED_SELF
        defaultNoteShouldNotBeFound("self.equals=" + UPDATED_SELF);
    }

    @Test
    @Transactional
    public void getAllNotesBySelfIsNotEqualToSomething() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where self not equals to DEFAULT_SELF
        defaultNoteShouldNotBeFound("self.notEquals=" + DEFAULT_SELF);

        // Get all the noteList where self not equals to UPDATED_SELF
        defaultNoteShouldBeFound("self.notEquals=" + UPDATED_SELF);
    }

    @Test
    @Transactional
    public void getAllNotesBySelfIsInShouldWork() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where self in DEFAULT_SELF or UPDATED_SELF
        defaultNoteShouldBeFound("self.in=" + DEFAULT_SELF + "," + UPDATED_SELF);

        // Get all the noteList where self equals to UPDATED_SELF
        defaultNoteShouldNotBeFound("self.in=" + UPDATED_SELF);
    }

    @Test
    @Transactional
    public void getAllNotesBySelfIsNullOrNotNull() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList where self is not null
        defaultNoteShouldBeFound("self.specified=true");

        // Get all the noteList where self is null
        defaultNoteShouldNotBeFound("self.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNoteShouldBeFound(String filter) throws Exception {
        restNoteMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/notes?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, defaultSample.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(note.getId().intValue())))
            .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT)))
            .andExpect(jsonPath("$.[*].self").value(hasItem(DEFAULT_SELF)));

        // Check, that the count call also returns 1
        restNoteMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/notes/count?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, defaultSample.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNoteShouldNotBeFound(String filter) throws Exception {
        restNoteMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/notes?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, defaultSample.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNoteMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/notes/count?sort=id,desc&" + filter, DEFAULT_PROJECT_ID, defaultSample.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingNote() throws Exception {
        // Get the note
        restNoteMockMvc.perform(get("/api/projects/{projectId}/samples/{sampleId}/notes/{id}", DEFAULT_PROJECT_ID, defaultSample.getId(), Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        int databaseSizeBeforeUpdate = noteRepository.findAll().size();

        // Update the note
        Note updatedNote = noteRepository.findById(note.getId()).get();
        // Disconnect from session so that the updates on updatedNote are not directly saved in db
        em.detach(updatedNote);
        updatedNote
            .text(UPDATED_TEXT)
            .self(UPDATED_SELF);
        NoteDTO noteDTO = noteMapper.toDto(updatedNote);

        restNoteMockMvc.perform(put("/api/projects/{projectId}/samples/{sampleId}/notes", DEFAULT_PROJECT_ID, defaultSample.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(noteDTO)))
            .andExpect(status().isOk());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
        Note testNote = noteList.get(noteList.size() - 1);
        assertThat(testNote.getText()).isEqualTo(UPDATED_TEXT);
        assertThat(testNote.isSelf()).isEqualTo(UPDATED_SELF);
    }

    @Test
    @Transactional
    public void updateNonExistingNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().size();

        // Create the Note
        NoteDTO noteDTO = noteMapper.toDto(note);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNoteMockMvc.perform(put("/api/projects/{projectId}/samples/{sampleId}/notes", DEFAULT_PROJECT_ID, defaultSample.getId())
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(noteDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        int databaseSizeBeforeDelete = noteRepository.findAll().size();

        // Delete the note
        restNoteMockMvc.perform(delete("/api/projects/{projectId}/samples/{sampleId}/notes/{id}", DEFAULT_PROJECT_ID, defaultSample.getId(), note.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
