package com.github.albertosh.swagplash.actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.albertosh.swagplash.annotations.ApiBodyParam;
import play.Logger;
import play.libs.Json;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ApiBodyParamAction extends Action<ApiBodyParam> {

    @Override
    public CompletionStage<Result> call(Http.Context ctx) {
        Optional<String> contentType = ctx.request().contentType();
        if (contentType.isPresent()) {
            if (contentType.get().contains("json")) {
                String asJsonString = ctx.request().body().asBytes().decodeString("UTF-8");
                JsonNode node = Json.parse(asJsonString);
                JsonNode param = null;
                if (contentType.get().equals("application/vnd.api+json")) {
                    try {
                        if (configuration.name().equals("id")) {
                            param = node.get("data").get("id");
                        } else {
                            param = node.get("data").get("attributes").get(configuration.name());
                            if (param == null) {
                                JsonNode relation = node.get("data").get("relationships").get(configuration.name());
                                if (relation != null) {
                                    param = relation.get("data").get("id");
                                }
                            }
                        }

                    } catch (NullPointerException e) {}
                } else {
                    param = node.get(configuration.name());
                }
                if (configuration.required()) {
                    if (param == null) {
                        return CompletableFuture.completedFuture(badRequest("Field \"" + configuration.name() + "\" not found!"));
                    } else {
                        try {
                            ctx.args.put(configuration.name(), configuration.dataType().toArgs(param, configuration.name()));
                        } catch (IllegalArgumentException e) {
                            return CompletableFuture.completedFuture(badRequest(e.getMessage()));
                        }
                    }
                } else {
                    if (param == null) {
                        ctx.args.put(configuration.name(), Optional.empty());
                    } else {
                        try {
                            ctx.args.put(configuration.name(), Optional.of(configuration.dataType().toArgs(param, configuration.name())));
                        } catch (IllegalArgumentException e) {
                            return CompletableFuture.completedFuture(badRequest(e.getMessage()));
                        }
                    }
                }

                return delegate.call(ctx);
            } else {
                Logger.warn("Content-type " + contentType.get() + " not implemented yet or unknown!");
                return delegate.call(ctx);
            }
        } else {
            if (configuration.required()) {
                return CompletableFuture.completedFuture(badRequest("Content-type is needed when calling an endpoint with required body parameters"));
            } else {
                ctx.args.put(configuration.name(), Optional.empty());
                return delegate.call(ctx);
            }
        }
    }


}
