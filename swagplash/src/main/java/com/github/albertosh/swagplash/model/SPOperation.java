package com.github.albertosh.swagplash.model;

import java.util.List;

public class SPOperation {

    private List<String> tags;
    private String summary;
    private String description;
    private String operationId;
    private List<String> consumes;
    private List<String> produces;
    private List<Object> parameters;
    private List<SPResponse> reponses;
    private boolean deprecated;

}
