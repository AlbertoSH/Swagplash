package com.github.albertosh.swagplash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.albertosh.swagplash.annotations.*;
import scala.annotation.meta.param;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.*;

public class SPApiOperation {

    private String path;
    private String method;
    private String summary;
    private String description;
    private List<String> tags = new ArrayList<>();
    private List<String> produces = new ArrayList<>();
    private List<String> consumes = new ArrayList<>();
    private List<SPApiParameter> parameters = new ArrayList<>();
    private Map<String, SPResponse> responses = new LinkedHashMap<>();

    public SPApiOperation(ApiOperation apiOperation, SPApi api, ExecutableElement method) {
        path = apiOperation.path();
        if (!path.startsWith("/"))
            path = "/" + path;
        this.method = apiOperation.httpMethod().toString().toLowerCase();
        summary = apiOperation.value();
        description = apiOperation.description();
        if (apiOperation.tags().length > 0) {
            Collections.addAll(tags, apiOperation.tags());
        } else {
            tags.addAll(api.getTags());
        }
        if (apiOperation.produces().length > 0) {
            Collections.addAll(produces, apiOperation.produces());
        } else {
            produces.addAll(api.getProduces());
        }
        if (apiOperation.consumes().length > 0) {
            Collections.addAll(consumes, apiOperation.consumes());
        } else {
            consumes.addAll(api.getConsumes());
        }

        buildParameters(method);
        buildResponses(method);
    }

    private void buildParameters(ExecutableElement method) {
        buildPathParameters(method);
        buildQueryParameters(method);
        buildBodyParameters(method);
    }

    private void buildPathParameters(ExecutableElement method) {
        for (VariableElement param : method.getParameters()) {
            ApiPathParam apiPathParam = param.getAnnotation(ApiPathParam.class);
            if (apiPathParam != null) {
                SPApiParameter spApiParameter = SPApiParameter.newPathParameter(apiPathParam, param);
                parameters.add(spApiParameter);
            }
        }
    }

    private void buildQueryParameters(ExecutableElement method) {
        for (VariableElement param : method.getParameters()) {
            ApiQueryParam apiQueryParam = param.getAnnotation(ApiQueryParam.class);
            if (apiQueryParam != null) {
                SPApiParameter spApiParameter = SPApiParameter.newQueryParameter(apiQueryParam, param);
                parameters.add(spApiParameter);
            }
        }
    }

    private void buildBodyParameters(ExecutableElement method) {
        ApiBodyParam[] apiBodyParams = method.getAnnotationsByType(ApiBodyParam.class);
        if (apiBodyParams.length > 0) {
            SPApiParameter spApiBodyParameter = SPApiParameter.newBodyParameter();
            for (ApiBodyParam apiBodyParam : method.getAnnotationsByType(ApiBodyParam.class)) {
                SPApiModelProperty property = new SPApiModelProperty(apiBodyParam);
                spApiBodyParameter.getSchema().addProperty(property);
            }
            parameters.add(spApiBodyParameter);
        }
    }

    private void buildResponses(ExecutableElement method) {
        for (ApiResponse apiResponse : method.getAnnotationsByType(ApiResponse.class)) {
            SPResponse response = new SPResponse(apiResponse, method);
            responses.put(String.valueOf(response.getCode()), response);
        }
        if (responses.keySet().isEmpty()) {
            Utils.warning("@ApiOperation without responses", method);
        }
    }

    @JsonIgnore
    public String getPath() {
        return path;
    }

    @JsonIgnore
    public String getMethod() {
        return method;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getProduces() {
        return produces;
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public List<SPApiParameter> getParameters() {
        return parameters;
    }

    public Map<String, SPResponse> getResponses() {
        return responses;
    }


    public SPApiOperation() {
    }

    public SPApiOperation setPath(String path) {
        this.path = path;
        return this;
    }

    public SPApiOperation setMethod(String method) {
        this.method = method;
        return this;
    }

    public SPApiOperation setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public SPApiOperation setDescription(String description) {
        this.description = description;
        return this;
    }

    public SPApiOperation setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public SPApiOperation setProduces(List<String> produces) {
        this.produces = produces;
        return this;
    }

    public SPApiOperation setConsumes(List<String> consumes) {
        this.consumes = consumes;
        return this;
    }

    public SPApiOperation setParameters(List<SPApiParameter> parameters) {
        this.parameters = parameters;
        return this;
    }

    public SPApiOperation setResponses(Map<String, SPResponse> responses) {
        this.responses = responses;
        return this;
    }
}
