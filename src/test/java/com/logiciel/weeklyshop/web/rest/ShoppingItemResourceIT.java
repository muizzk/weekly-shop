package com.logiciel.weeklyshop.web.rest;

import com.logiciel.weeklyshop.WeeklyShopApp;
import com.logiciel.weeklyshop.domain.ShoppingItem;
import com.logiciel.weeklyshop.domain.Category;
import com.logiciel.weeklyshop.repository.ShoppingItemRepository;
import com.logiciel.weeklyshop.repository.search.ShoppingItemSearchRepository;
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

import com.logiciel.weeklyshop.domain.enumeration.Origin;
/**
 * Integration tests for the {@Link ShoppingItemResource} REST controller.
 */
@SpringBootTest(classes = WeeklyShopApp.class)
public class ShoppingItemResourceIT {

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_QUANTITY = "AAAAAAAAAA";
    private static final String UPDATED_QUANTITY = "BBBBBBBBBB";

    private static final Origin DEFAULT_ORIGIN = Origin.MANUALLY_ENTERED;
    private static final Origin UPDATED_ORIGIN = Origin.STAPLE;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    @Autowired
    private ShoppingItemRepository shoppingItemRepository;

    /**
     * This repository is mocked in the com.logiciel.weeklyshop.repository.search test package.
     *
     * @see com.logiciel.weeklyshop.repository.search.ShoppingItemSearchRepositoryMockConfiguration
     */
    @Autowired
    private ShoppingItemSearchRepository mockShoppingItemSearchRepository;

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

    private MockMvc restShoppingItemMockMvc;

    private ShoppingItem shoppingItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ShoppingItemResource shoppingItemResource = new ShoppingItemResource(shoppingItemRepository, mockShoppingItemSearchRepository);
        this.restShoppingItemMockMvc = MockMvcBuilders.standaloneSetup(shoppingItemResource)
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
    public static ShoppingItem createEntity(EntityManager em) {
        ShoppingItem shoppingItem = new ShoppingItem()
            .owner(DEFAULT_OWNER)
            .name(DEFAULT_NAME)
            .quantity(DEFAULT_QUANTITY)
            .origin(DEFAULT_ORIGIN)
            .deleted(DEFAULT_DELETED);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        shoppingItem.setCategory(category);
        return shoppingItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingItem createUpdatedEntity(EntityManager em) {
        ShoppingItem shoppingItem = new ShoppingItem()
            .owner(UPDATED_OWNER)
            .name(UPDATED_NAME)
            .quantity(UPDATED_QUANTITY)
            .origin(UPDATED_ORIGIN)
            .deleted(UPDATED_DELETED);
        // Add required entity
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            category = CategoryResourceIT.createUpdatedEntity(em);
            em.persist(category);
            em.flush();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        shoppingItem.setCategory(category);
        return shoppingItem;
    }

    @BeforeEach
    public void initTest() {
        shoppingItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createShoppingItem() throws Exception {
        int databaseSizeBeforeCreate = shoppingItemRepository.findAll().size();

        // Create the ShoppingItem
        restShoppingItemMockMvc.perform(post("/api/shopping-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shoppingItem)))
            .andExpect(status().isCreated());

        // Validate the ShoppingItem in the database
        List<ShoppingItem> shoppingItemList = shoppingItemRepository.findAll();
        assertThat(shoppingItemList).hasSize(databaseSizeBeforeCreate + 1);
        ShoppingItem testShoppingItem = shoppingItemList.get(shoppingItemList.size() - 1);
        assertThat(testShoppingItem.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testShoppingItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testShoppingItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testShoppingItem.getOrigin()).isEqualTo(DEFAULT_ORIGIN);
        assertThat(testShoppingItem.isDeleted()).isEqualTo(DEFAULT_DELETED);

        // Validate the ShoppingItem in Elasticsearch
        verify(mockShoppingItemSearchRepository, times(1)).save(testShoppingItem);
    }

    @Test
    @Transactional
    public void createShoppingItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shoppingItemRepository.findAll().size();

        // Create the ShoppingItem with an existing ID
        shoppingItem.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShoppingItemMockMvc.perform(post("/api/shopping-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shoppingItem)))
            .andExpect(status().isBadRequest());

        // Validate the ShoppingItem in the database
        List<ShoppingItem> shoppingItemList = shoppingItemRepository.findAll();
        assertThat(shoppingItemList).hasSize(databaseSizeBeforeCreate);

        // Validate the ShoppingItem in Elasticsearch
        verify(mockShoppingItemSearchRepository, times(0)).save(shoppingItem);
    }


    @Test
    @Transactional
    public void checkOwnerIsRequired() throws Exception {
        int databaseSizeBeforeTest = shoppingItemRepository.findAll().size();
        // set the field null
        shoppingItem.setOwner(null);

        // Create the ShoppingItem, which fails.

        restShoppingItemMockMvc.perform(post("/api/shopping-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shoppingItem)))
            .andExpect(status().isBadRequest());

        List<ShoppingItem> shoppingItemList = shoppingItemRepository.findAll();
        assertThat(shoppingItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = shoppingItemRepository.findAll().size();
        // set the field null
        shoppingItem.setName(null);

        // Create the ShoppingItem, which fails.

        restShoppingItemMockMvc.perform(post("/api/shopping-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shoppingItem)))
            .andExpect(status().isBadRequest());

        List<ShoppingItem> shoppingItemList = shoppingItemRepository.findAll();
        assertThat(shoppingItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShoppingItems() throws Exception {
        // Initialize the database
        shoppingItemRepository.saveAndFlush(shoppingItem);

        // Get all the shoppingItemList
        restShoppingItemMockMvc.perform(get("/api/shopping-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.toString())))
            .andExpect(jsonPath("$.[*].origin").value(hasItem(DEFAULT_ORIGIN.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getShoppingItem() throws Exception {
        // Initialize the database
        shoppingItemRepository.saveAndFlush(shoppingItem);

        // Get the shoppingItem
        restShoppingItemMockMvc.perform(get("/api/shopping-items/{id}", shoppingItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(shoppingItem.getId().intValue()))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.toString()))
            .andExpect(jsonPath("$.origin").value(DEFAULT_ORIGIN.toString()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingShoppingItem() throws Exception {
        // Get the shoppingItem
        restShoppingItemMockMvc.perform(get("/api/shopping-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShoppingItem() throws Exception {
        // Initialize the database
        shoppingItemRepository.saveAndFlush(shoppingItem);

        int databaseSizeBeforeUpdate = shoppingItemRepository.findAll().size();

        // Update the shoppingItem
        ShoppingItem updatedShoppingItem = shoppingItemRepository.findById(shoppingItem.getId()).get();
        // Disconnect from session so that the updates on updatedShoppingItem are not directly saved in db
        em.detach(updatedShoppingItem);
        updatedShoppingItem
            .owner(UPDATED_OWNER)
            .name(UPDATED_NAME)
            .quantity(UPDATED_QUANTITY)
            .origin(UPDATED_ORIGIN)
            .deleted(UPDATED_DELETED);

        restShoppingItemMockMvc.perform(put("/api/shopping-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedShoppingItem)))
            .andExpect(status().isOk());

        // Validate the ShoppingItem in the database
        List<ShoppingItem> shoppingItemList = shoppingItemRepository.findAll();
        assertThat(shoppingItemList).hasSize(databaseSizeBeforeUpdate);
        ShoppingItem testShoppingItem = shoppingItemList.get(shoppingItemList.size() - 1);
        assertThat(testShoppingItem.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testShoppingItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testShoppingItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testShoppingItem.getOrigin()).isEqualTo(UPDATED_ORIGIN);
        assertThat(testShoppingItem.isDeleted()).isEqualTo(UPDATED_DELETED);

        // Validate the ShoppingItem in Elasticsearch
        verify(mockShoppingItemSearchRepository, times(1)).save(testShoppingItem);
    }

    @Test
    @Transactional
    public void updateNonExistingShoppingItem() throws Exception {
        int databaseSizeBeforeUpdate = shoppingItemRepository.findAll().size();

        // Create the ShoppingItem

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShoppingItemMockMvc.perform(put("/api/shopping-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(shoppingItem)))
            .andExpect(status().isBadRequest());

        // Validate the ShoppingItem in the database
        List<ShoppingItem> shoppingItemList = shoppingItemRepository.findAll();
        assertThat(shoppingItemList).hasSize(databaseSizeBeforeUpdate);

        // Validate the ShoppingItem in Elasticsearch
        verify(mockShoppingItemSearchRepository, times(0)).save(shoppingItem);
    }

    @Test
    @Transactional
    public void deleteShoppingItem() throws Exception {
        // Initialize the database
        shoppingItemRepository.saveAndFlush(shoppingItem);

        int databaseSizeBeforeDelete = shoppingItemRepository.findAll().size();

        // Delete the shoppingItem
        restShoppingItemMockMvc.perform(delete("/api/shopping-items/{id}", shoppingItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShoppingItem> shoppingItemList = shoppingItemRepository.findAll();
        assertThat(shoppingItemList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the ShoppingItem in Elasticsearch
        verify(mockShoppingItemSearchRepository, times(1)).deleteById(shoppingItem.getId());
    }

    @Test
    @Transactional
    public void searchShoppingItem() throws Exception {
        // Initialize the database
        shoppingItemRepository.saveAndFlush(shoppingItem);
        when(mockShoppingItemSearchRepository.search(queryStringQuery("id:" + shoppingItem.getId())))
            .thenReturn(Collections.singletonList(shoppingItem));
        // Search the shoppingItem
        restShoppingItemMockMvc.perform(get("/api/_search/shopping-items?query=id:" + shoppingItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].origin").value(hasItem(DEFAULT_ORIGIN.toString())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShoppingItem.class);
        ShoppingItem shoppingItem1 = new ShoppingItem();
        shoppingItem1.setId(1L);
        ShoppingItem shoppingItem2 = new ShoppingItem();
        shoppingItem2.setId(shoppingItem1.getId());
        assertThat(shoppingItem1).isEqualTo(shoppingItem2);
        shoppingItem2.setId(2L);
        assertThat(shoppingItem1).isNotEqualTo(shoppingItem2);
        shoppingItem1.setId(null);
        assertThat(shoppingItem1).isNotEqualTo(shoppingItem2);
    }
}
