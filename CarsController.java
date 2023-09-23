

package com.epix.hawkadmin.controller;

import com.epix.hawkadmin.services.CarsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.epix.hawkadmin.model.Cars;
import com.epix.hawkadmin.repository.CarsRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.stream.StreamSupport;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin(origins = "*")
@RequestMapping("/drop/api/related-options")
@RestController
public class CarsController {

    @Autowired
    private CarsRepo carsRepo;

    @Autowired
    private CarsService carsService;

    private static final Logger logger = LoggerFactory.getLogger(CarsController.class);

    @GetMapping
    public List<String> getRelatedOptions(@RequestParam String type) {
        Iterable<Cars> carsIterable = carsRepo.findAll();
        Stream<Cars> carStream = StreamSupport.stream(carsIterable.spliterator(), false);

        switch (type) {
            case "energy":
                return carStream
                        .map(Cars::getEnergy)
                        .distinct()
                        .collect(Collectors.toList());
            case "make":
                return carStream
                        .map(Cars::getMake)
                        .distinct()
                        .collect(Collectors.toList());
            case "model":
                return carStream
                        .map(Cars::getModel)
                        .distinct()
                        .collect(Collectors.toList());
            case "transmission_type":
                return carStream
                        .map(Cars::getTransmissionType)
                        .distinct()
                        .collect(Collectors.toList());
            case "body_type":
                return carStream
                        .map(Cars::getBodyType)
                        .distinct()
                        .collect(Collectors.toList());
            default:
                return Collections.emptyList();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Cars>> getAllCars() {
        Iterable<Cars> iterableCars = carsRepo.findAll();
        List<Cars> cars = StreamSupport.stream(iterableCars.spliterator(), false).collect(Collectors.toList());

        for (Cars car : cars) {
            // Construct the image URL based on make, model, energy, and transmission type
            String imageUrl = "/images/"
                    + car.getMake()
                    + "_" + car.getModel()
                    + "_" + car.getEnergy()
                    + "_" + car.getTransmissionType()
                    + ".jpg";

            car.setImageUrl(imageUrl);
        }

        return ResponseEntity.ok(cars);
    }


    //Il attend un seul Stringargument term, qui est le terme de recherche flexible.

    @GetMapping("/search/{term}")
    public ResponseEntity<List<Map<String, Object>>> getCarDetailsByTerm(@PathVariable String term) throws IOException {
        logger.info("Entered getCarDetailsByTerm with term: {}", term);

        List<String> terms = Arrays.asList(term.split("_"));

        // Generate all term combinations starting from the largest
        List<List<String>> termCombinations = generateTermCombinations(terms);

        // Process each combination until a match is found or all combinations are tried
        for (List<String> combination : termCombinations) {
            logger.info("Trying combination: {}", String.join("_", combination));
            List<Cars> foundCars = searchWithTerms(combination);

            if (!foundCars.isEmpty()) {
                logger.info("Result found and sent back for combination: {}", term);
                // Process and return all matches
                List<Map<String, Object>> carDetailsList = foundCars.stream()
                        .map(this::createCarDetails)
                        .collect(Collectors.toList());
                return ResponseEntity.ok(carDetailsList);  // Return immediately when a match is found
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }




    // New method to generate term combinations
    private List<List<String>> generateTermCombinations(List<String> terms) {
        List<List<String>> combinations = new ArrayList<>();

        for (int length = terms.size(); length > 0; length--) {
            for (int i = 0; i + length <= terms.size(); i++) {
                List<String> combination = terms.subList(i, i + length);
                combinations.add(combination);
                logger.info("Generated combination: {}", String.join("_", combination));
            }
        }

        return combinations;
    }



    // searchWithTerms method to return multiple matches
    private List<Cars> searchWithTerms(List<String> terms) throws IOException {
        String make = null;
        String model = null;
        String energy = null;
        String transmissionType = null;

        for (String part : terms) {
            if (carsService.isKnownMakeSynonym(part) || carsService.isKnownMake(part)) {
                make = carsService.convertSynonymToOriginal(part, "make");
            } else if (carsService.isKnownModelSynonym(part) || carsService.isKnownModel(part)) {
                model = carsService.convertSynonymToOriginal(part, "model");
            } else if (carsService.isKnownEnergySynonym(part) || carsService.isKnownEnergy(part)) {
                energy = carsService.convertSynonymToOriginal(part, "energy");
            } else if (carsService.isKnownTransmissionTypeSynonym(part) || carsService.isKnownTransmissionType(part)) {
                transmissionType = carsService.convertSynonymToOriginal(part, "transmissionType");
            }
            System.out.println("Term being processed: " + part);
            System.out.println("Recognized make: " + make);
            System.out.println("Recognized model: " + model);
            System.out.println("Recognized energy: " + energy);
            System.out.println("Recognized transmissionType: " + transmissionType);
        }

        // Enhanced logging to log the recognized attributes before querying the database.
        logger.info("Final recognized attributes before querying the database:");
        logger.info("Make: {}", make);
        logger.info("Model: {}", model);
        logger.info("Energy: {}", energy);
        logger.info("Transmission Type: {}", transmissionType);

        return carsService.findByFlexibleAttributes(make, model, energy, transmissionType);  // Using the dynamic query service method
    }



    // Helper method to create car details map
    private Map<String, Object> createCarDetails(Cars car) {
        String imageUrl = "/images/"
                + (car.getMake() != null ? car.getMake() + "_" : "")
                + (car.getModel() != null ? car.getModel() + "_" : "")
                + (car.getEnergy() != null ? car.getEnergy() + "_" : "")
                + (car.getTransmissionType() != null ? car.getTransmissionType() : "")
                + ".jpg";

        Map<String, Object> carDetails = new HashMap<>();
        carDetails.put("imageUrl", imageUrl);
        carDetails.put("make", car.getMake());
        carDetails.put("model", car.getModel());
        carDetails.put("energy", car.getEnergy());
        carDetails.put("transmission_type", car.getTransmissionType());

        return carDetails;
    }

    @GetMapping("/autocomplete/combined")
    public ResponseEntity<List<String>> autocompleteCombined(@RequestParam("prefix") String prefix) {
        List<String> combinedSuggestions = carsService.autocompleteCombined(prefix);
        return ResponseEntity.ok(combinedSuggestions);
    }


    // Update your recursiveSearch method to return multiple matches
   /*private List<Cars> recursiveSearch(List<String> terms) throws IOException {

        if (terms.isEmpty()) {
            return Collections.emptyList();
        }

        // Search with current terms
        List<Cars> results = searchWithTerms(terms);
        if (!results.isEmpty()) {
            return results;  // If there's a match, immediately return it.
        }

        // If no match was found, strip the last term and search again.
        List<String> newTerms = new ArrayList<>(terms);
        newTerms.remove(newTerms.size() - 1);
        return recursiveSearch(newTerms);
    }*/


}












/*package com.epix.hawkadmin.controller;

import com.epix.hawkadmin.services.CarsService;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import com.epix.hawkadmin.model.Cars;
import com.epix.hawkadmin.repository.CarsRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.stream.StreamSupport;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CrossOrigin(origins = "*")
@RequestMapping("/drop/api/related-options")
@RestController
public class CarsController {

    @Autowired
    private CarsRepo carsRepo;

    @Autowired
    private CarsService carsService;



    @GetMapping
    public List<String> getRelatedOptions(@RequestParam String type) {
        Iterable<Cars> carsIterable = carsRepo.findAll();
        Stream<Cars> carStream = StreamSupport.stream(carsIterable.spliterator(), false);

        switch (type) {
            case "energy":
                return carStream
                        .map(Cars::getEnergy)
                        .distinct()
                        .collect(Collectors.toList());
            case "make":
                return carStream
                        .map(Cars::getMake)
                        .distinct()
                        .collect(Collectors.toList());
            case "model":
                return carStream
                        .map(Cars::getModel)
                        .distinct()
                        .collect(Collectors.toList());
            case "transmission_type":
                return carStream
                        .map(Cars::getTransmissionType)
                        .distinct()
                        .collect(Collectors.toList());
            case "body_type":
                return carStream
                        .map(Cars::getBodyType)
                        .distinct()
                        .collect(Collectors.toList());
            default:
                return Collections.emptyList();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Cars>> getAllCars() {
        Iterable<Cars> iterableCars = carsRepo.findAll();
        List<Cars> cars = StreamSupport.stream(iterableCars.spliterator(), false).collect(Collectors.toList());

        for (Cars car : cars) {
            // Construct the image URL based on make, model, energy, and transmission type
            String imageUrl = "/images/"
                    + car.getMake()
                    + "_" + car.getModel()
                    + "_" + car.getEnergy()
                    + "_" + car.getTransmissionType()
                    + ".jpg";

            car.setImageUrl(imageUrl);
        }

        return ResponseEntity.ok(cars);
    }

    //Il attend un seul Stringargument term, qui est le terme de recherche flexible.
    @GetMapping("/search/{term}")
    public ResponseEntity<List<Map<String, Object>>> getCarDetailsByTerm(@PathVariable String term) throws IOException {
        List<String> terms = Arrays.asList(term.split("_"));

        // The recursive function will either return a List of Cars or an empty list
        List<Cars> foundCars = recursiveSearch(terms);

        if (!foundCars.isEmpty()) {
            // Process and return all matches
            List<Map<String, Object>> carDetailsList = new ArrayList<>();
            for (Cars foundCar : foundCars) {
                Map<String, Object> carDetails = createCarDetails(foundCar);
                carDetailsList.add(carDetails);
            }
            return ResponseEntity.ok(carDetailsList);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Update your recursiveSearch method to return multiple matches
    private List<Cars> recursiveSearch(List<String> terms) throws IOException {

        if (terms.isEmpty()) {
            return Collections.emptyList();
        }

        // Search with current terms
        List<Cars> results = searchWithTerms(terms);
        if (!results.isEmpty()) {
            return results;  // If there's a match, immediately return it.
        }

        // If no match was found, strip the last term and search again.
        List<String> newTerms = new ArrayList<>(terms);
        newTerms.remove(newTerms.size() - 1);
        return recursiveSearch(newTerms);
    }



    // Update your searchWithTerms method to return multiple matches
    private List<Cars> searchWithTerms(List<String> terms) throws IOException {
        String make = null;
        String model = null;
        String energy = null;
        String transmissionType = null;


        for (String part : terms) {
            if (carsService.isKnownMakeSynonym(part) || carsService.isKnownMake(part)) {
                make = carsService.convertSynonymToOriginal(part, "make");
            } else if (carsService.isKnownModelSynonym(part) || carsService.isKnownModel(part)) {
                model = carsService.convertSynonymToOriginal(part, "model");
            } else if (carsService.isKnownEnergySynonym(part) || carsService.isKnownEnergy(part)) {
                energy = carsService.convertSynonymToOriginal(part, "energy");
            } else if (carsService.isKnownTransmissionTypeSynonym(part) || carsService.isKnownTransmissionType(part)) {
                transmissionType = carsService.convertSynonymToOriginal(part, "transmissionType");
            }
            System.out.println("Term being processed: " + part);
            System.out.println("Recognized make: " + make);
            System.out.println("Recognized model: " + model);
            System.out.println("Recognized energy: " + energy);
            System.out.println("Recognized transmissionType: " + transmissionType);

        }


        return carsRepo.findByFlexibleAttributes(make, model, energy, transmissionType);
    }


    // Helper method to create car details map
    private Map<String, Object> createCarDetails(Cars car) {
        String imageUrl = "/images/"
                + (car.getMake() != null ? car.getMake() + "_" : "")
                + (car.getModel() != null ? car.getModel() + "_" : "")
                + (car.getEnergy() != null ? car.getEnergy() + "_" : "")
                + (car.getTransmissionType() != null ? car.getTransmissionType() : "")
                + ".jpg";

        Map<String, Object> carDetails = new HashMap<>();
        carDetails.put("imageUrl", imageUrl);
        carDetails.put("make", car.getMake());
        carDetails.put("model", car.getModel());
        carDetails.put("energy", car.getEnergy());
        carDetails.put("transmission_type", car.getTransmissionType());

        return carDetails;
    }

    @GetMapping("/autocomplete/combined")
    public ResponseEntity<List<String>> autocompleteCombined(@RequestParam("prefix") String prefix) {
        List<String> combinedSuggestions = carsService.autocompleteCombined(prefix);
        return ResponseEntity.ok(combinedSuggestions);
    }

}*/
