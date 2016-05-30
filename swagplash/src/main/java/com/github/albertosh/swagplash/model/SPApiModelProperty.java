package com.github.albertosh.swagplash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.albertosh.swagplash.annotations.ApiBodyParam;
import com.github.albertosh.swagplash.annotations.ApiModelProperty;

import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

public class SPApiModelProperty {

    private String name;
    private boolean required;
    private String description;
    private String type;
    private String format;
    private String $ref;

    public SPApiModelProperty(ApiBodyParam apiBodyParam) {
        name = apiBodyParam.name();
        description = apiBodyParam.value();
        required = apiBodyParam.required();
        type = apiBodyParam.dataType().getType();
        format = apiBodyParam.dataType().getFormat();
    }

    public SPApiModelProperty(ApiModelProperty apiModelProperty, VariableElement field) {
        name = apiModelProperty.name().isEmpty() ? field.getSimpleName().toString() : apiModelProperty.name();
        description = apiModelProperty.value();
        type = Utils.getType(field);
        format = Utils.getFormat(field);

        if (Utils.isPrimitive(field)) {
            required = true;
        } else {
            Nullable nullable = field.getAnnotation(Nullable.class);
            required = (nullable == null);
        }
    }

    public SPApiModelProperty(ApiModelProperty apiModelProperty, ExecutableElement field) {
        name = apiModelProperty.name().isEmpty() ? field.getSimpleName().toString() : apiModelProperty.name();
        description = apiModelProperty.value();
        type = Utils.getType(field);
        format = Utils.getFormat(field);

        if (Utils.isPrimitive(field)) {
            required = true;
        } else {
            Nullable nullable = field.getAnnotation(Nullable.class);
            required = (nullable == null);
        }
    }

    @JsonIgnore
    public String getName() {
        return name;
    }

    @JsonIgnore
    public boolean isRequired() {
        return required;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public String getFormat() {
        return format;
    }


    public SPApiModelProperty() {
    }

    public SPApiModelProperty setName(String name) {
        this.name = name;
        return this;
    }

    public SPApiModelProperty setRequired(boolean required) {
        this.required = required;
        return this;
    }

    public SPApiModelProperty setDescription(String description) {
        this.description = description;
        return this;
    }

    public SPApiModelProperty setType(String type) {
        this.type = type;
        return this;
    }

    public SPApiModelProperty setFormat(String format) {
        this.format = format;
        return this;
    }

    public SPApiModelProperty set$ref(String $ref) {
        this.$ref = $ref;
        return this;
    }
}
