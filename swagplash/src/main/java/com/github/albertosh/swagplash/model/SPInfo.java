package com.github.albertosh.swagplash.model;

import com.github.albertosh.swagplash.annotations.Info;

public class SPInfo {

    private String title;
    private String description;
    private String termsOfService;
    private SPContact contact;
    private SPLicense license;
    private String version;

    public SPInfo(Info info) {
        title = info.title();
        description = info.description();
        termsOfService = info.termsOfService();
        contact = new SPContact(info.contact());
        license = new SPLicense(info.license());
        version = info.version();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTermsOfService() {
        return termsOfService;
    }

    public SPContact getContact() {
        return contact;
    }

    public SPLicense getLicense() {
        return license;
    }

    public String getVersion() {
        return version;
    }
}
