package com.github.albertosh.swagplash.annotations;

import com.github.albertosh.swagplash.actions.ApiBodyParamsAction;
import play.mvc.With;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@With(ApiBodyParamsAction.class)
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiBodyParams {
    ApiBodyParam[] value() default {};
}