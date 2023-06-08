package com.example.jsp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FacebookUserResponse {
    private String email;
    private String name;

    // Các getter và setter
    // ...

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }
}

