package com.epix.hawkadmin.controller;

import com.epix.hawkadmin.model.DictionaryUpdateRequest;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import java.util.Collections;

@CrossOrigin(origins = "*")
@RequestMapping("/api")
@RestController
public class DictionaryUpdateController {

    @Autowired
    private RestClient restClient;

    @PostMapping("/update-dictionary")
    public ResponseEntity<String> updateDictionary(@RequestBody DictionaryUpdateRequest request) {
        try {
            // Log received request payload
            System.out.println("Received request with Key: " + request.getKey() + ", Value: " + request.getValue() + ", Synonym: " + request.getSynonym());

            // Create JSON for search request
            String searchJson = "{ \"query\": { \"bool\": { \"must\": [ " +
                    "{ \"term\": { \"key.keyword\": \"" + request.getKey() + "\" } }," +
                    "{ \"term\": { \"values.keyword\": \"" + request.getValue() + "\" } }" +
                    "] } } }";

            HttpEntity entity = new NStringEntity(searchJson, ContentType.APPLICATION_JSON);

            Request esRequest = new Request("GET", "/dictionary_20230816_1/_search");
            esRequest.setJsonEntity(searchJson);
            Response searchResponse = restClient.performRequest(esRequest);



            String searchResponseBody = EntityUtils.toString(searchResponse.getEntity());
            JsonObject jsonObject = new JsonParser().parse(searchResponseBody).getAsJsonObject();
            int totalHits = jsonObject.getAsJsonObject("hits").getAsJsonObject("total").getAsJsonPrimitive("value").getAsInt();

            if (totalHits > 0) {
                // Get ID of the first match
                String docId = jsonObject.getAsJsonObject("hits").getAsJsonArray("hits").get(0).getAsJsonObject().getAsJsonPrimitive("_id").getAsString();

                // Log update action
                System.out.println("Updating existing document with ID: " + docId);

                // Update existing document
                String updateJson = "{ \"doc\": { \"synonyms\": \"" + request.getSynonym() + "\" } }";
                entity = new NStringEntity(updateJson, ContentType.APPLICATION_JSON);

                Request updateRequest = new Request("POST", "/dictionary_20230816_1/_update/" + docId);
                updateRequest.setEntity(entity);
                Response updateResponse = restClient.performRequest(updateRequest);

                return ResponseEntity.ok("{\"message\": \"Dictionary entry updated successfully!\"}");
            } else {
                // Log insert action
                System.out.println("Inserting new dictionary entry.");

                // Add new document
                String indexJson = "{ \"key\": \"" + request.getKey() + "\", \"values\": \"" + request.getValue() + "\", \"synonyms\": \"" + request.getSynonym() + "\" }";
                entity = new NStringEntity(indexJson, ContentType.APPLICATION_JSON);

                Request indexRequest = new Request("POST", "/dictionary_20230816_1/_doc");
                indexRequest.setEntity(entity);
                Response indexResponse = restClient.performRequest(indexRequest);

                String indexResponseBody = EntityUtils.toString(indexResponse.getEntity());
                System.out.println("Elasticsearch index response: " + indexResponseBody);


                return ResponseEntity.ok("{\"message\": \"New dictionary entry added successfully!\"}");
            }
        } catch (Exception e) {
            System.out.println("Error updating dictionary: " + e.getMessage()); // log error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating dictionary: " + e.getMessage());
        }
    }
}
