package com.epix.hawkadmin.model;

import java.util.List;
import java.util.Objects;

public class TermData {
    private String term;
    private int fuzziness;
    private int occurrences;
    private List<String> relatedTerms;

    public TermData(String term, int fuzziness, int occurrences) {
        this.term = term;
        this.fuzziness = fuzziness;
        this.occurrences = occurrences;
    }

    public List<String> getRelatedTerms() {
        return relatedTerms;
    }

    public void setRelatedTerms(List<String> relatedTerms) {
        this.relatedTerms = relatedTerms;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getFuzziness() {
        return fuzziness;
    }

    public void setFuzziness(int fuzziness) {
        this.fuzziness = fuzziness;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(int occurrences) {
        this.occurrences = occurrences;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TermData termData = (TermData) obj;
        return Objects.equals(term, termData.term);
    }

    @Override
    public int hashCode() {
        return Objects.hash(term);
    }
}
