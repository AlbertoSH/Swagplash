package com.github.albertosh.swagplash.sample;

import com.github.albertosh.swagplash.annotations.*;
import play.mvc.Http;

import static com.github.albertosh.swagplash.annotations.ApiOperation.HttpMethod.GET;
import static com.github.albertosh.swagplash.annotations.ApiOperation.HttpMethod.POST;
import static com.github.albertosh.swagplash.annotations.ApiOperation.HttpMethod.PUT;

@Api("Some tag")
public class SomeController {

    @ApiOperation(value = "Returns something", httpMethod = GET, path = "/somePath")
    @ApiResponse(code = Http.Status.OK, message = "Some OK message")
    public void getSomething() {

    }

    @ApiOperation(value = "Returns another thing", httpMethod = GET, path = "/anotherPath")
    @ApiResponse(code = Http.Status.OK, message = "Other OK message", response = SomeModel.class)
    public void anotherGetSomething() {

    }

    @ApiOperation(value = "Returns an array of things", httpMethod = GET, path = "/arrayPath")
    @ApiResponse(code = Http.Status.OK, message = "Other OK message and array", response = SomeModel.class, responseContainer = "List")
    public void getAnArray() {

    }

    @ApiOperation(value = "Update something by id", httpMethod = PUT, path = "/somePath/{id}")
    @ApiResponse(code = Http.Status.OK, message = "Some OK message", response = SomeModel.class)
    public void updateSomethingWithPathParameter(@ApiPathParam long id) {

    }

    @ApiOperation(value = "Do some query", httpMethod = GET, path = "/someQueryPath")
    @ApiResponse(code = Http.Status.OK, message = "Some OK message")
    public void getSomethingWithQueryParameter(@ApiQueryParam(defaultValue = "20") long limit) {

    }

    @ApiOperation(value = "Create something", httpMethod = POST, path = "/createSomethingPath")
    @ApiBodyParam(name = "someRequiredValue", required = true)
    @ApiBodyParam(name = "someNotRequiredInt", dataType = ApiBodyParam.DataType.INT)
    @ApiResponse(code = Http.Status.CREATED, message = "Some Created message")
    public void createSomethingWithBodyParam() {

    }
}
