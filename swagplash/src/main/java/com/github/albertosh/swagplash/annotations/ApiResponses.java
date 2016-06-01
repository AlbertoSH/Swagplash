package com.github.albertosh.swagplash.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A wrapper to allow a list of multiple {@link ApiResponse} objects.
 *
 * @see ApiResponse
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface ApiResponses {
    /**
     * A list of {@link ApiResponse}s provided by the API operation.
     */
    ApiResponse[] value() default {};
}
