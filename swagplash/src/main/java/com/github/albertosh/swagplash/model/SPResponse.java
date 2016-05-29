package com.github.albertosh.swagplash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.albertosh.swagplash.annotations.ApiResponse;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

public class SPResponse {

    private int code;
    private String description;
    private SPSchema schema;

    @Override
    public String toString() {
        return "SPResponse{" +
                "code=" + code +
                ", description='" + description + '\'' +
                ", schema=" + schema +
                '}';
    }

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


    @JsonIgnore
    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public SPSchema getSchema() {
        return schema;
    }
}
