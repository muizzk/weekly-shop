package com.logiciel.weeklyshop.web.rest;

import com.logiciel.weeklyshop.WeeklyShopApp;
import com.logiciel.weeklyshop.domain.ShoppingList;
import com.logiciel.weeklyshop.repository.ShoppingListRepository;
import com.logiciel.weeklyshop.repository.search.ShoppingListSearchRepository;
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
 * Integration tests for the {@Link ShoppingListResource} REST controller.
 */
@SpringBootTest(classes = WeeklyShopApp.class)
public class ShoppingListResourceIT {

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    @Autowired
    private ShoppingListRepository shoppingListRepository;

    /**
     * This repository is mocked in the com.logiciel.weeklyshop.repository.search test package.
     *
     * @see com.logiciel.weeklyshop.repository.search.ShoppingListSearchRepositoryMockConfiguration
     */
    @Autowired
    private ShoppingListSearchRepository mockShoppingListSearchRepository;

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

    private MockMvc restShoppingListMockMvc;

    private ShoppingList shoppingList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShoppingListResource shoppingListResource = new ShoppingListResource(shoppingListRepository, mockShoppingListSearchRepository);
        this.restShoppingListMockMvc = MockMvcBuilders.standaloneSetup(shoppingListResource)
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
    public static ShoppingList createEntity(EntityManager em) {
        ShoppingList shoppingList = new ShoppingList()
            .owner(DEFAULT_OWNER);
        return shoppingList;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingList createUpdatedEntity(EntityManager em) {
        ShoppingList shoppingList = new ShoppingList()
            .owner(UPDATED_OWNER);
        return shoppingList;
    }

    @BeforeEach
    public void initTest() {
        shoppingList = createEntity(em);
    }

    @Test
    @Transactional
    public void createShoppingList() throws Exception {
        int databaseSizeBeforeCreate = shoppingListRepository.findAll().size();

        // Create the ShoppingList
        restShoppingListMockMvc.perform(post("/api/shopping-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shoppingList)))
            .andExpect(status().isCreated());

        // Validate the ShoppingList in the database
        List<ShoppingList> shoppingListList = shoppingListRepository.findAll();
        assertThat(shoppingListList).hasSize(databaseSizeBeforeCreate + 1);
        ShoppingList testShoppingList = shoppingListList.get(shoppingListList.size() - 1);
        assertThat(testShoppingList.getOwner()).isEqualTo(DEFAULT_OWNER);

        // Validate the ShoppingList in Elasticsearch
        verify(mockShoppingListSearchRepository, times(1)).save(testShoppingList);
    }

    @Test
    @Transactional
    public void createShoppingListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shoppingListRepository.findAll().size();

        // Create the ShoppingList with an existing ID
        shoppingList.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShoppingListMockMvc.perform(post("/api/shopping-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shoppingList)))
            .andExpect(status().isBadRequest());

        // Validate the ShoppingList in the database
        List<ShoppingList> shoppingListList = shoppingListRepository.findAll();
        assertThat(shoppingListList).hasSize(databaseSizeBeforeCreate);

        // Validate the ShoppingList in Elasticsearch
        verify(mockShoppingListSearchRepository, times(0)).save(shoppingList);
    }


    @Test
    @Transactional
    public void getAllShoppingLists() throws Exception {
        // Initialize the database
        shoppingListRepository.saveAndFlush(shoppingList);

        // Get all the shoppingListList
        restShoppingListMockMvc.perform(get("/api/shopping-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingList.getId().intValue())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER.toString())));
    }
    
    @Test
    @Transactional
    public void getShoppingList() throws Exception {
        // Initialize the database
        shoppingListRepository.saveAndFlush(shoppingList);

        // Get the shoppingList
        restShoppingListMockMvc.perform(get("/api/shopping-lists/{id}", shoppingList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shoppingList.getId().intValue()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingShoppingList() throws Exception {
        // Get the shoppingList
        restShoppingListMockMvc.perform(get("/api/shopping-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShoppingList() throws Exception {
        // Initialize the database
        shoppingListRepository.saveAndFlush(shoppingList);

        int databaseSizeBeforeUpdate = shoppingListRepository.findAll().size();

        // Update the shoppingList
        ShoppingList updatedShoppingList = shoppingListRepository.findById(shoppingList.getId()).get();
        // Disconnect from session so that the updates on updatedShoppingList are not directly saved in db
        em.detach(updatedShoppingList);
        updatedShoppingList
            .owner(UPDATED_OWNER);

        restShoppingListMockMvc.perform(put("/api/shopping-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShoppingList)))
            .andExpect(status().isOk());

        // Validate the ShoppingList in the database
        List<ShoppingList> shoppingListList = shoppingListRepository.findAll();
        assertThat(shoppingListList).hasSize(databaseSizeBeforeUpdate);
        ShoppingList testShoppingList = shoppingListList.get(shoppingListList.size() - 1);
        assertThat(testShoppingList.getOwner()).isEqualTo(UPDATED_OWNER);

        // Validate the ShoppingList in Elasticsearch
        verify(mockShoppingListSearchRepository, times(1)).save(testShoppingList);
    }

    @Test
    @Transactional
    public void updateNonExistingShoppingList() throws Exception {
        int databaseSizeBeforeUpdate = shoppingListRepository.findAll().size();

        // Create the ShoppingList

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShoppingListMockMvc.perform(put("/api/shopping-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shoppingList)))
            .andExpect(status().isBadRequest());

        // Validate the ShoppingList in the database
        List<ShoppingList> shoppingListList = shoppingListRepository.findAll();
        assertThat(shoppingListList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShoppingList in Elasticsearch
        verify(mockShoppingListSearchRepository, times(0)).save(shoppingList);
    }

    @Test
    @Transactional
    public void deleteShoppingList() throws Exception {
        // Initialize the database
        shoppingListRepository.saveAndFlush(shoppingList);

        int databaseSizeBeforeDelete = shoppingListRepository.findAll().size();

        // Delete the shoppingList
        restShoppingListMockMvc.perform(delete("/api/shopping-lists/{id}", shoppingList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShoppingList> shoppingListList = shoppingListRepository.findAll();
        assertThat(shoppingListList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ShoppingList in Elasticsearch
        verify(mockShoppingListSearchRepository, times(1)).deleteById(shoppingList.getId());
    }

    @Test
    @Transactional
    public void searchShoppingList() throws Exception {
        // Initialize the database
        shoppingListRepository.saveAndFlush(shoppingList);
        when(mockShoppingListSearchRepository.search(queryStringQuery("id:" + shoppingList.getId())))
            .thenReturn(Collections.singletonList(shoppingList));
        // Search the shoppingList
        restShoppingListMockMvc.perform(get("/api/_search/shopping-lists?query=id:" + shoppingList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingList.getId().intValue())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShoppingList.class);
        ShoppingList shoppingList1 = new ShoppingList();
        shoppingList1.setId(1L);
        ShoppingList shoppingList2 = new ShoppingList();
        shoppingList2.setId(shoppingList1.getId());
        assertThat(shoppingList1).isEqualTo(shoppingList2);
        shoppingList2.setId(2L);
        assertThat(shoppingList1).isNotEqualTo(shoppingList2);
        shoppingList1.setId(null);
        assertThat(shoppingList1).isNotEqualTo(shoppingList2);
    }
}
