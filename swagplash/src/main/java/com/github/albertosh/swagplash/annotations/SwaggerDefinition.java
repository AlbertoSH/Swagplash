package com.github.albertosh.swagplash.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
public @interface SwaggerDefinition {

    /**
     * The host to specify in the generated Swagger definition.
     *
     * @return the host to specify in the generated Swagger definition - keep empty for default
     */
    String host() default "";

    /**
     * The basePath to specify in the generated Swagger definition.
     *
     * @return the basePath to specify in the generated Swagger definition - keep empty for default
     */
    String basePath() default "";

    /**
     * Global level consumes for this swagger definition.
     * <p>
     * These will be added to all api definitions that don't have local overrides - see
     * https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#swagger-object
     *
     * @return a list of global level consumes.
     */
    String[] consumes() default "";

    /**
     * Global level produces for this swagger definition.
     * <p>
     * These will be added to all api definitions that don't have local overrides - see
     * https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#swagger-object
     *
     * @return a list of global level consumes
     */
    String[] produces() default "";

    /**
     * The transfer protocol of the API.
     * <p>
     * Setting this to Scheme.DEFAULT will result in the result being generated from the hosting container.
     *
     * @return list of supported transfer protocols, keep empty for default
     */
    Scheme[] schemes() default Scheme.DEFAULT;

    /**
     * General metadata for this Swagger definition.
     * <p>
     * See https://github.com/OAI/OpenAPI-Specification/blob/master/versions/2.0.md#infoObject
     *
     * @return general metadata for this Swagger definition
     */
    Info info() default @Info(title = "", version = "");

    /**
     * Enumeration with valid schemes
     */
    enum Scheme {
        DEFAULT, HTTP, HTTPS, WS, WSS
    }
}
