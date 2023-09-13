package com.epix.hawkadmin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

@Document(indexName = "cars")
public class Cars {

    @Id
    private String id;
    private String make;
    private String model;
    private String energy;
    @Field(name = "transmission_type")
    private String transmissionType;
    @Field(name = "body_type")
    private String bodyType;
    private String imageUrl;


    public Cars() {
    }

    public Cars(String id, String make, String model, String energy, String transmissionType, String bodyType, String imageUrl) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.energy = energy;
        this.transmissionType = transmissionType;
        this.bodyType = bodyType;
        this.imageUrl=imageUrl;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }
    
}
