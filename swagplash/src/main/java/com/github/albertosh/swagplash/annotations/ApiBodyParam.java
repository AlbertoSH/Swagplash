package com.github.albertosh.swagplash.annotations;

import java.lang.annotation.*;

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
        };

        public abstract String getType();
        public abstract String getFormat();
    }
}
