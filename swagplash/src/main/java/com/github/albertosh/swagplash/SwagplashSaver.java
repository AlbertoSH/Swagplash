package com.github.albertosh.swagplash;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.albertosh.swagplash.model.SPSwaggerDefinition;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;

public class SwagplashSaver {

    private final ObjectMapper mapper;

    public SwagplashSaver() {
        mapper = new ObjectMapper();
        configureMapper(mapper);
    }

    private void configureMapper(ObjectMapper mapper) {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }

    public void save(SPSwaggerDefinition swagger, ProcessingEnvironment processingEnv) {
        try {
            // TODO define this
            FileObject fileObject = processingEnv.getFiler().createResource(
                    StandardLocation.SOURCE_OUTPUT,
                    "com.github.albertosh.swagplash",
                    "swagger.json"
            );

            OutputStream os = fileObject.openOutputStream();
            mapper.writerWithDefaultPrettyPrinter().writeValue(os, swagger);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
