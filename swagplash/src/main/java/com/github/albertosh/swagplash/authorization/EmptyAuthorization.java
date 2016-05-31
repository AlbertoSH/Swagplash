package com.github.albertosh.swagplash.authorization;

import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * Default alternate authorization check
 * Allows all calls
 */
public class EmptyAuthorization implements AuthorizationCheck {

    @Override
    public CompletionStage<Result> doCheck(Http.Context ctx, Action<?> delegate) {
        return delegate.call(ctx);
    }

}
