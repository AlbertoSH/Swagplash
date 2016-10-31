package com.github.albertosh.swagplash.annotations;

import java.lang.annotation.*;

/**
 * Describes a possible response of an operation.
 * <p>
 * This can be used to describe possible success and error codes from your REST API call.
 * You may or may not use this to describe the return type of the operation (normally a
 * successful code), but the successful response should be described as well using the
 * {@link ApiOperation}.
 *
 * @see ApiOperation
 * @see ApiResponses
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Repeatable(value = ApiResponses.class)
public @interface ApiResponse {
    /**
     * The HTTP status code of the response.
     * <p>
     * The value should be one of the formal <a target="_blank" href="http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html">HTTP Status Code Definitions</a>.
     * If not set it will be "default"
     */
    int code() default -1;

    /**
     * Human-readable message to accompany the response.
     */
    String message();

    /**
     * Optional response class to describe the payload of the message.
     * <p>
     * Corresponds to the `schema` field of the response message object.
     */
    Class<?> response() default Void.class;

    /**
     * Declares a container wrapping the response.
     * <p>
     * Valid values are "List". Any other value will be ignored.
     */
    String responseContainer() default "";
}
