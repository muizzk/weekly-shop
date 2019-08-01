package com.logiciel.weeklyshop.web.rest;

import com.logiciel.weeklyshop.WeeklyShopApp;
import com.logiciel.weeklyshop.domain.Staple;
import com.logiciel.weeklyshop.repository.StapleRepository;
import com.logiciel.weeklyshop.repository.search.StapleSearchRepository;
import com.logiciel.weeklyshop.web.rest.errors.ExceptionTranslator;

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
import java.util.Collections;
import java.util.List;

import static com.logiciel.weeklyshop.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link StapleResource} REST controller.
 */
@SpringBootTest(classes = WeeklyShopApp.class)
public class StapleResourceIT {

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final String DEFAULT_QUANTITY = "AAAAAAAAAA";
    private static final String UPDATED_QUANTITY = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private StapleRepository stapleRepository;

    /**
     * This repository is mocked in the com.logiciel.weeklyshop.repository.search test package.
     *
     * @see com.logiciel.weeklyshop.repository.search.StapleSearchRepositoryMockConfiguration
     */
    @Autowired
    private StapleSearchRepository mockStapleSearchRepository;

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

    private MockMvc restStapleMockMvc;

    private Staple staple;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StapleResource stapleResource = new StapleResource(stapleRepository, mockStapleSearchRepository);
        this.restStapleMockMvc = MockMvcBuilders.standaloneSetup(stapleResource)
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
    public static Staple createEntity(EntityManager em) {
        Staple staple = new Staple()
            .owner(DEFAULT_OWNER)
            .quantity(DEFAULT_QUANTITY)
            .name(DEFAULT_NAME);
        return staple;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Staple createUpdatedEntity(EntityManager em) {
        Staple staple = new Staple()
            .owner(UPDATED_OWNER)
            .quantity(UPDATED_QUANTITY)
            .name(UPDATED_NAME);
        return staple;
    }

    @BeforeEach
    public void initTest() {
        staple = createEntity(em);
    }

    @Test
    @Transactional
    public void createStaple() throws Exception {
        int databaseSizeBeforeCreate = stapleRepository.findAll().size();

        // Create the Staple
        restStapleMockMvc.perform(post("/api/staples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(staple)))
            .andExpect(status().isCreated());

        // Validate the Staple in the database
        List<Staple> stapleList = stapleRepository.findAll();
        assertThat(stapleList).hasSize(databaseSizeBeforeCreate + 1);
        Staple testStaple = stapleList.get(stapleList.size() - 1);
        assertThat(testStaple.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testStaple.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testStaple.getName()).isEqualTo(DEFAULT_NAME);

        // Validate the Staple in Elasticsearch
        verify(mockStapleSearchRepository, times(1)).save(testStaple);
    }

    @Test
    @Transactional
    public void createStapleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stapleRepository.findAll().size();

        // Create the Staple with an existing ID
        staple.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStapleMockMvc.perform(post("/api/staples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(staple)))
            .andExpect(status().isBadRequest());

        // Validate the Staple in the database
        List<Staple> stapleList = stapleRepository.findAll();
        assertThat(stapleList).hasSize(databaseSizeBeforeCreate);

        // Validate the Staple in Elasticsearch
        verify(mockStapleSearchRepository, times(0)).save(staple);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = stapleRepository.findAll().size();
        // set the field null
        staple.setName(null);

        // Create the Staple, which fails.

        restStapleMockMvc.perform(post("/api/staples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(staple)))
            .andExpect(status().isBadRequest());

        List<Staple> stapleList = stapleRepository.findAll();
        assertThat(stapleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStaples() throws Exception {
        // Initialize the database
        stapleRepository.saveAndFlush(staple);

        // Get all the stapleList
        restStapleMockMvc.perform(get("/api/staples?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staple.getId().intValue())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getStaple() throws Exception {
        // Initialize the database
        stapleRepository.saveAndFlush(staple);

        // Get the staple
        restStapleMockMvc.perform(get("/api/staples/{id}", staple.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(staple.getId().intValue()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStaple() throws Exception {
        // Get the staple
        restStapleMockMvc.perform(get("/api/staples/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStaple() throws Exception {
        // Initialize the database
        stapleRepository.saveAndFlush(staple);

        int databaseSizeBeforeUpdate = stapleRepository.findAll().size();

        // Update the staple
        Staple updatedStaple = stapleRepository.findById(staple.getId()).get();
        // Disconnect from session so that the updates on updatedStaple are not directly saved in db
        em.detach(updatedStaple);
        updatedStaple
            .owner(UPDATED_OWNER)
            .quantity(UPDATED_QUANTITY)
            .name(UPDATED_NAME);

        restStapleMockMvc.perform(put("/api/staples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStaple)))
            .andExpect(status().isOk());

        // Validate the Staple in the database
        List<Staple> stapleList = stapleRepository.findAll();
        assertThat(stapleList).hasSize(databaseSizeBeforeUpdate);
        Staple testStaple = stapleList.get(stapleList.size() - 1);
        assertThat(testStaple.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testStaple.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testStaple.getName()).isEqualTo(UPDATED_NAME);

        // Validate the Staple in Elasticsearch
        verify(mockStapleSearchRepository, times(1)).save(testStaple);
    }

    @Test
    @Transactional
    public void updateNonExistingStaple() throws Exception {
        int databaseSizeBeforeUpdate = stapleRepository.findAll().size();

        // Create the Staple

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStapleMockMvc.perform(put("/api/staples")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(staple)))
            .andExpect(status().isBadRequest());

        // Validate the Staple in the database
        List<Staple> stapleList = stapleRepository.findAll();
        assertThat(stapleList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Staple in Elasticsearch
        verify(mockStapleSearchRepository, times(0)).save(staple);
    }

    @Test
    @Transactional
    public void deleteStaple() throws Exception {
        // Initialize the database
        stapleRepository.saveAndFlush(staple);

        int databaseSizeBeforeDelete = stapleRepository.findAll().size();

        // Delete the staple
        restStapleMockMvc.perform(delete("/api/staples/{id}", staple.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Staple> stapleList = stapleRepository.findAll();
        assertThat(stapleList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Staple in Elasticsearch
        verify(mockStapleSearchRepository, times(1)).deleteById(staple.getId());
    }

    @Test
    @Transactional
    public void searchStaple() throws Exception {
        // Initialize the database
        stapleRepository.saveAndFlush(staple);
        when(mockStapleSearchRepository.search(queryStringQuery("id:" + staple.getId())))
            .thenReturn(Collections.singletonList(staple));
        // Search the staple
        restStapleMockMvc.perform(get("/api/_search/staples?query=id:" + staple.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staple.getId().intValue())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Staple.class);
        Staple staple1 = new Staple();
        staple1.setId(1L);
        Staple staple2 = new Staple();
        staple2.setId(staple1.getId());
        assertThat(staple1).isEqualTo(staple2);
        staple2.setId(2L);
        assertThat(staple1).isNotEqualTo(staple2);
        staple1.setId(null);
        assertThat(staple1).isNotEqualTo(staple2);
    }
}
