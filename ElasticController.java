package com.epix.hawkadmin.controller;

import com.epix.hawkadmin.model.SearchRequest;
import com.epix.hawkadmin.services.ElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@CrossOrigin(origins = "*")
@RequestMapping("/search")
@RestController
public class ElasticController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticController.class);
    @Autowired

    private ElasticService elasticsearchService;

    @PostMapping
    public ResponseEntity<?> searchWithFuzziness(@RequestBody SearchRequest request) {
        double totalOccurrences;
        try {
            totalOccurrences = elasticsearchService.searchWithFuzziness(request.getTerm(), request.getFuzziness());
        } catch (IOException e) {
            LOGGER.error("Error while processing the request:", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(totalOccurrences, HttpStatus.OK);
    }
}
