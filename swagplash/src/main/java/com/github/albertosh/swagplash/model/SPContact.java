package com.github.albertosh.swagplash.model;

import com.github.albertosh.swagplash.annotations.Contact;

public class SPContact {

    private String name;
    private String url;
    private String email;

    public SPContact(Contact contact) {
        name = contact.name();
        url = contact.url();
        email = contact.email();
    }

    public SPContact() {
    }

    public String getName() {
        return name;
    }

    public SPContact setName(String name) {
        this.name = name;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public SPContact setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public SPContact setEmail(String email) {
        this.email = email;
        return this;
    }
}
