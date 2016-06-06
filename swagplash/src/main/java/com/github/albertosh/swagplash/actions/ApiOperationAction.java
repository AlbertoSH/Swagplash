package com.github.albertosh.swagplash.actions;

import com.github.albertosh.swagplash.annotations.ApiOperation;
import com.github.albertosh.swagplash.model.SPSwaggerDefinition;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ApiOperationAction extends Action<ApiOperation> {

    private final SwagplashProvider swagplashProvider;

    @Inject
    public ApiOperationAction(SwagplashProvider swagplashProvider) {
        this.swagplashProvider = swagplashProvider;
    }

    @Override
    public CompletionStage<Result> call(Http.Context ctx) {
        return swagplashProvider.get()
                .thenCompose(swagplash -> {
                    if (swagplash == null) {
                        Logger.error("Could not load Swagplash definition");
                        return delegate.call(ctx);
                    } else {
                        String method = ctx.request().method();
                        if (method.equals("POST") || (method.equals("PUT"))) {
                            Optional<Result> consumesError = checkConsumes(ctx, swagplash);
                            if (consumesError.isPresent())
                                return CompletableFuture.completedFuture(consumesError.get());
                        }

                        Optional<Result> producesError = checkProduces(ctx, swagplash);
                        if (producesError.isPresent()) {
                            return CompletableFuture.completedFuture(producesError.get());
                        }

                        return delegate.call(ctx);
                    }
                });

    }

    /**
     * Check consumes.
     *
     * @param ctx
     * @param swagplash
     * @return the error wrapped into an Optional. empty if none
     */
    private Optional<Result> checkConsumes(Http.Context ctx, SPSwaggerDefinition swagplash) {
        List<String> consumes = new ArrayList<String>();
        Collections.addAll(consumes, configuration.consumes());
        if (consumes.isEmpty()) {
            consumes = swagplash.getConsumes();
        }
        if (!consumes.isEmpty()) {
            Optional<String> contentType = ctx.request().contentType();
            if (contentType.isPresent()) {
                String unwrapped = contentType.get();
                if (consumes.contains(unwrapped))
                    return Optional.empty();
                else
                    return Optional.of(badRequest("This endpoint can't consume " + unwrapped +
                            ".This endpoint consumes the following content-type: " + String.join(", ", consumes)));
            } else {
                return Optional.of(badRequest("This endpoint consumes the following content-type: " + String.join(", ", consumes)));
            }
        } else {
            return Optional.empty();
        }
    }

    /**
     * Check produces.
     *
     * @param ctx
     * @param swagplash
     * @return the error wrapped into an Optional. empty if none
     */
    private Optional<Result> checkProduces(Http.Context ctx, SPSwaggerDefinition swagplash) {
        String[] clientAccept = ctx.request().headers().get(Http.HeaderNames.ACCEPT);
        if ((clientAccept == null)
                || clientAccept.length == 0
                || clientAccept[0].equals("*/*")) {
            return Optional.empty();
        } else {
            List<String> produces = new ArrayList<String>();
            Collections.addAll(produces, configuration.produces());
            if (produces.isEmpty()) {
                produces = swagplash.getProduces();
            }
            if (produces.isEmpty()) {
                return Optional.empty();
            } else {
                List<String> accepts = new ArrayList<>();
                Collections.addAll(accepts, clientAccept);
                for (String p : produces) {
                    if (accepts.contains(p))
                        return Optional.empty();
                }
                return Optional.of(badRequest("This endpoint can't produce " + String.join(", ", clientAccept) +
                        ".This endpoint produces the following content-type: " + String.join(", ", produces)));
            }
        }
    }
}
