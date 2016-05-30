package com.github.albertosh.swagplash.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.albertosh.swagplash.annotations.ApiBodyParam;
import com.github.albertosh.swagplash.annotations.ApiPathParam;
import com.github.albertosh.swagplash.annotations.ApiQueryParam;

import javax.lang.model.element.VariableElement;

public class SPApiParameter {

    private String name;
    private String in;
    private String description;
    private boolean required;
    private String defaultValue;
    private SPSchema schema;
    private String type;
    private String format;
    private SPItems items;

    public static SPApiParameter newPathParameter(ApiPathParam apiPathParam, VariableElement paramElement) {
        SPApiParameter parameter = new SPApiParameter();
        parameter.name = apiPathParam.name().isEmpty() ? paramElement.getSimpleName().toString() : apiPathParam.name();
        parameter.in = "path";
        parameter.description = apiPathParam.value();
        parameter.required = true;
        parameter.type = Utils.getType(paramElement);
        parameter.format = Utils.getFormat(paramElement);
        return parameter;
    }


    public static SPApiParameter newQueryParameter(ApiQueryParam apiQueryParam, VariableElement paramElement) {
        SPApiParameter parameter = new SPApiParameter();
        parameter.name = apiQueryParam.name().isEmpty() ? paramElement.getSimpleName().toString() : apiQueryParam.name();
        parameter.in = "query";
        parameter.description = apiQueryParam.value();
        parameter.required = apiQueryParam.required();
        parameter.type = Utils.getType(paramElement);
        if ((parameter.type) != null && (parameter.type.equals("array")))
            parameter.items = SPItems.newBuilder().withType(Utils.getTypeOfArrayItems(paramElement)).build();
        parameter.defaultValue = apiQueryParam.defaultValue();
        parameter.format = Utils.getFormat(paramElement);
        return parameter;
    }

    public static SPApiParameter newBodyParameter() {
        SPApiParameter parameter = new SPApiParameter();
        parameter.name = "body params";
        parameter.in = "body";
        parameter.required = true;
        parameter.schema = new SPSchema();
        return parameter;
    }

    public String getName() {
        return name;
    }

    public String getIn() {
        return in;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRequired() {
        return required;
    }

    @JsonProperty("default")
    public Object getDefaultValue() {
        if ((defaultValue != null) && (!defaultValue.isEmpty()))
            return Utils.defaultValueWithTypeFormat(defaultValue, type, format);
        else
            return null;
    }

    public SPSchema getSchema() {
        return schema;
    }

    public String getType() {
        return type;
    }

    public String getFormat() {
        return format;
    }

    public SPItems getItems() {
        return items;
    }

    public SPApiParameter() {
    }

    public SPApiParameter setName(String name) {
        this.name = name;
        return this;
    }

    public SPApiParameter setIn(String in) {
        this.in = in;
        return this;
    }

    public SPApiParameter setDescription(String description) {
        this.description = description;
        return this;
    }

    public SPApiParameter setRequired(boolean required) {
        this.required = required;
        return this;
    }

    public SPApiParameter setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public SPApiParameter setSchema(SPSchema schema) {
        this.schema = schema;
        return this;
    }

    public SPApiParameter setType(String type) {
        this.type = type;
        return this;
    }

    public SPApiParameter setFormat(String format) {
        this.format = format;
        return this;
    }

    public SPApiParameter setItems(SPItems items) {
        this.items = items;
        return this;
    }
}
