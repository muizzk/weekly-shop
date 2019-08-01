package com.logiciel.weeklyshop.repository.search;

import com.logiciel.weeklyshop.domain.ShoppingItem;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link ShoppingItem} entity.
 */
public interface ShoppingItemSearchRepository extends ElasticsearchRepository<ShoppingItem, Long> {
}
