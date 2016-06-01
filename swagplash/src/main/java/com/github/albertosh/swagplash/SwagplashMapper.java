package com.github.albertosh.swagplash;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.github.albertosh.swagplash.model.SPApiOperationSecurity;
import com.github.albertosh.swagplash.model.SPSecureEndPoint;

import java.io.IOException;
import java.util.Iterator;

public class SwagplashMapper extends ObjectMapper {

    public SwagplashMapper() {
        setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        SimpleModule m = new SimpleModule();

        m.addSerializer(SPApiOperationSecurity.class, new SPApiOperationSecuritySerializer());
        m.addDeserializer(SPApiOperationSecurity.class, new SPApiOperationSecurityDeserializer());

        registerModule(m);
    }

    private final static class SPApiOperationSecuritySerializer extends StdSerializer<SPApiOperationSecurity> {
        protected SPApiOperationSecuritySerializer() {
            super(SPApiOperationSecurity.class);
        }

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

    private final static class SPApiOperationSecurityDeserializer extends StdDeserializer<SPApiOperationSecurity> {

        protected SPApiOperationSecurityDeserializer() {
            super(SPApiOperationSecurity.class);
        }

        @Override
        public SPApiOperationSecurity deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            SPApiOperationSecurity spApiOperationSecurity = new SPApiOperationSecurity();
            ArrayNode arrayNode = p.readValueAsTree();
            Iterator<JsonNode> i = arrayNode.elements();
            while (i.hasNext()) {
                JsonNode node = i.next();
                String name = node.fieldNames().next();
                SPSecureEndPoint spSecureEndPoint = new SPSecureEndPoint();
                spSecureEndPoint.setName(name);
                ArrayNode scopes = (ArrayNode) node.get(name);
                Iterator<JsonNode> scopesI = scopes.iterator();
                while (scopesI.hasNext()) {
                    String scope = scopesI.next().asText();
                    spSecureEndPoint.addScope(scope);
                }
                spApiOperationSecurity.add(spSecureEndPoint);
            }

            return spApiOperationSecurity;
        }
    }
}
