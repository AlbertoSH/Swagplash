package com.github.albertosh.swagplash.sample;

import com.github.albertosh.swagplash.annotations.ApiModel;
import com.github.albertosh.swagplash.annotations.ApiModelProperty;

import javax.annotation.Nullable;

@ApiModel
public class SomeModel {

    @ApiModelProperty
    private int name;
    @ApiModelProperty
    @Nullable private String tag;
}
