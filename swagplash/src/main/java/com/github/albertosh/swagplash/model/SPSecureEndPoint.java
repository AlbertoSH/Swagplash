package com.github.albertosh.swagplash.model;

import com.github.albertosh.swagplash.annotations.SecureEndPoint;

import java.util.ArrayList;
import java.util.List;

public class SPSecureEndPoint {

    private String name;
    private List<String> scopes = new ArrayList<>();

    public SPSecureEndPoint(SecureEndPoint secureEndPoint) {
        name = secureEndPoint.value();
    }

    public SPSecureEndPoint() {

    }

    public String getName() {
        return name;
    }

    public SPSecureEndPoint setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getScopes() {
        return scopes;
    }

    public SPSecureEndPoint setScopes(List<String> scopes) {
        this.scopes = scopes;
        return this;
    }

    public SPSecureEndPoint addScope(String scope) {
        this.scopes.add(scope);
        return this;
    }
}
