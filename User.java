package com.epix.hawkadmin.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "register")
public class User {
  //  @Id
    private String id;
   // @Field(type = FieldType.Text, name = "name")
    private String name;
   // @Field(type = FieldType.Keyword, name = "email")
    private String email;
  //  @Field(type = FieldType.Text, name = "password")
    private String password;
   // @Field(type = FieldType.Text, name = "confirmPassword")
    private String confirmPassword;

    public User() {
    }

    public User(String id, String name, String email, String password, String confirmPassword) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
