package pt.up.hs.sampling.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pt.up.hs.sampling.SamplingApp;
import pt.up.hs.sampling.config.SecurityBeanOverrideConfiguration;
import pt.up.hs.sampling.domain.Protocol;
import pt.up.hs.sampling.domain.Sample;
import pt.up.hs.sampling.repository.ProtocolRepository;
import pt.up.hs.sampling.service.ProtocolService;
import pt.up.hs.sampling.service.dto.ProtocolDTO;
import pt.up.hs.sampling.service.mapper.ProtocolMapper;
import pt.up.hs.sampling.web.rest.errors.ExceptionTranslator;
import pt.up.hs.sampling.service.ProtocolQueryService;

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

import static org.hamcrest.Matchers.*;
import static pt.up.hs.sampling.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProtocolResource} REST controller.
 */
@SpringBootTest(classes = {SecurityBeanOverrideConfiguration.class, SamplingApp.class})
public class ProtocolResourceIT {

    private static final Long DEFAULT_PROJECT_ID = 1L;
    private static final Long OTHER_PROJECT_ID = 2L;

    private static final Integer DEFAULT_PAGE_NUMBER = 1;
    private static final Integer UPDATED_PAGE_NUMBER = 2;
    private static final Integer SMALLER_PAGE_NUMBER = 1 - 1;

    @Autowired
    private ProtocolRepository protocolRepository;

    @Autowired
    private ProtocolMapper protocolMapper;

    @Autowired
    private ProtocolService protocolService;

    @Autowired
    private ProtocolQueryService protocolQueryService;

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

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc restProtocolMockMvc;

    private Protocol protocol;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProtocolResource protocolResource = new ProtocolResource(protocolService, protocolQueryService);
        this.restProtocolMockMvc = MockMvcBuilders.standaloneSetup(protocolResource)
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
    public static Protocol createEntity(EntityManager em) {
        return new Protocol()
            .pageNumber(DEFAULT_PAGE_NUMBER)
            .projectId(DEFAULT_PROJECT_ID);
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Protocol createUpdatedEntity(EntityManager em) {
        return new Protocol()
            .pageNumber(UPDATED_PAGE_NUMBER)
            .projectId(OTHER_PROJECT_ID);
    }

    @BeforeEach
    public void initTest() {
        protocol = createEntity(em);
    }

    @Test
    @Transactional
    public void createProtocol() throws Exception {
        int databaseSizeBeforeCreate = protocolRepository.findAll().size();

        // Create the Protocol
        ProtocolDTO protocolDTO = protocolMapper.toDto(protocol);
        restProtocolMockMvc.perform(post("/api/projects/{projectId}/protocols", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(protocolDTO)))
            .andExpect(status().isCreated());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeCreate + 1);
        Protocol testProtocol = protocolList.get(protocolList.size() - 1);
        assertThat(testProtocol.getPageNumber()).isEqualTo(DEFAULT_PAGE_NUMBER);
        assertThat(testProtocol.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void createProtocolWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = protocolRepository.findAll().size();

        // Create the Protocol with an existing ID
        protocol.setId(1L);
        ProtocolDTO protocolDTO = protocolMapper.toDto(protocol);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProtocolMockMvc.perform(post("/api/projects/{projectId}/protocols", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(protocolDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllProtocols() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList
        restProtocolMockMvc.perform(get("/api/projects/{projectId}/protocols?sort=id,desc", DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(protocol.getId().intValue())))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.intValue())));
    }

    @Test
    @Transactional
    public void getProtocol() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get the protocol
        restProtocolMockMvc.perform(get("/api/projects/{projectId}/protocols/{id}", DEFAULT_PROJECT_ID, protocol.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(protocol.getId().intValue()))
            .andExpect(jsonPath("$.pageNumber").value(DEFAULT_PAGE_NUMBER))
            .andExpect(jsonPath("$.projectId").value(DEFAULT_PROJECT_ID.intValue()));
    }


    @Test
    @Transactional
    public void getProtocolsByIdFiltering() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        Long id = protocol.getId();

        defaultProtocolShouldBeFound("id.equals=" + id);
        defaultProtocolShouldNotBeFound("id.notEquals=" + id);

        defaultProtocolShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProtocolShouldNotBeFound("id.greaterThan=" + id);

        defaultProtocolShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProtocolShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProtocolsByPageNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where pageNumber equals to DEFAULT_PAGE_NUMBER
        defaultProtocolShouldBeFound("pageNumber.equals=" + DEFAULT_PAGE_NUMBER);

        // Get all the protocolList where pageNumber equals to UPDATED_PAGE_NUMBER
        defaultProtocolShouldNotBeFound("pageNumber.equals=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProtocolsByPageNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where pageNumber not equals to DEFAULT_PAGE_NUMBER
        defaultProtocolShouldNotBeFound("pageNumber.notEquals=" + DEFAULT_PAGE_NUMBER);

        // Get all the protocolList where pageNumber not equals to UPDATED_PAGE_NUMBER
        defaultProtocolShouldBeFound("pageNumber.notEquals=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProtocolsByPageNumberIsInShouldWork() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where pageNumber in DEFAULT_PAGE_NUMBER or UPDATED_PAGE_NUMBER
        defaultProtocolShouldBeFound("pageNumber.in=" + DEFAULT_PAGE_NUMBER + "," + UPDATED_PAGE_NUMBER);

        // Get all the protocolList where pageNumber equals to UPDATED_PAGE_NUMBER
        defaultProtocolShouldNotBeFound("pageNumber.in=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProtocolsByPageNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where pageNumber is not null
        defaultProtocolShouldBeFound("pageNumber.specified=true");

        // Get all the protocolList where pageNumber is null
        defaultProtocolShouldNotBeFound("pageNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllProtocolsByPageNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where pageNumber is greater than or equal to DEFAULT_PAGE_NUMBER
        defaultProtocolShouldBeFound("pageNumber.greaterThanOrEqual=" + DEFAULT_PAGE_NUMBER);

        // Get all the protocolList where pageNumber is greater than or equal to UPDATED_PAGE_NUMBER
        defaultProtocolShouldNotBeFound("pageNumber.greaterThanOrEqual=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProtocolsByPageNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where pageNumber is less than or equal to DEFAULT_PAGE_NUMBER
        defaultProtocolShouldBeFound("pageNumber.lessThanOrEqual=" + DEFAULT_PAGE_NUMBER);

        // Get all the protocolList where pageNumber is less than or equal to SMALLER_PAGE_NUMBER
        defaultProtocolShouldNotBeFound("pageNumber.lessThanOrEqual=" + SMALLER_PAGE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProtocolsByPageNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where pageNumber is less than DEFAULT_PAGE_NUMBER
        defaultProtocolShouldNotBeFound("pageNumber.lessThan=" + DEFAULT_PAGE_NUMBER);

        // Get all the protocolList where pageNumber is less than UPDATED_PAGE_NUMBER
        defaultProtocolShouldBeFound("pageNumber.lessThan=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllProtocolsByPageNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        // Get all the protocolList where pageNumber is greater than DEFAULT_PAGE_NUMBER
        defaultProtocolShouldNotBeFound("pageNumber.greaterThan=" + DEFAULT_PAGE_NUMBER);

        // Get all the protocolList where pageNumber is greater than SMALLER_PAGE_NUMBER
        defaultProtocolShouldBeFound("pageNumber.greaterThan=" + SMALLER_PAGE_NUMBER);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProtocolShouldBeFound(String filter) throws Exception {
        restProtocolMockMvc.perform(get("/api/projects/{projectId}/protocols?sort=id,desc&" + filter, DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(protocol.getId().intValue())))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].projectId").value(hasItem(DEFAULT_PROJECT_ID.intValue())));

        // Check, that the count call also returns 1
        restProtocolMockMvc.perform(get("/api/projects/{projectId}/protocols/count?sort=id,desc&" + filter, DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProtocolShouldNotBeFound(String filter) throws Exception {
        restProtocolMockMvc.perform(get("/api/projects/{projectId}/protocols?sort=id,desc&" + filter, DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProtocolMockMvc.perform(get("/api/projects/{projectId}/protocols/count?sort=id,desc&" + filter, DEFAULT_PROJECT_ID))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProtocol() throws Exception {
        // Get the protocol
        restProtocolMockMvc.perform(get("/api/projects/{projectId}/protocols/{id}", DEFAULT_PROJECT_ID, Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProtocol() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        int databaseSizeBeforeUpdate = protocolRepository.findAll().size();

        // Update the protocol
        Protocol updatedProtocol = protocolRepository.findById(protocol.getId()).get();
        // Disconnect from session so that the updates on updatedProtocol are not directly saved in db
        em.detach(updatedProtocol);
        updatedProtocol
            .pageNumber(UPDATED_PAGE_NUMBER)
            .projectId(OTHER_PROJECT_ID);
        ProtocolDTO protocolDTO = protocolMapper.toDto(updatedProtocol);

        restProtocolMockMvc.perform(put("/api/projects/{projectId}/protocols", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(protocolDTO)))
            .andExpect(status().isOk());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeUpdate);
        Protocol testProtocol = protocolList.get(protocolList.size() - 1);
        assertThat(testProtocol.getPageNumber()).isEqualTo(UPDATED_PAGE_NUMBER);
        assertThat(testProtocol.getProjectId()).isEqualTo(DEFAULT_PROJECT_ID);
    }

    @Test
    @Transactional
    public void updateNonExistingProtocol() throws Exception {
        int databaseSizeBeforeUpdate = protocolRepository.findAll().size();

        // Create the Protocol
        ProtocolDTO protocolDTO = protocolMapper.toDto(protocol);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProtocolMockMvc.perform(put("/api/projects/{projectId}/protocols", DEFAULT_PROJECT_ID)
            .contentType(TestUtil.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(protocolDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Protocol in the database
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProtocol() throws Exception {
        // Initialize the database
        protocolRepository.saveAndFlush(protocol);

        int databaseSizeBeforeDelete = protocolRepository.findAll().size();

        // Delete the protocol
        restProtocolMockMvc.perform(delete("/api/projects/{projectId}/protocols/{id}", DEFAULT_PROJECT_ID, protocol.getId())
            .accept(TestUtil.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Protocol> protocolList = protocolRepository.findAll();
        assertThat(protocolList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void importProtocol() throws Exception {
        // read file
        byte[] contentPage = TestUtil.readFileFromResourcesFolder("data/protocols/page_full.data");
        MockMultipartFile filePage = new MockMultipartFile("file", "page_full.data", null, contentPage);

        // Import the protocols
        restProtocolMockMvc
            .perform(
                MockMvcRequestBuilders
                    .multipart("/api/projects/{projectId}/protocols/import", DEFAULT_PROJECT_ID)
                    .file(filePage)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.total").value(1))
            .andExpect(jsonPath("$.invalid").value(0))
            .andExpect(jsonPath("$.data").value(hasSize(1)))
            /*.andExpect(jsonPath("$.data.[0].strokes").value(hasSize(643)))
            .andExpect(jsonPath("$.data.[0].strokes[0].startTime").value(isA(Long.class)))
            .andExpect(jsonPath("$.data.[0].strokes[0].endTime").value(isA(Long.class)))
            .andExpect(jsonPath("$.data.[0].strokes[0].dots").value(hasSize(97)))
            .andExpect(jsonPath("$.data.[0].strokes[0].dots.[*].x").value(hasItems(isA(Double.class), isA(Double.class), isA(Double.class))))
            .andExpect(jsonPath("$.data.[0].strokes[0].dots.[*].y").value(hasItems(isA(Double.class), isA(Double.class), isA(Double.class))))
            .andExpect(jsonPath("$.data.[0].strokes[0].dots.[*].timestamp").value(hasItems(isA(Long.class), isA(Long.class), isA(Long.class))))
            .andExpect(jsonPath("$.data.[0].strokes[0].dots.[*].type").value(hasItems("DOWN", "DOWN", "DOWN")))*/;
    }

    @Test
    @Transactional
    public void bulkImportProtocol() throws Exception {
        // read file
        byte[] contentPageEmpty = TestUtil.readFileFromResourcesFolder("data/protocols/page_empty.data");
        byte[] contentPageFull = TestUtil.readFileFromResourcesFolder("data/protocols/page_full.data");
        MockMultipartFile filePageEmpty = new MockMultipartFile("file", "page_empty.data", null, contentPageEmpty);
        MockMultipartFile filePageFull = new MockMultipartFile("file", "page_full.data", null, contentPageFull);

        // Import the protocols
        restProtocolMockMvc
            .perform(
                MockMvcRequestBuilders
                    .multipart("/api/projects/{projectId}/protocols/import", DEFAULT_PROJECT_ID)
                    .file(filePageEmpty)
                    .file(filePageFull)
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.total").value(2))
            .andExpect(jsonPath("$.invalid").value(0))
            .andExpect(jsonPath("$.data").value(hasSize(2)))
            .andExpect(jsonPath("$.data.[*].strokes.[*].protocolId").value(everyItem(isA(Integer.class))))
            .andExpect(jsonPath("$.data.[*].strokes.[*].dots.[*].strokeId").value(everyItem(isA(Integer.class))));
    }
}
