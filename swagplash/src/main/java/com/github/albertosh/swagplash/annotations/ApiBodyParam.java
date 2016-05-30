package com.github.albertosh.swagplash.annotations;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.albertosh.swagplash.actions.ApiBodyParamAction;
import play.mvc.With;

import java.lang.annotation.*;
import java.time.LocalDate;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static play.mvc.Results.badRequest;
import static play.mvc.Results.redirect;

@With(ApiBodyParamAction.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = ApiBodyParams.class)
public @interface ApiBodyParam {
    /**
     * The parameter name.
     * <p>
     * The name of the parameter will be derived from the field/method/parameter name,
     * however you can override it.
     * <p>
     * Path parameters must always be named as the path section they represent.
     */
    String name();

    /**
     * A brief description of the parameter.
     */
    String value() default "";

    /**
     * Specifies if the parameter is required or not.
     */
    boolean required() default false;

    /**
     * The data type of the parameter.
     * <p>
     * This can be the class name or a primitive.
     */
    DataType dataType() default DataType.STRING;

    public enum DataType {
        STRING {
            @Override
            public String getType() {
                return "string";
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public Object toArgs(JsonNode node, String name) {
                String value;
                if (node.isNull())
                    value = null;
                else
                    value = node.asText();

                return value;
            }
        },
        INT {
            @Override
            public String getType() {
                return "integer";
            }

            @Override
            public String getFormat() {
                return "int32";
            }

            @Override
            public Object toArgs(JsonNode node, String name) throws IllegalArgumentException {
                String valueAsText = node.asText();
                try {
                    return Integer.parseInt(valueAsText);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Field \"" + name + "\" must be an integer value!");
                }
            }
        },
        BOOLEAN {
            @Override
            public String getType() {
                return "boolean";
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public Object toArgs(JsonNode node, String name) throws IllegalArgumentException {
                String valueAsText = node.asText();
                if (valueAsText.equals("true") || valueAsText.equals("false")) {
                    return node.asBoolean();
                } else {
                    throw new IllegalArgumentException("Field \"" + name + "\" must be a boolean value!");
                }
            }
        },
        DATE {
            @Override
            public String getType() {
                return "string";
            }

            @Override
            public String getFormat() {
                return "date";
            }

            @Override
            public Object toArgs(JsonNode node, String name) throws IllegalArgumentException {
                String valueAsText = node.asText();
                try {
                    LocalDate date = LocalDate.parse(valueAsText, DateTimeFormatter.ISO_DATE);
                    return date;
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Field \"" + name + "\" must be a valid date value with valid format: \n"
                            + "e.g. \"" + LocalDate.now().format(DateTimeFormatter.ISO_DATE) + "\"");
                }
            }
        },
        OFFSET_TIME {
            @Override
            public String getType() {
                return "string";
            }

            @Override
            public String getFormat() {
                return "time";
            }

            @Override
            public Object toArgs(JsonNode node, String name) throws IllegalArgumentException {
                String valueAsText = node.asText();
                try {
                    return OffsetTime.parse(valueAsText, DateTimeFormatter.ISO_OFFSET_TIME);
                } catch (DateTimeParseException e) {
                    return Optional.of(CompletableFuture.completedFuture(badRequest("Field \"" + name + "\" must be a valid time value with valid format: \n"
                            + "e.g. \"" + OffsetTime.now().format(DateTimeFormatter.ISO_TIME) + "\"")));
                }
            }
        };

        public abstract String getType();
        public abstract String getFormat();

        public abstract Object toArgs(JsonNode node, String name) throws IllegalArgumentException;
    }
}
