package com.epix.hawkadmin.repository;

import com.epix.hawkadmin.model.ChartData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ChartRepo extends ElasticsearchRepository<ChartData,String> {

}
