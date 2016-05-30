package com.github.albertosh.swagplash.annotations;

import play.mvc.Http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface ApiOperation {
    /**
     * Corresponds to the `summary` field of the operation.
     * <p>
     * Provides a brief description of this operation. Should be 120 characters or less
     * for proper visibility in Swagger-UI.
     */
    String value() default "";

    /**
     * Corresponds to the 'description' field of the operation.
     * <p>
     * A verbose description of the operation.
     */
    String description() default "";

    /**
     * A list of tags for API documentation control.
     * <p>
     * Tags can be used for logical grouping of operations by resources or any other qualifier.
     * A non-empty value will override the value received from {@link Api#value()} or {@link Api#tags()}
     * for this operation.
     */
    String[] tags() default {};

    /**
     * Corresponds to the `method` field as the HTTP method used.
     */
    HttpMethod httpMethod();

    /**
     * Path to append to basePath
     */
    String path();

    /**
     * Hides the operation from the list of operations.
     */
    boolean hidden() default false;

    public enum HttpMethod {
        GET, POST, PUT, DELETE
    }

    /**
     * Corresponds to the `produces` field of the operation.
     */
    String[] produces() default {};

    /**
     * Corresponds to the `consumes` field of the operation.
     */
    String[] consumes() default {};
}
