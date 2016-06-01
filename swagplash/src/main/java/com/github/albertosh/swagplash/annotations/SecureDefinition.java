package com.github.albertosh.swagplash.annotations;

import play.mvc.Http;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface SecureDefinition {

    /**
     * If empty the class name will be taken
     *
     * @return
     */
    String value() default "";

    String type() default "apiKey";

    String description() default "";

    String in() default "header";

    String name() default Http.HeaderNames.AUTHORIZATION;

}
