package com.epix.hawkadmin.controller;

import com.epix.hawkadmin.services.SearchService;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/request")
    public List<Map<String, Object>> search(@RequestParam String query) throws IOException {
        return searchService.decomposeAndSearch(query);
    }


    private List<String> searchTerms = new ArrayList<>();


    @PostMapping("/term")
    public ResponseEntity<Void> saveTerm(@RequestBody String term) {
        System.out.println("Received term from search project: " + term);
        searchTerms.add(term);  // Add the term to the list
        return ResponseEntity.ok().build();
    }

    @GetMapping("/term")
    public ResponseEntity<String> getTerm() {
        String termsAsString = String.join("\n", searchTerms); // Join terms with newline
        System.out.println("Sending terms to table project:\n" + termsAsString);
        return ResponseEntity.ok(termsAsString);
    }

}
