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

    public String getName() {
        return name;
    }

    public List<String> getScopes() {
        return scopes;
    }
}
