package com.github.albertosh.swagplash.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Contact metadata available within the info section of a Swagger definition - see
 * https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#contactObject
 *
 * @since 1.5.0
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface Contact {

    /**
     * The name of the contact.
     *
     * @return the name of the contact
     */
    String name();

    /**
     * Optional URL associated with this contact.
     *
     * @return an optional URL associated with this contact
     */
    String url() default "";

    /**
     * Optional email for this contact.
     *
     * @return an optional email for this contact
     */
    String email() default "";
}
