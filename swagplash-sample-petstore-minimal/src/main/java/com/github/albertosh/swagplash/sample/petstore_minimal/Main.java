package com.github.albertosh.swagplash.sample.petstore_minimal;

import com.github.albertosh.swagplash.SwagplashMapper;
import com.github.albertosh.swagplash.actions.SwagplashProvider;
import com.github.albertosh.swagplash.annotations.SwaggerDefinition;
import com.github.albertosh.swagplash.model.SPSwaggerDefinition;

import java.io.File;
import java.io.IOException;

/**
 * Created by albertosanzherrero on 31/5/16.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        SwagplashMapper mapper = new SwagplashMapper();

        SPSwaggerDefinition swaggerDefinition = mapper.readValue(new File("Swagger.json"), SPSwaggerDefinition.class);

        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(swaggerDefinition));
    }

}
