package com.logiciel.weeklyshop.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ShoppingItemSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ShoppingItemSearchRepositoryMockConfiguration {

    @MockBean
    private ShoppingItemSearchRepository mockShoppingItemSearchRepository;

}
