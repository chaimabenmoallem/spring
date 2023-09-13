package com.epix.hawkadmin.services;

//import com.epix.hawkadmin.config.FuzzinessConfig;
import com.epix.hawkadmin.model.TermData;
import com.epix.hawkadmin.model.Tuning;
import com.epix.hawkadmin.repository.TuningRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.apache.commons.text.similarity.LevenshteinDistance;


import java.util.*;
import java.util.stream.Collectors;


@Service
public class TuningService {

    private static final Logger logger = LoggerFactory.getLogger(TuningService.class);
    private boolean mergeWhitespace = false;

    private final TuningRepo tuningRepo;
    private final ElasticsearchOperations elasticsearchOperations;

    // Static list of terms
    public Map<String, Integer> staticData = new HashMap<String, Integer>() {{
        put("gasoile", 20);
        put("gaz oil", 8);
        put("gaze oil", 9);
        put("gazxxyoile", 4);
        put("gaxxxxyoile", 1);

        put("Audy", 13);
        put("auddy", 12);
    }};
        //Comparing terms: gaze oil and gaz oil
        //Fuzziness: 1
        //Comparing terms: gaz oil and gaze oil
        //Fuzziness: 1

  //Comparing terms: gazxxyoile and gaxxxxyoile
  //Fuzziness: 2
  //Comparing terms: gaxxxxyoile and gazxxyoile
  //Fuzziness: 2

    //Comparing terms: gasoile and gaz oil
    //Fuzziness: 3
    //Comparing terms: gaz oil and gasoile
    //Fuzziness: 3

    //Comparing terms: gaze oil and gazxxyoile
    //Fuzziness: 4
    //Comparing terms: gaze oil and gasoile
    //Fuzziness: 4
    //Comparing terms: gazxxyoile and gaze oil
    //Fuzziness: 4
    //Comparing terms: gazxxyoile and gasoile
    //Fuzziness: 4
    //Comparing terms: gazxxyoile and gaz oil
    //Fuzziness: 4
    //Comparing terms: gasoile and gaze oil
    //Fuzziness: 4
    //Comparing terms: gasoile and gazxxyoile
    //Fuzziness: 4
    //Comparing terms: gaz oil and gazxxyoile
    //Fuzziness: 4

    //Comparing terms: gaxxxxyoile and gasoile
    //Fuzziness: 5
    //Comparing terms: gasoile and gaxxxxyoile
    //Fuzziness: 5
    @Autowired
    public TuningService(TuningRepo tuningRepo, ElasticsearchOperations elasticsearchOperations) {
        this.tuningRepo = tuningRepo;
        this.elasticsearchOperations = elasticsearchOperations;
    }



    public Iterable<Tuning> getTunings() {
        return tuningRepo.findAll();
    }

    public void deleteTermById(String id) {
        tuningRepo.deleteById(id);
    }




    // Fuzziness calculation
    public int calculateFuzziness(String term1, String term2) {
        return LevenshteinDistance.getDefaultInstance().apply(term1, term2);
    }

    /*public List<TermData> getRepresentativeTermByFuzziness(int fuzziness) {
        List<String> termsList = new ArrayList<>(staticData.keySet());

        String maxTermWithoutSpace = null;
        int maxSumOccurrencesWithoutSpace = Integer.MIN_VALUE;

        String maxTermWithSpace = null;
        int maxSumOccurrencesWithSpace = Integer.MIN_VALUE;

        for (String term : termsList) {
            Integer occurrences = staticData.get(term);

            // Check if the key exists in the map
            if (occurrences != null) {
                int sumOccurrences = occurrences;

                for (String otherTerm : termsList) {
                    if (term.equals(otherTerm) || calculateFuzziness(term, otherTerm) != fuzziness) {
                        continue;  // Skip if it's the same term or fuzziness doesn't match
                    }

                    if (term.contains(" ") && otherTerm.contains(" ")) {
                        // Only aggregate occurrences if both terms have spaces
                        Integer otherOccurrences = staticData.get(otherTerm);

                        // Check if the key exists in the map
                        if (otherOccurrences != null) {
                            sumOccurrences += otherOccurrences;
                        }
                    }

                    // Debug output to print the terms being compared
                    System.out.println("Comparing terms: " + term + " and " + otherTerm);

                    // Debug output to print the fuzziness for this pair of terms
                    System.out.println("Fuzziness: " + calculateFuzziness(term, otherTerm));
                }

                // Decide where to store the aggregated occurrences
                if (term.contains(" ")) {
                    if (sumOccurrences > maxSumOccurrencesWithSpace) {
                        maxTermWithSpace = term;
                        maxSumOccurrencesWithSpace = sumOccurrences;
                    }
                } else {
                    if (sumOccurrences > maxSumOccurrencesWithoutSpace) {
                        maxTermWithoutSpace = term;
                        maxSumOccurrencesWithoutSpace = sumOccurrences;
                    }
                }
            }
        }

        List<TermData> results = new ArrayList<>();
        if (maxTermWithoutSpace != null) {
            results.add(new TermData(maxTermWithoutSpace, fuzziness, maxSumOccurrencesWithoutSpace));
        }
        if (maxTermWithSpace != null) {
            results.add(new TermData(maxTermWithSpace, fuzziness, maxSumOccurrencesWithSpace));
        }

        return results;
    }*/

    public List<TermData> getGroupedTermsByFuzziness(int fuzziness) {
        List<String> termsList = new ArrayList<>(staticData.keySet());

        Map<Boolean, List<String>> groupedTerms = termsList.stream()
                .filter(term -> termsList.stream()
                        .anyMatch(otherTerm -> !term.equals(otherTerm) && calculateFuzziness(term, otherTerm) == fuzziness))
                .collect(Collectors.groupingBy(term -> term.contains(" ")));

        List<TermData> results = new ArrayList<>();

        for (Map.Entry<Boolean, List<String>> entry : groupedTerms.entrySet()) {
            String highestOccurrenceTerm = entry.getValue().stream()
                    .max(Comparator.comparingInt(staticData::get))
                    .orElse(null);

            int totalOccurrences = entry.getValue().stream()
                    .mapToInt(staticData::get)
                    .sum();

            if (highestOccurrenceTerm != null) {
                TermData termData = new TermData(highestOccurrenceTerm, fuzziness, totalOccurrences);
                termData.setRelatedTerms(entry.getValue());  // Storing all related terms
                results.add(termData);
            }
        }

        return results;
    }



    /*public List<TermData> fuzzyMatch(int fuzziness) {
        List<TermData> results = new ArrayList<>();
        List<String> termsList = new ArrayList<>(staticData.keySet());

        for (int i = 0; i < termsList.size(); i++) {
            for (int j = i + 1; j < termsList.size(); j++) {
                String term1 = termsList.get(i);
                String term2 = termsList.get(j);

                if (calculateFuzziness(term1, term2) == fuzziness) {
                    if (!results.stream().anyMatch(termData -> termData.getTerm().equals(term1))) {
                        results.add(new TermData(term1, 0, staticData.get(term1)));
                    }
                    if (!results.stream().anyMatch(termData -> termData.getTerm().equals(term2))) {
                        results.add(new TermData(term2, 0, staticData.get(term2)));
                    }
                }
            }
        }
        return results;
    }*/










  /*  public Map<Integer, List<String>> classifyByFuzziness() {
        Map<Integer, List<String>> result = new HashMap<>();

        // Get all terms from the staticData
        List<String> terms = new ArrayList<>(staticData.keySet());

        for (int i = 0; i < terms.size(); i++) {
            for (int j = i + 1; j < terms.size(); j++) {
                int fuzziness = calculateFuzziness(terms.get(i), terms.get(j));
                result.putIfAbsent(fuzziness, new ArrayList<>());
                result.get(fuzziness).add(terms.get(i));
                result.get(fuzziness).add(terms.get(j));
            }
        }

        // Remove duplicates from the lists
        result.forEach((k, v) -> result.put(k, v.stream().distinct().collect(Collectors.toList())));

        return result;
    }


    // Fuzziness calculation
    public int calculateFuzziness(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        // Initialization: the distance of any first string to an empty second string
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }

        // Initialization: the distance of any second string to an empty first string
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // Fill in the table using a bottom-up approach
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }

        return dp[m][n];
    }



    public List<TermData> getTermsByFuzziness(int fuzziness) {
        List<TermData> result = new ArrayList<>();
        Map<Integer, Set<String>> termsByFuzziness = new HashMap<>();

        for (String term1 : staticData.keySet()) {
            for (String term2 : staticData.keySet()) {
                if (!term1.equals(term2)) {
                    int currentFuzziness = calculateFuzziness(term1, term2);

                    // Initialize the set if it doesn't exist for this fuzziness level
                    termsByFuzziness.putIfAbsent(currentFuzziness, new HashSet<>());

                    termsByFuzziness.get(currentFuzziness).add(term1);
                    termsByFuzziness.get(currentFuzziness).add(term2);
                }
            }
        }

        if (termsByFuzziness.containsKey(fuzziness)) {
            for (String term : termsByFuzziness.get(fuzziness)) {
                result.add(new TermData(term, fuzziness, staticData.get(term)));
            }
        }

        return result;
    }*/


    }



   /* @PostConstruct
    public void init() {
        List<Tuning> dataModels = Arrays.asList(
                new Tuning(1L, "vw", 25),
                new Tuning(2L, "RangeRover", 100),
                new Tuning(3L, "Range Rover", 50),
                new Tuning(4L, "BMW", 200)
        );

        tuningRepo.saveAll(dataModels);
    }*/



