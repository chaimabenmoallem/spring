package com.epix.hawkadmin.model;

public class SearchRequest {
    private String term;
    private int fuzziness;

    public SearchRequest() {
    }

    public SearchRequest(String term, int fuzziness) {
        this.term = term;
        this.fuzziness = fuzziness;
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
}
