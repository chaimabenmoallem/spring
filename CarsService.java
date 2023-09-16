package com.epix.hawkadmin.services;

import com.epix.hawkadmin.repository.CarsRepo;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.apache.http.util.EntityUtils;

import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import com.google.gson.JsonObject;
import org.apache.http.entity.StringEntity;
import com.google.gson.JsonParser;


import java.io.IOException;
import java.util.*;


@Service
public class CarsService {

    @Autowired
    private RestClient restClient;

    @Autowired
    private CarsRepo carsRepo;

    public boolean isKnownMakeSynonym(String term) throws IOException {
        String original = convertSynonymToOriginal(term, "make");
        // If the converted term is not the same as the original term, then it's a synonym.
        return !term.equalsIgnoreCase(original);
    }

    public boolean isKnownModelSynonym(String term) throws IOException {
        String original = convertSynonymToOriginal(term, "model");
        // If the converted term is not the same as the original term, then it's a synonym.
        return !term.equalsIgnoreCase(original);
    }

    public boolean isKnownEnergySynonym(String term) throws IOException {
        String original = convertSynonymToOriginal(term, "energy");
        // If the converted term is not the same as the original term, then it's a synonym.
        return !term.equalsIgnoreCase(original);
    }


    public boolean isKnownTransmissionTypeSynonym(String term) throws IOException {
        String original = convertSynonymToOriginal(term, "transmission_type");
        // If the converted term is not the same as the original term, then it's a synonym.
        return !term.equalsIgnoreCase(original);
    }

    public boolean isKnownMake(String make) throws IOException {
        return isKnownAttribute("make", make);
    }

    public boolean isKnownModel(String make) throws IOException {
        return isKnownAttribute("model", make);
    }

    public boolean isKnownEnergy(String make) throws IOException {
        return isKnownAttribute("energy", make);
    }

    public boolean isKnownTransmissionType(String make) throws IOException {
        return isKnownAttribute("transmission_type", make);
    }

    private boolean isKnownAttribute(String attributeType, String value) throws IOException {
        String jsonString = "{" +
                "\"query\": {" +
                "   \"bool\": {" +
                "       \"must\": [{" +
                "           \"match\": {" +
                "               \"" + attributeType + "\": \"" + value + "\"" +
                "           }" +
                "       }]" +
                "   }" +
                "}" +
                "}";

        Request request = new Request("GET", "/cars/_search");
        request.setJsonEntity(jsonString);
        Response response = restClient.performRequest(request);

        String responseBody = EntityUtils.toString(response.getEntity());
        JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
        int count = jsonNode.path("hits").path("total").path("value").asInt();

        return count > 0;
    }

    public String convertSynonymToOriginal(String term, String attributeType) throws IOException {
        Map<String, String> params = Collections.emptyMap();
        String jsonString = "{" +
                "\"query\": {" +
                "   \"bool\": {" +
                "       \"must\": [{" +
                "           \"match\": {" +
                "               \"synonyms\": \"" + term + "\"" +
                "           }" +
                "       }, {" +
                "           \"match\": {" +
                "               \"key\": \"" + attributeType + "\"" +
                "           }" +
                "       }]" +
                "   }" +
                "}" +
                "}";
        Request request = new Request("GET", "/dictionary_20230816_1/_search");
        request.setJsonEntity(jsonString);
        Response response = restClient.performRequest(request);

        String responseBody = EntityUtils.toString(response.getEntity());
        JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
        JsonNode hitsNode = jsonNode.path("hits").path("hits");


        if (hitsNode.isArray() && hitsNode.size() > 0) {
            return hitsNode.get(0).path("_source").path("values").asText();
        }
        return term;  // Return the original term if no synonym was found
    }

    public List<String> autocompleteCombined(String prefix) {
        String endpoint = "/cars/_search";

        // Build the search JSON payload targeting combined_autocomplete
        String jsonString = "{"
                + "\"suggest\": {"
                + "\"combined-suggest\": {"
                + "\"prefix\": \"" + prefix + "\","
                + "\"completion\": {"
                + "\"field\": \"combined_autocomplete\","
                + "\"size\": 10"
                + "}"
                + "}"
                + "}"
                + "}";

        List<String> suggestions = new ArrayList<>();

        try {
            HttpEntity entity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
            Request request = new Request("POST", endpoint);
            request.setEntity(entity);
            Response response = restClient.performRequest(request);

            // Parse the response
            String responseBody = EntityUtils.toString(response.getEntity());
            JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

            JsonArray options = jsonObject.getAsJsonObject("suggest")
                    .getAsJsonArray("combined-suggest")
                    .get(0)
                    .getAsJsonObject()
                    .getAsJsonArray("options");

            Set<String> uniqueSuggestions = new HashSet<>();
            for (JsonElement option : options) {
                uniqueSuggestions.add(option.getAsJsonObject().get("text").getAsString());
            }
            suggestions.addAll(uniqueSuggestions);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return suggestions;
    }














   /* public String convertToOriginalEnergy(String term) throws IOException {
        Map<String, String> params = Collections.emptyMap();


        //Ce code construit une requête Elasticsearch au format JSON
        // qui recherchera dans l' dictionary_20230816_1index
        // les documents dont le synonymschamp correspond au fichier term.

        String jsonString = "{" +
                "\"query\": {" +
                "   \"match\": {" +
                "       \"synonyms\": \"" + term + "\"" +
                "   }" +
                "}" +
                "}";


        //La requête Elasticsearch est configurée pour rechercher ( _search) l' dictionary_20230816_1index.
        //La requête JSON précédemment construite est ajoutée à la requête.
        //La requête est ensuite exécutée à l'aide du restClient, et la réponse est stockée dans la responsevariable.

        Request request = new Request("GET", "/dictionary_20230816_1/_search");
        request.setJsonEntity(jsonString);
        Response response = restClient.performRequest(request);

        //Le corps de la réponse (qui est au format JSON) est extrait du fichier response.
        //Le corps est ensuite analysé en un JsonNodeobjet, ce qui permet de parcourir et d'extraire facilement les champs JSON imbriqués.
        //La hitsNodevariable est spécifiquement affectée à la liste des résultats (documents correspondant à la requête) dans la réponse.

        String responseBody = EntityUtils.toString(response.getEntity());
        JsonNode jsonNode = new ObjectMapper().readTree(responseBody);
        JsonNode hitsNode = jsonNode.path("hits").path("hits");


        //S'il y a des résultats ( hits) dans la réponse, il extrait la valeur du valueschamp du premier accès.
        // Dans le contexte de cette méthode, cette valeur représente le terme original du synonyme.
        //Ce terme original est ensuite renvoyé.

        if (hitsNode.isArray() && hitsNode.size() > 0) {
            return hitsNode.get(0).path("_source").path("values").asText();
        }

        //Si le terme n'est pas trouvé comme synonyme dans l'index Elasticsearch,
        // la méthode renverra l'original termfourni comme argument.
        return term;
    }*/



}
