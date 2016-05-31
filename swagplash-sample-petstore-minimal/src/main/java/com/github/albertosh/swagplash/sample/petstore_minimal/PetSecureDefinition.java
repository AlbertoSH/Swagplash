package com.github.albertosh.swagplash.sample.petstore_minimal;

import com.github.albertosh.swagplash.annotations.SecureDefinition;
import com.github.albertosh.swagplash.authorization.AuthorizationCheck;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static play.mvc.Results.unauthorized;

@SecureDefinition(value = "dumb", description = "Dumb check.")
public class PetSecureDefinition implements AuthorizationCheck {

    @Override
    public CompletionStage<Result> doCheck(Http.Context ctx, Action<?> delegate) {
        String[] auths = ctx.request().headers().get(Http.HeaderNames.AUTHORIZATION);

        if ((auths.length == 1) && (auths[0].equals("pet")))
            return delegate.call(ctx);
        else
            return CompletableFuture.completedFuture(unauthorized());
    }

}
