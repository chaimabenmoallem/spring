package com.epix.hawkadmin.repository;

import com.epix.hawkadmin.model.Tuning;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


import java.util.List;
import java.util.Optional;

public interface TuningRepo extends ElasticsearchRepository<Tuning,String> {

    // Optional<Tuning> findByTerm(String term);

    /*@Query("{\"bool\": {\"must\": [{\"match\": {\"term\": {\"query\": \"?0\", \"fuzziness\": \"?1\"}}}]}}")
    List<Tuning> findByTermFuzzy(String term, int fuzziness);*/

}