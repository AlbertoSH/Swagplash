package com.github.albertosh.swagplash.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Adds and manipulates data of a model property.
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface ApiModelProperty {
    /**
     * A brief description of this property.
     */
    String value() default "";

    /**
     * Allows overriding the name of the property.
     *
     * @return the overridden property name
     */
    String name() default "";

    /**
     * Allows a model property to be hidden in the Swagger model definition.
     */
    boolean hidden() default false;

}
