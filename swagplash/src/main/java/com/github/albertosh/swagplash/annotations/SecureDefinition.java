package com.github.albertosh.swagplash.annotations;

import com.github.albertosh.swagplash.actions.AuthorizationAction;
import play.mvc.Http;
import play.mvc.With;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface SecureDefinition {

    /**
     * If empty the class name will be taken
     * @return
     */
    String value() default "";

    String type() default "apiKey";

    String description() default "";

    String in() default "header";

    String name() default Http.HeaderNames.AUTHORIZATION;

}
