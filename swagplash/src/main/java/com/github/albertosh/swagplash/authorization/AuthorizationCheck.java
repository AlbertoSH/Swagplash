package com.github.albertosh.swagplash.authorization;

import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

/**
 * Performs the authorization check
 */
public interface AuthorizationCheck {

    /**
     * Performs a check.
     * @param ctx
     * @param delegate
     * @return delegate.call(ctx) if everything went OK. CompletionStage<Result> with the result otherwise
     */
    public CompletionStage<Result> doCheck(Http.Context ctx, Action<?> delegate);

}
