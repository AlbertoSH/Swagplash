package com.github.albertosh.swagplash.annotations;

import com.github.albertosh.swagplash.actions.AuthorizationAction;
import com.github.albertosh.swagplash.authorization.AuthorizationCheck;
import com.github.albertosh.swagplash.authorization.EmptyAuthorization;
import play.mvc.With;

import java.lang.annotation.*;

@With(AuthorizationAction.class)
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = SecureEndPoints.class)
public @interface SecureEndPoint {

    /**
     * Security definition used
     * @return
     */
    String value();

    /**
     * Overrides de default checker
     */
    Class<? extends AuthorizationCheck> alternateChecker() default EmptyAuthorization.class;

}
