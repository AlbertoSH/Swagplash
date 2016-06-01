package com.github.albertosh.swagplash.annotations;

import java.lang.annotation.*;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
public @interface Api {
    /**
     * Implicitly sets a tag for the operations.
     * <p>
     * If {@link #tags()} is <i>not</i> used, this value will be used to set the tag for the operations described by this
     * resource. Otherwise, the value will be ignored.
     * <p>
     * The leading / (if exists) will be removed.
     *
     * @return tag name for operations under this resource, unless {@link #tags()} is defined.
     */
    String value() default "";

    /**
     * A list of tags for API documentation control.
     * Tags can be used for logical grouping of operations by resources or any other qualifier.
     * <p>
     * A non-empty value will override the value provided in {@link #value()}.
     *
     * @return a string array of tag values
     */
    String[] tags() default {};

    /**
     * Corresponds to the `produces` field of the operations under this resource.
     *
     * @return the supported media types supported by the server, or an empty string if not set.
     */
    String[] produces() default {};

    /**
     * Corresponds to the `consumes` field of the operations under this resource.
     *
     * @return the consumes value, or empty string if not set
     */
    String[] consumes() default {};


    /**
     * Hides the operations under this resource.
     *
     * @return true if the api should be hidden from the swagger documentation
     */
    boolean hidden() default false;
}
