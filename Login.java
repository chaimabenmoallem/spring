package com.epix.hawkadmin.model;

import org.springframework.data.elasticsearch.annotations.Document;


@Document(indexName = "login")
public class Login {
        //  @Id
        private String id;

        // @Field(type = FieldType.Keyword, name = "email")
        private String email;
        //  @Field(type = FieldType.Text, name = "password")
        private String password;


        public Login() {
        }

        public Login(String id, String email, String password) {
            this.id = id;
            this.email = email;
            this.password = password;

        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

    }
