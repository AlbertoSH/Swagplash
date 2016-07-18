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

                if (configuration.required()) {
                    if (contentType.get().equals("application/vnd.api+json")) {
                        if (configuration.name().equals("id")) {
                            if (node.get("data").has("id")) {
                                param = node.get("data").get("id");
                                try {
                                    ctx.args.put(configuration.name(), configuration.dataType().toArgs(param, configuration.name(), configuration.contentDataType()));
                                } catch (IllegalArgumentException e) {
                                    return CompletableFuture.completedFuture(badRequest(e.getMessage()));
                                }
                            } else {
                                return CompletableFuture.completedFuture(badRequest("Field \"" + configuration.name() + "\" not found!"));
                            }
                        } else {
                            if (node.get("data").get("attributes").has(configuration.name())) {
                                param = node.get("data").get("attributes").get(configuration.name());
                                try {
                                    ctx.args.put(configuration.name(), configuration.dataType().toArgs(param, configuration.name(), configuration.contentDataType()));
                                } catch (IllegalArgumentException e) {
                                    return CompletableFuture.completedFuture(badRequest(e.getMessage()));
                                }
                            } else if (node.get("data").get("relationships").has(configuration.name())) {
                                param = node.get("data").get("relationships").get(configuration.name()).get("data").get("id");
                                try {
                                    ctx.args.put(configuration.name(), configuration.dataType().toArgs(param, configuration.name(), configuration.contentDataType()));
                                } catch (IllegalArgumentException e) {
                                    return CompletableFuture.completedFuture(badRequest(e.getMessage()));
                                }
                            } else {
                                return CompletableFuture.completedFuture(badRequest("Field \"" + configuration.name() + "\" not found!"));
                            }
                        }
                    } else {
                        if (node.has(configuration.name())) {
                            param = node.get(configuration.name());
                            try {
                                ctx.args.put(configuration.name(), configuration.dataType().toArgs(param, configuration.name(), configuration.contentDataType()));
                            } catch (IllegalArgumentException e) {
                                return CompletableFuture.completedFuture(badRequest(e.getMessage()));
                            }
                        } else {
                            return CompletableFuture.completedFuture(badRequest("Field \"" + configuration.name() + "\" not found!"));
                        }
                    }
                } else {
                    if (contentType.get().equals("application/vnd.api+json")) {

                        if (configuration.name().equals("id")) {
                            if (node.get("data").has("id")) {
                                param = node.get("data").get("id");
                                try {
                                    ctx.args.put(configuration.name(), Optional.of(configuration.dataType().toArgs(param, configuration.name(), configuration.contentDataType())));
                                } catch (IllegalArgumentException e) {
                                    return CompletableFuture.completedFuture(badRequest(e.getMessage()));
                                }
                            } else {
                                ctx.args.put(configuration.name(), Optional.empty());
                            }
                        } else {
                            if (node.get("data").get("attributes").has(configuration.name())) {
                                param = node.get("data").get("attributes").get(configuration.name());
                                try {
                                    ctx.args.put(configuration.name(), Optional.of(configuration.dataType().toArgs(param, configuration.name(), configuration.contentDataType())));
                                } catch (IllegalArgumentException e) {
                                    return CompletableFuture.completedFuture(badRequest(e.getMessage()));
                                }
                            } else if ((node.get("data").has("relationships"))
                                    && (node.get("data").get("relationships").has(configuration.name()))) {
                                param = node.get("data").get("relationships").get(configuration.name()).get("data");
                                if (param.has("id")) {
                                    param = param.get("id");
                                    try {
                                        ctx.args.put(configuration.name(), Optional.of(configuration.dataType().toArgs(param, configuration.name(), configuration.contentDataType())));
                                    } catch (IllegalArgumentException e) {
                                        return CompletableFuture.completedFuture(badRequest(e.getMessage()));
                                    }
                                } else {
                                    ctx.args.put(configuration.name(), Optional.empty());
                                }
                            } else {
                                ctx.args.put(configuration.name(), Optional.empty());
                            }
                        }

                    } else {
                        param = node.get(configuration.name());
                        if (param == null) {
                            ctx.args.put(configuration.name(), Optional.empty());
                        } else {
                            try {
                                ctx.args.put(configuration.name(), Optional.of(configuration.dataType().toArgs(param, configuration.name(), configuration.contentDataType())));
                            } catch (IllegalArgumentException e) {
                                return CompletableFuture.completedFuture(badRequest(e.getMessage()));
                            }
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
