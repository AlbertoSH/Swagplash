package com.github.albertosh.swagplash;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.albertosh.swagplash.model.SPApiOperationSecurity;
import com.github.albertosh.swagplash.model.SPSecureDefinition;
import com.github.albertosh.swagplash.model.SPSecureEndPoint;

import java.io.IOException;

public class SwagplashMapper extends ObjectMapper {

    public SwagplashMapper() {
        setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        SimpleModule m = new SimpleModule();

        m.addSerializer(SPApiOperationSecurity.class, new SPApiOperationSecuritySerializer());
        registerModule(m);
    }


    private final static class SPApiOperationSecuritySerializer extends JsonSerializer<SPApiOperationSecurity> {
        @Override
        public void serialize(SPApiOperationSecurity value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            gen.writeStartArray();

            for (SPSecureEndPoint spSecureEndPoint : value.getSecurity()) {
                gen.writeStartObject();
                gen.writeFieldName(spSecureEndPoint.getName());
                gen.writeStartArray();
                for (String scope : spSecureEndPoint.getScopes())
                    gen.writeString(scope);
                gen.writeEndArray();
                gen.writeEndObject();
            }

            gen.writeEndArray();
        }
    }
}
