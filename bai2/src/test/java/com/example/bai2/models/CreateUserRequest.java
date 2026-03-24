package com.example.bai2.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserRequest {

    private final String name;
    private final String job;

    @JsonCreator
    public CreateUserRequest(@JsonProperty("name") String name,
            @JsonProperty("job") String job) {
        this.name = name;
        this.job = job;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("job")
    public String getJob() {
        return job;
    }
}
