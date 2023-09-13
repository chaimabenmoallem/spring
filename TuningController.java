package com.epix.hawkadmin.controller;

import com.epix.hawkadmin.model.Tuning;
import com.epix.hawkadmin.repository.TuningRepo;
import com.epix.hawkadmin.services.TuningService;
import jakarta.annotation.PostConstruct;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.metrics.Sum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@CrossOrigin(origins = "*")
//@CrossOrigin(origins = "http://localhost:4200" , methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.OPTIONS, RequestMethod.HEAD})
@RequestMapping("/tuning")
@RestController
public class TuningController {

   // private List<Tuning> tableData = new ArrayList<>();

    private final TuningRepo tuningRepo;

    private final TuningService tuningService;


    @Autowired
    public TuningController(TuningRepo tuningRepo, TuningService tuningService) {
        this.tuningRepo = tuningRepo;
        this.tuningService = tuningService;
    }
    @RequestMapping
   // @GetMapping("/tunings")
    public Iterable<Tuning> getTunings() {
        return tuningService.getTunings();
    }


   /* @DeleteMapping("/{id}")
    public void deleteData(@PathVariable("id") String id) {
        tuningRepo.deleteById(id);
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTerm(@PathVariable String id) {
        try {
            tuningService.deleteTermById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

   /* @PostMapping("/searchWithFuzziness")
    public ResponseEntity<?> searchWithFuzziness(@RequestBody SearchRequest request) {
        SearchRequestBuilder searchBuilder = client.prepareSearch("tuning")
                .setQuery(QueryBuilders.fuzzyQuery("term", request.getTerm()).fuzziness(request.getFuzziness()))
                .addAggregation(AggregationBuilders.sum("totalOccurrences").field("occurrences"));

        SearchResponse response = searchBuilder.execute().actionGet();
        Sum sum = response.getAggregations().get("totalOccurrences");

        return new ResponseEntity<>(sum.getValue(), HttpStatus.OK);
    }*/

}
    //@RequestMapping
   /*  @GetMapping("/search")
    public ResponseEntity<?> handleTermSearch(@RequestParam String tuning) {
        Optional<Tuning> existingTerm = tuningRepo.findByTerm(tuning);

        if (existingTerm.isPresent()) {
            return new ResponseEntity<>(existingTerm.get(), HttpStatus.OK);
        } else {
            // Return a JSON object instead of a plain string
            return new ResponseEntity<>(Collections.singletonMap("error", "Term not found"), HttpStatus.NOT_FOUND);
        }
    }

   @PostMapping("/add")
    public ResponseEntity<Tuning> addTerm(@RequestBody Tuning term) {
        Tuning savedTerm = tuningService.addTerm1(term);
        return new ResponseEntity<>(savedTerm, HttpStatus.CREATED);
    }
*/

  /*  @PostMapping("/addTermToTable")
    public ResponseEntity<Map<String, String>> addTermToTable(@RequestBody String term) {
        handleIncomingTerm(term);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Term added");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    private void handleIncomingTerm(String term) {
        Tuning existingTerm = tableData.stream()
                .filter(t -> t.getTerms().equals(term))
                .findFirst()
                .orElse(null);

        if (existingTerm != null) {
            existingTerm.incrementOccurrence();
        } else {
            tableData.add(new Tuning("1",term, 1));
        }
    }*/
  /*@PostMapping("/add")
    public ResponseEntity<?> addTermToTable(@RequestBody Tuning term) {
        tuningService.addTerm(term);
        return ResponseEntity.ok().build();
    }*/




   /* @GetMapping("/fuzzy")
    public Tuning getTuningsWithFuzziness(@RequestParam String term, @RequestParam int fuzziness) {
        return tuningService.findWithFuzziness(term, fuzziness);
    }*/


   /* @GetMapping("/tunings")
    public List<Tuning> getTuningsWithFuzziness(@RequestParam String terms, @RequestParam int fuzziness) {
        return tuningService.findWithFuzziness(terms, fuzziness);
    }*/



