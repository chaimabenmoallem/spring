package com.epix.hawkadmin.services;


import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class ElasticService {

    @Autowired
    private RestClient restClient;

    public double searchWithFuzziness(String term, int fuzziness) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> termWithFuzziness = new HashMap<>();
        termWithFuzziness.put("query", term);
        termWithFuzziness.put("fuzziness", fuzziness);

        Map<String, Object> match = new HashMap<>();
        match.put("term", termWithFuzziness);

        Map<String, Object> query = new HashMap<>();
        query.put("match", match);

        Map<String, Object> sumAggregation = new HashMap<>();
        sumAggregation.put("field", "occurrences");

        Map<String, Object> aggs = new HashMap<>();
        aggs.put("total_occurrences", Map.of("sum", sumAggregation));

        Map<String, Object> searchBody = new HashMap<>();
        searchBody.put("query", query);
        searchBody.put("aggs", aggs);

        Request searchRequest = new Request("POST", "/tuning/_search");
        searchRequest.setJsonEntity(objectMapper.writeValueAsString(searchBody));

        Response response = restClient.performRequest(searchRequest);
        JsonNode jsonResponse = objectMapper.readTree(EntityUtils.toString(response.getEntity()));
        double totalOccurrences = jsonResponse.path("aggregations").path("total_occurrences").path("value").asDouble();

        return totalOccurrences;
    }


}
