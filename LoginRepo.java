package com.epix.hawkadmin.repository;

import com.epix.hawkadmin.model.Login;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface LoginRepo extends ElasticsearchRepository<Login,String> {
}
