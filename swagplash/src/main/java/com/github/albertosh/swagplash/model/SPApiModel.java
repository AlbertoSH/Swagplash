package com.github.albertosh.swagplash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.albertosh.swagplash.annotations.ApiModel;

import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SPApiModel {

    private String name;
    private String description;
    private String type = "object";
    private List<String> required;
    private Map<String, SPApiModelProperty> properties;

    public SPApiModel(ApiModel apiModel, TypeElement classElement) {
        name = apiModel.value().isEmpty() ? classElement.getSimpleName().toString() : apiModel.value();
        description = apiModel.description();
        required = new ArrayList<>();
        properties = new LinkedHashMap<>();
    }

    public SPApiModel() {
    }

    @JsonIgnore
    public String getName() {
        return name;
    }

    public SPApiModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SPApiModel setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getType() {
        return type;
    }

    public SPApiModel setType(String type) {
        this.type = type;
        return this;
    }

    public List<String> getRequired() {
        return required;
    }

    public SPApiModel setRequired(List<String> required) {
        this.required = required;
        return this;
    }

    public Map<String, SPApiModelProperty> getProperties() {
        return properties;
    }

    public SPApiModel setProperties(Map<String, SPApiModelProperty> properties) {
        this.properties = properties;
        return this;
    }

    public void addProperty(SPApiModelProperty property) {
        properties.put(property.getName(), property);
        if (property.isRequired())
            required.add(property.getName());
    }
}
