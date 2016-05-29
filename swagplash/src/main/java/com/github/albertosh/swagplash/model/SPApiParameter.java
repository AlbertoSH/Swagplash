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
        if (defaultValue != null)
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
}
