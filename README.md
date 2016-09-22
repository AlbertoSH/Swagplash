# Swagplash

[ ![Download](https://api.bintray.com/packages/albertosh/maven/swagplash/images/download.svg) ](https://bintray.com/albertosh/maven/swagplash/_latestVersion)

 
Play Framework utilities and Swagger documentation generator at compile time

## Overview

Generating documentation for our API is a tedious task. Lots of things to write and maintain up-to-date to out code changes. Wouldn't it be nice to let it generate by itself and evolve with our code?

**Swagplash** provides an easy way of generating [Swagger](http://swagger.io/) documentation in addition to some utils to [Play Framework](https://www.playframework.com/) 

---

## Features

* Generate Swagger specification for your API at compile time
* Verbose code
* Body parameters check
* Security check

--- 

## How to start

Swagplash is based in static code analysis with annotation support. It can make "some magic" but it's not a wizard. You'll have to let it know some data:

**NOTE:** this example will show how to generate the [petstore-minimal](https://github.com/OAI/OpenAPI-Specification/blob/master/examples/v2.0/json/petstore-minimal.json) example. You can find how to generate the remaining examples in this repo

* **Add the dependency**: Add **Swagplash** to your dependencies (you can find latest version at top)

```
libraryDependencies += "com.github.albertosh" % "swagplash" % "1.0.0"
```

* **Specify basic info**: Create a class and annotate it with `@SwaggerDefinition`. In this annotation you can specify the basic info of your API. You don't even hava to use it at runtime or even have a public constructor. All Swagplash needs is the info in the annotation:

```java
@SwaggerDefinition(
    info = @Info(
        title = "Swagger Petstore",
        description = "A sample API that uses a petstore as an example to demonstrate features in the swagger-2.0 specification",
        version = "1.0.0",
         termsOfService = "http://swagger.io/terms/",
        contact = @Contact(name = "Swagger API Team"),
        license = @License(name = "MIT")
        ),
    host = "petstore.swagger.io",
    basePath = "/api",
    schemes = SwaggerDefinition.Scheme.HTTP,
    consumes = "application/json",
    produces = "application/json")
    class Swagplash {}
```

* **Define your models**: Annotate your model classes with `@ApiModel` and its fields with `@ApiModelProperty`. All primitive fields are required. Non primitive fields are required unless they have the `@Nullable` annotation:

```java
@ApiModel
public class Pet {

    @ApiModelProperty
    private long id;
    @ApiModelProperty
    private String name;
    @ApiModelProperty
    @Nullable private String tag;

    ...
}
```

* **Define your controllers**: Annotate your controllers with `@Api`:

```java
@Api
public class PetController {
    ...
}
```

* **Define your endpoints**: Annotate your methods with `@ApiOperation`

```java
@ApiOperation(
    description = "Returns all pets from the system that the user has access to",
    httpMethod = ApiOperation.HttpMethod.GET,
    path = "/pets")
@ApiResponse(
    code = 200,
    message = "A list of pets.",
    responseContainer = "List",
    response = Pet.class)
public Result getPets() {
    ...
}
``` 

* **Compile**: And you're done! Your `Swagger.json` file will be generated and stored at `{your project path}/target/scala-2.11/classes/Swagger.json`. The only remaining thing is to create and endpoint that will serve that file. Or you can just use `SwagplashProvider` in combination with `SwagplashMapper` to serve the json:

```java
public CompletionStage<Result> getDoc() {
    return provider.get()
            .thenApply(swagger -> {
                try {
                    return ok(mapper.writeValueAsString(swagger));
                } catch (JsonProcessingException e) {
                    return internalServerError("Something failed while writing swagger.json");
                }
            });
    }
    
```

---

## Extra usage

As said before, **Swagplash** provides body parameters check and security check

### Body parameter check

Annotate your method with `@ApiBodyParam`. It will check that the body passed has the field indicated in the `name` property. It will also check its type. Once the method is reached (all parameters were ok) you can receive them via `ctx().args.get(name)` and cast them to the expected type. Be aware that if the parameter is not required it will passed as an `Optional`

```java
@ApiOperation(httpMethod = POST, path = "/somePath")
@ApiBodyParam(name = "someParam", required = true)
@ApiBodyParam(name = "otherParam")
public Result doSomething() {
    String someParam = (String) ctx().args.get("someParam");
    Optional<String> otherParam = (Optional<String>) ctx().args.get("otherParam");
    ....
}        
```

Current supported types are:
* String (default)
* Integer
* Boolean
* Date (ISO-8601 Date format. e.g. `2016-06-01` or `2016-06-01+02:00`)
* OffsetTime (ISO-8601 time format. e.g. `10:00:00` or `10:00:00+02:00`)
* Duration (ISO-8601 duration format. e.g. `P23DT23H`)
* MongoId (Performs a check assuring that this value can became a valid `ObjectId` **but it will passed to the controller as a `String`**. You know, your controller doesn't need to know that you are working with MongoDB)


### Security check

You'll have to provide a class that implements `AuthorizationCheck`. Its `doCheck` method will be called with the context and the delegate that should do the following action. If the call is authorized to perform the action `delegate.call(ctx)` should be returned. Otherwise return the `Result` wrapped into a `CompletableFuture.completedValue`

``` java
public class SomeAuthorization implements AuthorizationCheck {

    @Override
    public CompletionStage<Result> doCheck(Http.Context ctx, Action<?> delegate) {
        if (checkSomething())
            return delegate.call(ctx);
        else
            return CompletableFuture.completedValue(forbidden());
    }

}
```
 You'll also need to tell Guice (the DI framework Play Framework works with) how to load your `AuthorizationCheck` class. You can find more info about this [in the Dependency Injection](https://www.playframework.com/documentation/2.5.x/JavaDependencyInjection#programmatic-bindings) section of the Play Framework doc.
 If you're too lazy to read it (shame on you) here is the snippet you'll need:

``` java
public class MyAuthorizationModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AuthorizationCheck.class).to(MyAuthorizationCheck.class);
    }
}
```
and add this line to your `conf/aplication.conf`:

    play.modules.enabled += "some.package.MyAuthorizationModule"

This way you specify a **default** `AuthorizationCheck` class. But in some methods you may want to specify a different `AuthorizationCheck`. You can do it by specifying it in the `@SecureEndPoint` annotation:

``` java
@SecureEndPoint(value = "other", alternateChecker = OtherChecker.class)
public Result anotherCheckMethod() {
    ...
}
```

This `AuthorizationCheck` class should also has `@SecureDefinition` in order to generate Swagger documentation:

``` java
@SecureDefinition(name = "special")
public class SpecialAuth implements AuthorizationCheck {

    @Override
    public CompletionStage<Result> doCheck(Http.Context context, Action<?> delegate) {
        ...
    }

}
```

---

##License
 
The MIT License (MIT)

    Copyright (c) 2016 Alberto Sanz

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
