package com.logiciel.weeklyshop.repository.search;

import com.logiciel.weeklyshop.domain.Staple;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Staple} entity.
 */
public interface StapleSearchRepository extends ElasticsearchRepository<Staple, Long> {
}
