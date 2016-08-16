package com.github.albertosh.swagplash.actions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.albertosh.swagplash.annotations.ApiBodyParam;
import play.Logger;
import play.api.libs.Files;
import play.libs.Json;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import scala.annotation.meta.param;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ApiBodyParamAction extends Action<ApiBodyParam> {

    @Override
    public CompletionStage<Result> call(Http.Context ctx) {
        Optional<String> contentType = ctx.request().contentType();
        if (contentType.isPresent()) {
            if (contentType.get().contains("json")) {
                return handleJsonContentType(ctx);
            } else if (contentType.get().contains("multipart")) {
                return handleMultipartContentType(ctx);
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

    private CompletionStage<Result> handleMultipartContentType(Http.Context ctx) {
        if (configuration.required()) {
            return handleRequiredMultipart(ctx);
        } else {
            return handleNotRequiredMultipart(ctx);
        }
    }

    private CompletionStage<Result> handleRequiredMultipart(Http.Context ctx) {
        Http.MultipartFormData multipart = ctx.request().body().asMultipartFormData();
        if (multipart != null) {
            if (configuration.dataType() == ApiBodyParam.DataType.FILE) {
                Http.MultipartFormData.FilePart filePart = multipart.getFile(configuration.name());
                File file = null;
                if (filePart != null) {
                    file = (File) filePart.getFile();
                } else {
                    return CompletableFuture.completedFuture(badRequest("File \"" + configuration.name() + "\" not found!"));
                }
                Files.TemporaryFile tmpFile = new Files.TemporaryFile(file);
                ctx.args.put(configuration.name(), tmpFile);
            } else {
                String[] value = (String[]) multipart.asFormUrlEncoded().get(configuration.name());
                if (value != null) {
                    if (configuration.dataType() == ApiBodyParam.DataType.ARRAY) {
                        List values = new ArrayList();
                        for (int i = 0; i < value.length; i++) {
                            try {
                                values.add(configuration.dataType().toArgs(value[i], configuration.name(), configuration.contentDataType()));
                            } catch (IllegalArgumentException e) {
                                return CompletableFuture.completedFuture(badRequest(e.getMessage()));
                            }
                            ctx.args.put(configuration.name(), values);
                        }
                    } else {
                        if (value.length == 0) {
                            return CompletableFuture.completedFuture(badRequest("Field \"" + configuration.name() + "\" not found!"));
                        } else if (value.length == 1) {
                            try {
                                ctx.args.put(configuration.name(), configuration.dataType().toArgs(value[0], configuration.name(), configuration.contentDataType()));
                            } catch (IllegalArgumentException e) {
                                return CompletableFuture.completedFuture(badRequest(e.getMessage()));
                            }
                        } else {
                            return CompletableFuture.completedFuture(badRequest("Several values received for " + configuration.name()));
                        }
                    }
                } else {
                    return CompletableFuture.completedFuture(badRequest("Field \"" + configuration.name() + "\" not found!"));
                }
            }
        }
        return delegate.call(ctx);
    }

    private CompletionStage<Result> handleNotRequiredMultipart(Http.Context ctx) {
        Http.MultipartFormData multipart = ctx.request().body().asMultipartFormData();
        if (multipart != null) {
            if (configuration.dataType() == ApiBodyParam.DataType.FILE) {
                Http.MultipartFormData.FilePart filePart = multipart.getFile(configuration.name());
                if (filePart != null) {
                    File file = (File) filePart.getFile();
                    Files.TemporaryFile tmpFile = new Files.TemporaryFile(file);
                    ctx.args.put(configuration.name(), Optional.of(tmpFile));
                } else {
                    ctx.args.put(configuration.name(), Optional.empty());
                }
            } else {
                String[] value = (String[]) multipart.asFormUrlEncoded().get(configuration.name());
                if (value != null) {
                    if (configuration.dataType() == ApiBodyParam.DataType.ARRAY) {
                        List values = new ArrayList();
                        for (int i = 0; i < value.length; i++) {
                            try {
                                values.add(configuration.dataType().toArgs(value[i], configuration.name(), configuration.contentDataType()));
                            } catch (IllegalArgumentException e) {
                                return CompletableFuture.completedFuture(badRequest(e.getMessage()));
                            }
                            ctx.args.put(configuration.name(), Optional.of(values));
                        }
                    } else {
                        if (value.length == 0) {
                            ctx.args.put(configuration.name(), Optional.empty());
                        } else if (value.length == 1) {
                            try {
                                ctx.args.put(configuration.name(), Optional.of(configuration.dataType().toArgs(value[0], configuration.name(), configuration.contentDataType())));
                            } catch (IllegalArgumentException e) {
                                return CompletableFuture.completedFuture(badRequest(e.getMessage()));
                            }
                        } else {
                            return CompletableFuture.completedFuture(badRequest("Several values received for " + configuration.name()));
                        }
                    }
                } else {
                    ctx.args.put(configuration.name(), Optional.empty());
                }
            }
        }
        return delegate.call(ctx);
    }



    private CompletionStage<Result> handleJsonContentType(Http.Context ctx) {
        if (configuration.required()) {
            return handleRequiredJson(ctx);
        } else {
            return handleNotRequiredJson(ctx);
        }
    }

    private CompletionStage<Result> handleNotRequiredJson(Http.Context ctx) {
        Optional<String> contentType = ctx.request().contentType();
        String asJsonString = ctx.request().body().asBytes().decodeString("UTF-8");
        JsonNode node = Json.parse(asJsonString);
        JsonNode param = null;

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
                    if (configuration.dataType().equals(ApiBodyParam.DataType.ARRAY)) {
                        ArrayNode asArray = (ArrayNode) param;
                        int l = asArray.size();
                        if (l > 0) {
                            List<String> list = new ArrayList<>();
                            for (int i = 0; i < l; i++) {
                                JsonNode pNode = asArray.get(i);
                                JsonNode idNode = pNode.get("id");
                                String sId = (String) ApiBodyParam.DataType.MONGO_ID.toArgs(idNode, configuration.name(), null);
                                list.add(sId);
                            }
                            ctx.args.put(configuration.name(), Optional.of(list));
                        } else {
                            ctx.args.put(configuration.name(), Optional.empty());
                        }
                    } else {
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
        return delegate.call(ctx);
    }

    private CompletionStage<Result> handleRequiredJson(Http.Context ctx) {
        Optional<String> contentType = ctx.request().contentType();
        String asJsonString = ctx.request().body().asBytes().decodeString("UTF-8");
        JsonNode node = Json.parse(asJsonString);
        JsonNode param = null;

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
        return delegate.call(ctx);
    }


}
