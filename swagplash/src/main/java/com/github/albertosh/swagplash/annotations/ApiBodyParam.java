package com.github.albertosh.swagplash.annotations;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.albertosh.swagplash.actions.ApiBodyParamAction;

import org.apache.commons.codec.binary.Base64;

import play.api.libs.Files;
import play.mvc.With;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
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

    DataType contentDataType() default DataType.STRING;

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
            public Object toArgs(JsonNode node, String name, DataType arrayContentType) throws IllegalArgumentException {
                String value;
                if (node.isNull())
                    value = null;
                else
                    value = node.asText();

                return value;
            }

            @Override
            public Object toArgs(String valueAsString, String name, DataType arrayContentType) throws IllegalArgumentException {
                return valueAsString;
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
            public Object toArgs(JsonNode node, String name, DataType arrayContentType) throws IllegalArgumentException {
                String value;
                if (node.isNull())
                    value = null;
                else
                    value = node.asText();

                return value;
            }

            @Override
            public Object toArgs(String valueAsString, String name, DataType arrayContentType) throws IllegalArgumentException {
                return valueAsString;
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
            public Object toArgs(JsonNode node, String name, DataType arrayContentType) throws IllegalArgumentException {
                String valueAsString = node.asText();
                return toArgs(valueAsString, name, arrayContentType);
            }

            @Override
            public Object toArgs(String valueAsString, String name, DataType arrayContentType) throws IllegalArgumentException {
                try {
                    return Integer.parseInt(valueAsString);
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
            public Object toArgs(JsonNode node, String name, DataType arrayContentType) throws IllegalArgumentException {
                String valueAsString = node.asText();
                return toArgs(valueAsString, name, arrayContentType);
            }

            @Override
            public Object toArgs(String valueAsString, String name, DataType arrayContentType) throws IllegalArgumentException {
                if (valueAsString.toLowerCase().equals("true") || valueAsString.toLowerCase().equals("false")) {
                    return Boolean.valueOf(valueAsString);
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
            public Object toArgs(JsonNode node, String name, DataType arrayContentType) throws IllegalArgumentException {
                String valueAsString = node.asText();
                return toArgs(valueAsString, name, arrayContentType);
            }

            @Override
            public Object toArgs(String valueAsString, String name, DataType arrayContentType) throws IllegalArgumentException {
                try {
                    LocalDate date = LocalDate.parse(valueAsString, DateTimeFormatter.ISO_DATE);
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
            public Object toArgs(JsonNode node, String name, DataType arrayContentType) throws IllegalArgumentException {
                String valueAsString = node.asText();
                return toArgs(valueAsString, name, arrayContentType);
            }

            @Override
            public Object toArgs(String valueAsString, String name, DataType arrayContentType) throws IllegalArgumentException {
                try {
                    return OffsetTime.parse(valueAsString, DateTimeFormatter.ISO_OFFSET_TIME);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Field \"" + name + "\" must be a valid time value with valid format: \n"
                            + "e.g. \"" + OffsetTime.now().format(DateTimeFormatter.ISO_TIME) + "\"");
                }
            }
        },
        OFFSET_DATE_TIME {
            @Override
            public String getType() {
                return "string";
            }

            @Override
            public String getFormat() {
                return "date-time";
            }

            @Override
            public Object toArgs(JsonNode node, String name, DataType arrayContentType) throws IllegalArgumentException {
                String valueAsString = node.asText();
                return toArgs(valueAsString, name, arrayContentType);
            }

            @Override
            public Object toArgs(String valueAsString, String name, DataType arrayContentType) throws IllegalArgumentException {
                try {
                    return OffsetDateTime.parse(valueAsString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Field \"" + name + "\" must be a valid datetime value with valid format: \n"
                            + "e.g. \"" + OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + "\"");
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
            public Object toArgs(JsonNode node, String name, DataType arrayContentType) throws IllegalArgumentException {
                if (node.isNull()) {
                    return null;
                } else {
                    String valueAsString = node.asText();
                    return toArgs(valueAsString, name, arrayContentType);
                }
            }

            @Override
            public Object toArgs(String valueAsString, String name, DataType arrayContentType) throws IllegalArgumentException {
                if (isValid(valueAsString))
                    return valueAsString;
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
            public Object toArgs(JsonNode node, String name, DataType arrayContentType) throws IllegalArgumentException {
                String valueAsString = node.asText();
                return toArgs(valueAsString, name, arrayContentType);
            }

            @Override
            public Object toArgs(String valueAsString, String name, DataType arrayContentType) throws IllegalArgumentException {
                try {
                    return Duration.parse(valueAsString);
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
            public Object toArgs(JsonNode node, String name, DataType arrayContentType) throws IllegalArgumentException {
                String valueAsString = node.asText();
                return toArgs(valueAsString, name, arrayContentType);
            }

            @Override
            public Object toArgs(String valueAsString, String name, DataType arrayContentType) throws IllegalArgumentException {
                Pattern pattern = Pattern.compile(HEXA_COLOR_REGEX);
                if (pattern.matcher(valueAsString).matches())
                    return valueAsString;
                else
                    throw new IllegalArgumentException("Field \"" + name + "\" must be a valid color value\n"
                            + "e.g. \"#AAA\", \"#FFFFFF\", \"#FF0000FF\"");
            }
        },
        ARRAY {

            @Override
            public String getType() {
                return "array";
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public List<Object> toArgs(JsonNode node, String name, DataType arrayContentType) throws IllegalArgumentException {
                int size = node.size();
                List<Object> arguments = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    Object o = arrayContentType.toArgs(node.get(i), name, arrayContentType);
                    arguments.add(o);
                }
                return arguments;
            }

            @Override
            public Object toArgs(String valueAsString, String name, DataType arrayContentType) throws IllegalArgumentException {
                throw new IllegalStateException("This is not supposed to be called");
            }
        },
        JSON {
            @Override
            public String getType() {
                return "json";
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public Object toArgs(JsonNode node, String name, DataType arrayContentType) throws IllegalArgumentException {
                return node;
            }

            @Override
            public Object toArgs(String valueAsString, String name, DataType arrayContentType) throws IllegalArgumentException {
                throw new IllegalStateException("This is not supposed to be called");
            }

        },
        FILE {
            @Override
            public String getType() {
                return "file";
            }

            @Override
            public String getFormat() {
                return null;
            }

            @Override
            public Object toArgs(JsonNode node, String name, DataType arrayContentType) throws IllegalArgumentException {
                if (node.isNull())
                    return null;

                String value = node.asText();

                File temp = null;
                try {
                    temp = File.createTempFile(name, ".tmp");
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Failed to create tmp file: " + name);
                }

                byte[] data = Base64.decodeBase64(value);
                try (OutputStream stream = new FileOutputStream(temp)) {
                    stream.write(data);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // Shouldn't happen...
                    throw new RuntimeException("Failed to create tmp file: " + name);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Failed to write tmp file: " + name);
                }
                Files.TemporaryFile tmpFile = new Files.TemporaryFile(temp);

                return tmpFile;
            }

            @Override
            public Object toArgs(String valueAsString, String name, DataType arrayContentType) throws IllegalArgumentException {
                throw new IllegalStateException("Multipart does not decode files!");
            }
        };

        public abstract String getType();

        public abstract String getFormat();

        public abstract Object toArgs(JsonNode node, String name, DataType arrayContentType) throws IllegalArgumentException;

        public abstract Object toArgs(String valueAsString, String name, DataType arrayContentType) throws IllegalArgumentException;

    }
}
