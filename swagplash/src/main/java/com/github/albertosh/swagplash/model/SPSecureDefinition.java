package com.github.albertosh.swagplash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.albertosh.swagplash.annotations.SecureDefinition;
import com.google.common.base.Preconditions;

import javax.lang.model.element.TypeElement;

public class SPSecureDefinition {

    private String secureName;
    private String type;
    private String description;
    private String name;
    private String in;

    public SPSecureDefinition(SecureDefinition secureDefinition, TypeElement classElement) {
        secureName = secureDefinition.value().isEmpty() ? classElement.getSimpleName().toString() : secureDefinition.value();
        type = secureDefinition.type();
        description = secureDefinition.description();
        name = secureDefinition.name();
        in = secureDefinition.in();
    }

    @JsonIgnore
    public String getSecureName() {
        return secureName;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getIn() {
        return in;
    }
}
