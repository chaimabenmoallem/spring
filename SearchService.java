package com.epix.hawkadmin.services;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    private RestClient client;

    @Autowired
    private ObjectMapper objectMapper;

    public List<Map<String, Object>> decomposeAndSearch(String query) throws IOException {
        List<String> terms = new ArrayList<>(Arrays.asList(query.split(" ")));
        List<Map<String, Object>> matches = new ArrayList<>();

        while (!terms.isEmpty()) {
            String searchTerm = String.join(" ", terms);
            matches = searchElasticsearch(searchTerm);

            if (!matches.isEmpty()) {
                return matches;
            }

            terms.remove(terms.size() - 1);
        }

        return matches;
    }

    private List<Map<String, Object>> searchElasticsearch(String term) throws IOException {
        String queryString = String.format("{ \"query\": { \"multi_match\": { \"query\": \"%s\", \"fields\": [\"make\", \"model\", \"energy\", \"transmission_type\", \"body_type\"] } } }", term);
        Request request = new Request("GET", "/cars/_search");
        request.setJsonEntity(queryString);

        Response response = client.performRequest(request);
        Map<String, Object> responseBody = objectMapper.readValue(response.getEntity().getContent(), Map.class);

        List<Map<String, Object>> searchHits = (List<Map<String, Object>>) ((Map<String, Object>) responseBody.get("hits")).get("hits");

        List<Map<String, Object>> sources = new ArrayList<>();
        for (Map<String, Object> hit : searchHits) {
            sources.add((Map<String, Object>) hit.get("_source"));
        }

        return sources;
    }
}
