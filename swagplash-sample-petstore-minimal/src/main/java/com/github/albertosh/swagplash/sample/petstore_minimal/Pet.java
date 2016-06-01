package com.github.albertosh.swagplash.sample.petstore_minimal;

import com.github.albertosh.swagplash.annotations.ApiModel;
import com.github.albertosh.swagplash.annotations.ApiModelProperty;

import javax.annotation.Nullable;

@ApiModel
public class Pet {

    @ApiModelProperty
    private long id;
    @ApiModelProperty
    private String name;
    @ApiModelProperty
    @Nullable private String tag;

}
