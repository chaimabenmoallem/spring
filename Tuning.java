package com.epix.hawkadmin.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "tuning")
public class Tuning {


    //  @Id
    private String id;

    //  @Field(type = FieldType.Text, name = "terms")
    private String term;
    // @Field(type = FieldType.Text, name = "occurrences")
    private int occurrences;
    private int fuzziness;

    public Tuning() {
    }


    public Tuning(String id, String term, int occurrences, int fuzziness) {

        this.id = id;
        this.term = term;
        this.occurrences = occurrences;
        this.fuzziness = fuzziness;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getOccurrences() {
        return occurrences;
    }

    public void setOccurrences(int occurrences) {
        this.occurrences = occurrences;
    }

    public int getFuzziness() {
        return fuzziness;
    }

    public void setFuzziness(int fuzziness) {
        this.fuzziness = fuzziness;
    }

    @Override
    public String toString() {
        return "Tuning{" +
                "id='" + id + '\'' +
                ", term='" + term + '\'' +
                ", occurrences=" + occurrences +
                ", fuzziness=" + fuzziness +
                "}";
    }

}

