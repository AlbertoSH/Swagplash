package com.github.albertosh.swagplash.sample;

import com.github.albertosh.swagplash.annotations.Contact;
import com.github.albertosh.swagplash.annotations.Info;
import com.github.albertosh.swagplash.annotations.License;
import com.github.albertosh.swagplash.annotations.SwaggerDefinition;

@SwaggerDefinition(
        host = "localhost:9000",
        basePath = "/api",
        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS},
        consumes = {"application/json", "text/json"},
        produces = {"application/json", "text/json"},
        info = @Info(
                title = "Swagplash sample",
                version = "1.0.0",
                description = "This is a sample of how to use Swagplash to generate Swagger documentation and use its functionalities in Play Framework",
                contact = @Contact(
                        email = "alberto.sanz.herrero@gmail.com",
                        name = "Alberto Sanz",
                        url = "https://github.com/albertosh"
                ),
                license = @License(
                        name = "Apache 2",
                        url = "http://www.apache.org/licenses/LICENSE-2.0"
                )
        )
)
public class MySwaggerDefinition {

}
