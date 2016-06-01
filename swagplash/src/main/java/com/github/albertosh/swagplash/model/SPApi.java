package com.github.albertosh.swagplash.model;

import com.github.albertosh.swagplash.annotations.Api;
import com.github.albertosh.swagplash.annotations.SecureEndPoint;

import javax.lang.model.element.TypeElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SPApi {

    private List<String> tags = new ArrayList<>();
    private List<String> produces = new ArrayList<>();
    private List<String> consumes = new ArrayList<>();
    private List<SPSecureEndPoint> security = new ArrayList<>();

    public SPApi(Api api, SPSwaggerDefinition swagger, TypeElement apiClassElement) {
        if (api.tags().length > 0) {
            Collections.addAll(tags, api.tags());
        } else if (!api.value().isEmpty()) {
            tags.add(api.value());
        }

        if (api.produces().length > 0) {
            Collections.addAll(produces, api.produces());
        } else {
            produces.addAll(swagger.getProduces());
        }

        if (api.consumes().length > 0) {
            Collections.addAll(consumes, api.produces());
        } else {
            consumes.addAll(swagger.getConsumes());
        }

        SecureEndPoint[] secureEndPoints = apiClassElement.getAnnotationsByType(SecureEndPoint.class);
        for (SecureEndPoint secureEndPoint : secureEndPoints) {
            security.add(new SPSecureEndPoint(secureEndPoint));
        }
    }

    public SPApi() {
    }

    public List<String> getTags() {
        return tags;
    }

    public SPApi setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public List<String> getProduces() {
        return produces;
    }

    public SPApi setProduces(List<String> produces) {
        this.produces = produces;
        return this;
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public SPApi setConsumes(List<String> consumes) {
        this.consumes = consumes;
        return this;
    }

    public List<SPSecureEndPoint> getSecurity() {
        return security;
    }

    public SPApi setSecurity(List<SPSecureEndPoint> security) {
        this.security = security;
        return this;
    }
}
