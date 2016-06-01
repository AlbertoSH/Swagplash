package com.github.albertosh.swagplash.actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.albertosh.swagplash.annotations.ApiBodyParam;
import play.Logger;
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
                JsonNode node = ctx.request().body().asJson();
                JsonNode param = node.get(configuration.name());
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
