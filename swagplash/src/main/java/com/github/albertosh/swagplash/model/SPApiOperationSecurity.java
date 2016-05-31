package com.github.albertosh.swagplash.model;

import java.util.ArrayList;
import java.util.List;

public class SPApiOperationSecurity {

    private List<SPSecureEndPoint> security = new ArrayList<>();

    public void addAll(List<SPSecureEndPoint> security) {
        this.security.addAll(security);
    }

    public void add(SPSecureEndPoint secureEndPoint) {
        this.security.add(secureEndPoint);
    }

    public List<SPSecureEndPoint> getSecurity() {
        return security;
    }
}
