package com.github.albertosh.swagplash.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * License metadata available within the info section of a Swagger definition, see
 * https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#licenseObject
 *
 * @since 1.5.0
 */

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface License {

    /**
     * The name of the license.
     *
     * @return the name of the license
     */
    String name();

    /**
     * An optional URL for the license.
     *
     * @return an optional URL for the license.
     */
    String url() default "";
}
