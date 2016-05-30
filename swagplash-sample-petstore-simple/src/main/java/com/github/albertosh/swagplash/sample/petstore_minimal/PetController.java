package com.github.albertosh.swagplash.sample.petstore_minimal;

import com.github.albertosh.swagplash.annotations.Api;
import com.github.albertosh.swagplash.annotations.ApiOperation;
import com.github.albertosh.swagplash.annotations.ApiQueryParam;
import com.github.albertosh.swagplash.annotations.ApiResponse;
import play.mvc.Result;

@Api
public class PetController {

    @ApiOperation(
            description = "Returns all pets from the system that the user has access to",
            httpMethod = ApiOperation.HttpMethod.GET,
            path = "/pets")
    @ApiResponse(
            code = 200,
            message = "pet response",
            responseContainer = "List",
            response = Pet.class
    )
    public Result getPets(
            @ApiQueryParam String[] tags,
            @ApiQueryParam int limit
            ) {
        return null;
    }

}
