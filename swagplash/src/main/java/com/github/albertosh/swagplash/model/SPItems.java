package com.github.albertosh.swagplash.model;

public class SPItems {

    private String $ref;
    private String type;
    private String format;
    private SPItems items;
    private Number maximum;
    private Boolean exclusiveMaximum;
    private Number minimum;
    private Boolean exclusiveMinimum;

    private SPItems() {
    }

    private SPItems(Builder builder) {
        $ref = builder.$ref;
        type = builder.type;
        format = builder.format;
        items = builder.items;
        maximum = builder.maximum;
        exclusiveMaximum = builder.exclusiveMaximum;
        minimum = builder.minimum;
        exclusiveMinimum = builder.exclusiveMinimum;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String get$ref() {
        return $ref;
    }

    public SPItems set$ref(String $ref) {
        this.$ref = $ref;
        return this;
    }

    public String getType() {
        return type;
    }

    public SPItems setType(String type) {
        this.type = type;
        return this;
    }

    public String getFormat() {
        return format;
    }

    public SPItems setFormat(String format) {
        this.format = format;
        return this;
    }

    public SPItems getItems() {
        return items;
    }

    public SPItems setItems(SPItems items) {
        this.items = items;
        return this;
    }

    public Number getMaximum() {
        return maximum;
    }

    public SPItems setMaximum(Number maximum) {
        this.maximum = maximum;
        return this;
    }

    public Boolean isExclusiveMaximum() {
        return exclusiveMaximum;
    }

    public Number getMinimum() {
        return minimum;
    }

    public SPItems setMinimum(Number minimum) {
        this.minimum = minimum;
        return this;
    }

    public Boolean isExclusiveMinimum() {
        return exclusiveMinimum;
    }

    public SPItems setExclusiveMaximum(Boolean exclusiveMaximum) {
        this.exclusiveMaximum = exclusiveMaximum;
        return this;
    }

    public SPItems setExclusiveMinimum(Boolean exclusiveMinimum) {
        this.exclusiveMinimum = exclusiveMinimum;
        return this;
    }

    public static final class Builder {
        private String $ref;
        private String type;
        private String format;
        private SPItems items;
        private Number maximum;
        private Boolean exclusiveMaximum;
        private Number minimum;
        private Boolean exclusiveMinimum;

        private Builder() {
        }

        public Builder withRef(String val) {
            $ref = "#/definitions/" + val;
            return this;
        }

        public Builder withType(String val) {
            type = val;
            return this;
        }

        public Builder withFormat(String val) {
            format = val;
            return this;
        }

        public Builder withItems(SPItems val) {
            items = val;
            return this;
        }

        public Builder withMaximum(Number val) {
            maximum = val;
            return this;
        }

        public Builder withExclusiveMaximum(boolean val) {
            exclusiveMaximum = val;
            return this;
        }

        public Builder withMinimum(Number val) {
            minimum = val;
            return this;
        }

        public Builder withExclusiveMinimum(boolean val) {
            exclusiveMinimum = val;
            return this;
        }

        public SPItems build() {
            return new SPItems(this);
        }
    }
}
