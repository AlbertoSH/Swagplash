package com.github.albertosh.swagplash;

import com.github.albertosh.swagplash.SwagplashMapper;
import com.github.albertosh.swagplash.model.SPSwaggerDefinition;
import com.google.inject.Provider;
import play.Application;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class SwagplashProvider implements Provider<CompletionStage<SPSwaggerDefinition>> {

    private final Application application;
    private final SwagplashMapper mapper;
    private SPSwaggerDefinition swaggerDefinition = null;

    @Inject
    public SwagplashProvider(Application application, SwagplashMapper mapper) {
        this.application = application;
        this.mapper = mapper;
    }

    @Override
    public CompletionStage<SPSwaggerDefinition> get() {
        return CompletableFuture.supplyAsync(() -> {
            if (swaggerDefinition == null) {
                InputStream swagger = application.resourceAsStream("swagger.json");

                try {
                    swaggerDefinition = mapper.readValue(swagger, SPSwaggerDefinition.class);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
                return swaggerDefinition;
            } else {
                return swaggerDefinition;
            }
        });
    }

}
