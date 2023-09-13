package com.epix.hawkadmin.controller;

//import com.epix.hawkadmin.config.FuzzinessConfig;
import com.epix.hawkadmin.model.Tuning;
import com.epix.hawkadmin.repository.TuningRepo;
import com.epix.hawkadmin.model.TermData;
import com.epix.hawkadmin.services.TuningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RequestMapping("/fuzzy")
@RestController
public class fuzzinessController {

    @Autowired
    private  TuningRepo tuningRepo;
    @Autowired
    private TuningService tuningService;


  /*  @GetMapping("/fetch-terms-by-fuzziness")
    public List<TermData> fuzzyMatch(@RequestParam int fuzziness) {
        return tuningService.fuzzyMatch(fuzziness);
    }*/



    @GetMapping("/all-terms")
    public ResponseEntity<List<TermData>> getAllTerms() {
        return ResponseEntity.ok(
                tuningService.staticData.entrySet().stream()
                        .map(e -> new TermData(e.getKey(), 0, e.getValue())) // Here fuzziness is set to 0 by default.
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/grouped-terms-by-fuzziness/{fuzziness}")
    public ResponseEntity<List<TermData>> getGroupedTermsByFuzziness(@PathVariable int fuzziness) {
        List<TermData> terms = tuningService.getGroupedTermsByFuzziness(fuzziness);
        if (terms.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(terms);
    }

   /* @GetMapping("/representativeTerm/{fuzziness}")
    public ResponseEntity<List<TermData>> getRepresentativeTerm(@PathVariable int fuzziness) {
        List<TermData> terms = tuningService.getRepresentativeTermByFuzziness(fuzziness);
        if (terms.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(terms);
    }*/









   /* @Autowired
    private Map<String, FuzzinessConfig.TermData> fuzzinessMapping;


   @GetMapping("/get-terms-by-fuzziness")
    public ResponseEntity<List<String>> getTermsByFuzziness(@RequestParam Integer fuzziness) {
        List<String> terms = fuzzinessMapping.entrySet().stream()
                .filter(entry -> entry.getValue().fuzziness== fuzziness)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return ResponseEntity.ok(terms);
    }
    @GetMapping("/all-terms")
    public ResponseEntity<Map<String, FuzzinessConfig.TermData>> getAllTerms() {
        return ResponseEntity.ok(fuzzinessMapping);
    }*/


   /* @PostMapping("/es")
    public ResponseEntity<?> saveToElastic(@RequestBody List<Tuning> dataList) {

        System.out.println("Received data: " + dataList);

        if (dataList.isEmpty()) {
            return ResponseEntity.badRequest().body("Empty data list received.");
        }

        dataList.forEach(data -> tuningRepo.save(data));

        Map<String, String> response = new HashMap<>();
        response.put("message", "Data saved to Elasticsearch");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
*/

}
