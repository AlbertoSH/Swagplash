package com.github.albertosh.swagplash.model;

import com.github.albertosh.swagplash.annotations.License;

public class SPLicense {

    private String name;
    private String url;

    public SPLicense(License license) {
        name = license.name();
        url = license.url();
    }
}
