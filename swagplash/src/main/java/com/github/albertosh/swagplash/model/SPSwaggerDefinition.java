package com.github.albertosh.swagplash.model;

import com.github.albertosh.swagplash.annotations.SwaggerDefinition;
import javax.lang.model.element.Element;
import java.util.*;

public class SPSwaggerDefinition {

    private final String swagger = "2.0";
    private SPInfo info;
    private String host;
    private String basePath;
    private List<String> schemes = new ArrayList<>();
    private List<String> consumes = new ArrayList<>();
    private List<String> produces = new ArrayList<>();
    private Map<String, Map<String, SPApiOperation>> paths = new LinkedHashMap<>();
    private Map<String, SPApiModel> definitions = new LinkedHashMap<>();
    private Map<String, SPSecureDefinition> securityDefinitions = new LinkedHashMap<>();

    public SPSwaggerDefinition(SwaggerDefinition def) {
        info = new SPInfo(def.info());
        host = def.host();
        basePath = def.basePath();
        for (SwaggerDefinition.Scheme scheme : def.schemes()) {
            schemes.add(scheme.toString().toLowerCase());
        }
        Collections.addAll(consumes, def.consumes());
        Collections.addAll(produces, def.consumes());
    }

    public SPSwaggerDefinition() {
    }

    public SPSwaggerDefinition setInfo(SPInfo info) {
        this.info = info;
        return this;
    }

    public SPSwaggerDefinition setHost(String host) {
        this.host = host;
        return this;
    }

    public SPSwaggerDefinition setBasePath(String basePath) {
        this.basePath = basePath;
        return this;
    }

    public SPSwaggerDefinition setSchemes(List<String> schemes) {
        this.schemes = schemes;
        return this;
    }

    public SPSwaggerDefinition setConsumes(List<String> consumes) {
        this.consumes = consumes;
        return this;
    }

    public SPSwaggerDefinition setProduces(List<String> produces) {
        this.produces = produces;
        return this;
    }

    public SPSwaggerDefinition setPaths(Map<String, Map<String, SPApiOperation>> paths) {
        this.paths = paths;
        return this;
    }

    public SPSwaggerDefinition setDefinitions(Map<String, SPApiModel> definitions) {
        this.definitions = definitions;
        return this;
    }

    public String getSwagger() {
        return swagger;
    }

    public SPInfo getInfo() {
        return info;
    }

    public String getHost() {
        return host;
    }

    public String getBasePath() {
        return basePath;
    }

    public List<String> getSchemes() {
        return schemes;
    }

    public List<String> getConsumes() {
        return consumes;
    }

    public List<String> getProduces() {
        return produces;
    }

    public Map<String, Map<String, SPApiOperation>> getPaths() {
        return paths;
    }

    public Map<String, SPApiModel> getDefinitions() {
        return definitions;
    }

    public Map<String, SPSecureDefinition> getSecurityDefinitions() {
        return securityDefinitions;
    }

    public void addDefinition(SPApiModel spApiModel) {
        definitions.put(spApiModel.getName(), spApiModel);
    }

    public void addApiOperation(SPApiOperation apiOperation, Element method) {
        Map<String, SPApiOperation> path = paths.get(apiOperation.getPath());
        if (path == null) {
            path = new LinkedHashMap<>();
            paths.put(apiOperation.getPath(), path);
        }
        if (path.containsKey(apiOperation.getMethod())) {
            Utils.warning("Overriding previous method " + apiOperation.getMethod()
                    + " at path " + apiOperation.getPath(),
                    method);
        }
        path.put(apiOperation.getMethod(), apiOperation);
    }

    public void addSecureDefinition(SPSecureDefinition secureDefinition) {
        securityDefinitions.put(secureDefinition.getSecureName(), secureDefinition);
    }
}
