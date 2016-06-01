package com.github.albertosh.swagplash.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.albertosh.swagplash.annotations.SecureDefinition;

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

    public SPSecureDefinition() {
    }

    @JsonIgnore
    public String getSecureName() {
        return secureName;
    }

    public SPSecureDefinition setSecureName(String secureName) {
        this.secureName = secureName;
        return this;
    }

    public String getType() {
        return type;
    }

    public SPSecureDefinition setType(String type) {
        this.type = type;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SPSecureDefinition setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getName() {
        return name;
    }

    public SPSecureDefinition setName(String name) {
        this.name = name;
        return this;
    }

    public String getIn() {
        return in;
    }

    public SPSecureDefinition setIn(String in) {
        this.in = in;
        return this;
    }
}
