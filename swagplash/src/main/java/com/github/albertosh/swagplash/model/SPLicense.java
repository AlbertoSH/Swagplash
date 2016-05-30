package com.github.albertosh.swagplash.model;

import com.github.albertosh.swagplash.annotations.License;

public class SPLicense {

    private String name;
    private String url;

    public SPLicense(License license) {
        name = license.name();
        url = license.url();
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public SPLicense() {
    }

    public SPLicense setName(String name) {
        this.name = name;
        return this;
    }

    public SPLicense setUrl(String url) {
        this.url = url;
        return this;
    }
}
