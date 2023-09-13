package com.epix.hawkadmin.repository;

import com.epix.hawkadmin.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepo extends ElasticsearchRepository<User,String> {
    User findByEmail(String email);
}
