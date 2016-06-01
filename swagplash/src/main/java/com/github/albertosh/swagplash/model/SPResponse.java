package com.github.albertosh.swagplash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.albertosh.swagplash.annotations.ApiResponse;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

public class SPResponse {

    private int code;
    private String description;
    private SPSchema schema;

    public SPResponse(ApiResponse apiResponse, ExecutableElement element) {
        code = apiResponse.code();
        description = apiResponse.message();

        TypeMirror tm = null;
        try {
            // Force exception
            apiResponse.response().toString();
        } catch (MirroredTypeException e) {
            tm = e.getTypeMirror();
        }

        schema = SPSchema.fromTypeMirrorAndContainer(tm, apiResponse.responseContainer(), element);
    }

    public SPResponse() {
    }

    @Override
    public String toString() {
        return "SPResponse{" +
                "code=" + code +
                ", description='" + description + '\'' +
                ", schema=" + schema +
                '}';
    }

    @JsonIgnore
    public int getCode() {
        return code;
    }

    public SPResponse setCode(int code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SPResponse setDescription(String description) {
        this.description = description;
        return this;
    }

    public SPSchema getSchema() {
        return schema;
    }

    public SPResponse setSchema(SPSchema schema) {
        this.schema = schema;
        return this;
    }
}
