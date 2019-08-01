package com.logiciel.weeklyshop.repository.search;

import com.logiciel.weeklyshop.domain.ShoppingList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ShoppingList} entity.
 */
public interface ShoppingListSearchRepository extends ElasticsearchRepository<ShoppingList, Long> {
}
