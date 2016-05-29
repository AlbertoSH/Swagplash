package com.github.albertosh.swagplash.model;

import com.github.albertosh.swagplash.annotations.ApiBodyParam;
import com.github.albertosh.swagplash.annotations.ApiModelProperty;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SPSchema {

    private String $ref;
    private String type;
    private SPItems items;
    private String format;
    private List<String> required = new ArrayList<>();
    private Map<String, SPApiModelProperty> properties = new LinkedHashMap<>();

    public SPSchema() {
    }

    public static SPSchema fromTypeMirrorAndContainer(TypeMirror tm, String container, ExecutableElement element) {
        if (Utils.typeMirrorIsNotVoid(tm)) {

            if (Utils.typeMirrorIsNotApiModel(tm)) {
                Utils.warning("@ApiResponse.response type is not an @ApiModel", element);
            }

            SPSchema schema = new SPSchema();
            String type = tm.toString().replaceAll(".+\\.", "");
            switch (container) {
                case "List":
                    schema.type = "array";
                    schema.items = SPItems.newBuilder().withRef(type).build();
                    break;
                case "":
                    schema.$ref = "#/definitions/" + type;
                    break;
                default:
                    schema.$ref = "#/definitions/" + type;
                    Utils.warning("Unknown responseContainer", element);
            }

            return schema;
        } else {
            return null;
        }
    }

    public static SPSchema fromDataType(ApiBodyParam.DataType dataType) {
        SPSchema schema = new SPSchema();
        schema.type = dataType.getType();
        schema.format = dataType.getFormat();
        return schema;
    }

    public String get$ref() {
        return $ref;
    }

    public String getType() {
        return type;
    }

    public SPItems getItems() {
        return items;
    }

    public String getFormat() {
        return format;
    }

    public List<String> getRequired() {
        return required;
    }

    public Map<String, SPApiModelProperty> getProperties() {
        return properties;
    }

    public SPSchema set$ref(String $ref) {
        this.$ref = $ref;
        return this;
    }

    public SPSchema setType(String type) {
        this.type = type;
        return this;
    }

    public SPSchema setItems(SPItems items) {
        this.items = items;
        return this;
    }

    public SPSchema setFormat(String format) {
        this.format = format;
        return this;
    }


    public SPSchema addProperty(SPApiModelProperty property) {
        this.properties.put(property.getName(), property);
        if (property.isRequired())
            required.add(property.getName());
        return this;
    }
}
