package com.github.albertosh.swagplash.annotations;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.albertosh.swagplash.actions.ApiBodyParamAction;
import play.mvc.With;

import java.lang.annotation.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

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
        PASSWORD {
            @Override
            public String getType() {
                return "string";
            }

            @Override
            public String getFormat() {
                return "password";
            }

            @Override
            public Object toArgs(JsonNode node, String name) throws IllegalArgumentException {
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
                    throw new IllegalArgumentException("Field \"" + name + "\" must be a valid time value with valid format: \n"
                            + "e.g. \"" + OffsetTime.now().format(DateTimeFormatter.ISO_TIME) + "\"");
                }
            }
        },
        MONGO_ID {
            @Override
            public String getType() {
                return "string";
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public Object toArgs(JsonNode node, String name) throws IllegalArgumentException {
                String valueAsText = node.asText();
                if (isValid(valueAsText))
                    return valueAsText;
                else
                    throw new IllegalArgumentException("Field \"" + name + "\" must be a valid MongoId!");
            }

            /**
             * Taken from org.bson.types.ObjectId
             * @return
             */
            private boolean isValid(String hexString) {
                int len = hexString.length();
                if (len != 24) {
                    return false;
                } else {
                    for (int i = 0; i < len; ++i) {
                        char c = hexString.charAt(i);
                        if ((c < 48 || c > 57) && (c < 97 || c > 102) && (c < 65 || c > 70)) {
                            return false;
                        }
                    }
                    return true;
                }
            }
        },
        DURATION {
            @Override
            public String getType() {
                return "string";
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public Object toArgs(JsonNode node, String name) throws IllegalArgumentException {
                String valueAsText = node.asText();
                try {
                    return Duration.parse(valueAsText);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Field \"" + name + "\" must be a valid duration value with valid format following the ISO-8601: \n"
                            + "e.g. \"P2DT3H4M\"");
                }
            }
        },
        HEXA_COLOR {
            private final String HEXA_COLOR_REGEX = "^#([0-9a-fA-F]{3}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8})$";
            @Override
            public String getType() {
                return "string";
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public Object toArgs(JsonNode node, String name) throws IllegalArgumentException {
                String value = node.asText();
                Pattern pattern = Pattern.compile(HEXA_COLOR_REGEX);
                if (pattern.matcher(value).matches())
                    return value;
                else
                    throw new IllegalArgumentException("Field \"" + name + "\" must be a valid color value\n"
                            + "e.g. \"#AAA\", \"#FFFFFF\", \"#FF0000FF\"");
            }
        };

        public abstract String getType();

        public abstract String getFormat();

        public abstract Object toArgs(JsonNode node, String name) throws IllegalArgumentException;
    }
}
