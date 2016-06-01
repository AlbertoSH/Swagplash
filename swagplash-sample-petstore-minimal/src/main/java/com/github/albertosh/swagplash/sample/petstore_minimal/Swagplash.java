package com.github.albertosh.swagplash.sample.petstore_minimal;

import com.github.albertosh.swagplash.annotations.Contact;
import com.github.albertosh.swagplash.annotations.Info;
import com.github.albertosh.swagplash.annotations.License;
import com.github.albertosh.swagplash.annotations.SwaggerDefinition;

@SwaggerDefinition(
        info = @Info(
                title = "Swagger Petstore",
                description = "A sample API that uses a petstore as an example to demonstrate features in the swagger-2.0 specification",
                version = "1.0.0",
                termsOfService = "http://swagger.io/terms/",
                contact = @Contact(
                        name = "Swagger API Team"
                ),
                license = @License(
                        name = "MIT"
                )
        ),
        host = "petstore.swagger.io",
        basePath = "/api",
        schemes = SwaggerDefinition.Scheme.HTTP,
        consumes = "application/json",
        produces = "application/json"
)
class Swagplash {}
