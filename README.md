# Swagplash

## Overview

Generating documentation for our API is a tedious task. Lots of things to write and mantain up-to-date to out code changes. Wouldn't it be nice to let it generate by itself and evolve with our code?

**Swagplash** provides an easy way of generating [Swagger](http://swagger.io/) documentation in addition to some utils to [Play Framework](https://www.playframework.com/) 

---

## Features

* Generate Swagger specification for your API at compile time
* Verbose code
* Body parameters check (**not implemented yet but scheduled in the roadmap**)

--- 

## Usage

Swagplash is based in static code analysis with annotation support. It can make "some magic" but it's not a wizard. You'll have to let it know some data:

**NOTE:** this example will show how to generate the [petstore-minimal](https://github.com/OAI/OpenAPI-Specification/blob/master/examples/v2.0/json/petstore-minimal.json) example. You can find how to generate the remaining examples in this repo

* **Add the dependency**: Add **Swagplash** to your dependencies (you can find latest version at top)

```
libraryDependencies += "com.github.albertosh" % "swagplash" % "1.0.0"
``

* **Specify basic info**: Create a class and annotate it with `@SwaggerDefinition`. In this annotation you can specify the basic info of your API. You don't even hava to use it at runtime or even have a public constructor. All Swagplash needs is the info in the annotation:

``` java
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
```
* **Define your models**: Annotate your model classes with `@ApiModel` and its fields with `@ApiModelProperty`. All primitive fields are required. Non primitive fields are required unless they have the `@Nullable` annotation:

``` java
@ApiModel
public class Pet {

    @ApiModelProperty
    private long id;
    @ApiModelProperty
    private String name;
    @ApiModelProperty
    @Nullable private String tag;

}
```

* **Define your controllers**: Annotate your controllers with `@Api`:

``` java
@Api
public class PetController {
    ...
}
```

* **Define your endpoints**: Annotate your methods with `@ApiOperation`

``` java
@ApiOperation(
            description = "Returns all pets from the system that the user has access to",
            httpMethod = ApiOperation.HttpMethod.GET,
            path = "/pets")
    @ApiResponse(
            code = 200,
            message = "A list of pets.",
            responseContainer = "List",
            response = Pet.class
    )
    public Result getPets() {
        ...
    }
``` 

* **Compile**: And you're done! Your `Swagger.json` file will be generated and stored at `{your project path}/target/scala-2.11/classes/Swagger.json`. The only remaining thing is to create and endpoint that will serve that file:

``` java
public class DocController extends Controller {

    private final Application application;

    @Inject
    public DocController(Application application) {
        this.application = application;
    }

    public CompletionStage<Result> getDoc() {
        return CompletableFuture.supplyAsync(() -> {
            File swagger = application.getFile("target/scala-2.11/classes/swagger.json");

            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(swagger));
            } catch (FileNotFoundException e) {
                return internalServerError("Swagger file not found!");
            }
            try {
                StringBuilder sb = new StringBuilder();
                String line = br.readLine();

                while (line != null) {
                    sb.append(line);
                    sb.append("\n");
                    line = br.readLine();
                }
                return ok(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return internalServerError("Fail while reading Swagger file!");

        });
    }
}
```
