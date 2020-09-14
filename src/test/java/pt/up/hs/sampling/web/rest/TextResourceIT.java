package pt.up.hs.sampling.web.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;
import pt.up.hs.sampling.SamplingApp;
import pt.up.hs.sampling.config.SecurityBeanOverrideConfiguration;
import pt.up.hs.sampling.domain.Text;
import pt.up.hs.sampling.repository.TextRepository;
import pt.up.hs.sampling.service.TextQueryService;
import pt.up.hs.sampling.service.TextService;
import pt.up.hs.sampling.service.dto.TextDTO;
import pt.up.hs.sampling.service.mapper.TextMapper;
import pt.up.hs.sampling.web.rest.errors.ExceptionTranslator;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pt.up.hs.sampling.web.rest.TestUtil.createFormattingConversionService;

/**
 * Integration tests for the {@link TextResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SamplingApp.class})
public class TextResourceIT {

    private static final Long DEFAULT_PROJECT_ID = 1L;
    private static final Long OTHER_PROJECT_ID = 2L;

    private static final String DEFAULT_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TEXT = "BBBBBBBBBB";

    private static final String SAMPLE_TEXT = "Re: Preciso conversar com alg" +
        "uém\nEu não sei por onde começar, minha vida ta uma bagunça, me sin" +
        "to só sem rumo. Meu pai usa drogas, minha mãe é uma egoísta, disse " +
        "que vai embora, que vai ser melhor assim, quer q eu more de favor n" +
        "a casa dos outros e quer seguir a vida dela, e eu? Eu fico pra trás" +
        ", eu fico por minha total e própria conta, acabei de me formar no e" +
        "nsino médio, ainda não achei emprego, e to com medo, tinha tantos p" +
        "lanos para o futuro, eu queria ser tanta coisa, mas não tenho apoio" +
        " de ninguém, ninguém pra me ajudar, eu sei que eu já deveria saber " +
        "me virar, sei que ela não tem obrigação de me da um teto e tudo mai" +
        "s...mas me sinto sendo jogada pra fora...além disso não só tem eu, " +
        "tenho mais duas irmãs mais nova e ela quer fazer o mesmo com as out" +
        "ras duas, desde pequena  cuido da minhas irmãs, e agora me sinto im" +
        "potente não tenho um teto pra dar a elas, me sinto um fracasso.";

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

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTextShouldBeFound(String filter) throws Exception {
        restTextMockMvc.perform(get("/api/projects/{projectId}/texts?sort=id,desc&" + filter, DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(text.getId().intValue())))
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

    @Test
    @Transactional
    public void bulkImportEmptyTexts() throws Exception {
        // read file
        byte[] docxContentEmpty = TestUtil.readFileFromResourcesFolder("data/texts/empty.docx");
        MockMultipartFile docxFileEmpty = new MockMultipartFile("file", "empty.docx", null, docxContentEmpty);
        byte[] odtContentEmpty = TestUtil.readFileFromResourcesFolder("data/texts/empty.odt");
        MockMultipartFile odtFileEmpty = new MockMultipartFile("file", "empty.odt", null, odtContentEmpty);
        byte[] txtContentEmpty = TestUtil.readFileFromResourcesFolder("data/texts/empty.txt");
        MockMultipartFile txtFileEmpty = new MockMultipartFile("file", "empty.txt", null, txtContentEmpty);

        // Import the texts
        restTextMockMvc
            .perform(
                MockMvcRequestBuilders
                    .multipart("/api/projects/{projectId}/texts/import", DEFAULT_PROJECT_ID)
                    .file(docxFileEmpty)
                    .file(odtFileEmpty)
                    .file(txtFileEmpty)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.total").value(3))
            .andExpect(jsonPath("$.invalid").value(3))
            .andExpect(jsonPath("$.data").value(hasSize(0)));
    }

    @Test
    @Transactional
    public void bulkImportTexts() throws Exception {
        // read files
        byte[] docxContent = TestUtil.readFileFromResourcesFolder("data/texts/sample.docx");
        MockMultipartFile docxFile = new MockMultipartFile("file", "sample.docx", null, docxContent);
        byte[] odtContent = TestUtil.readFileFromResourcesFolder("data/texts/sample.odt");
        MockMultipartFile odtFile = new MockMultipartFile("file", "sample.odt", null, odtContent);
        byte[] txtContent = TestUtil.readFileFromResourcesFolder("data/texts/sample.txt");
        MockMultipartFile txtFile = new MockMultipartFile("file", "sample.txt", null, txtContent);

        // Import the protocols
        restTextMockMvc
            .perform(
                MockMvcRequestBuilders
                    .multipart("/api/projects/{projectId}/texts/import", DEFAULT_PROJECT_ID)
                    .file(docxFile)
                    .file(odtFile)
                    .file(txtFile)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.total").value(3))
            .andExpect(jsonPath("$.invalid").value(0))
            .andExpect(jsonPath("$.data").value(hasSize(3)))
            .andExpect(jsonPath("$.data.[*].projectId").value(everyItem(is(DEFAULT_PROJECT_ID.intValue()))))
            .andExpect(jsonPath("$.data.[*].text").value(everyItem(is(SAMPLE_TEXT))));
    }

    @Test
    @Transactional
    public void bulkImportTextsWithEmpty() throws Exception {
        // read files
        byte[] docxContent = TestUtil.readFileFromResourcesFolder("data/texts/sample.docx");
        MockMultipartFile docxFile = new MockMultipartFile("file", "sample.docx", null, docxContent);
        byte[] odtContent = TestUtil.readFileFromResourcesFolder("data/texts/sample.odt");
        MockMultipartFile odtFile = new MockMultipartFile("file", "sample.odt", null, odtContent);
        byte[] txtContent = TestUtil.readFileFromResourcesFolder("data/texts/empty.txt");
        MockMultipartFile txtFile = new MockMultipartFile("file", "empty.txt", null, txtContent);

        // Import the protocols
        restTextMockMvc
            .perform(
                MockMvcRequestBuilders
                    .multipart("/api/projects/{projectId}/texts/import", DEFAULT_PROJECT_ID)
                    .file(docxFile)
                    .file(odtFile)
                    .file(txtFile)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.total").value(3))
            .andExpect(jsonPath("$.invalid").value(1))
            .andExpect(jsonPath("$.data").value(hasSize(2)))
            .andExpect(jsonPath("$.data.[*].projectId").value(everyItem(is(DEFAULT_PROJECT_ID.intValue()))))
            .andExpect(jsonPath("$.data.[*].text").value(everyItem(is(SAMPLE_TEXT))));
    }
}
