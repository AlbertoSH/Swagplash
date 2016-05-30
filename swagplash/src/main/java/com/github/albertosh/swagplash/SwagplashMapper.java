package com.github.albertosh.swagplash;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SwagplashMapper extends ObjectMapper {

    public SwagplashMapper() {
        setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
}
