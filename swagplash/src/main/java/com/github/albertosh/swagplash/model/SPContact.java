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

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getEmail() {
        return email;
    }
}
