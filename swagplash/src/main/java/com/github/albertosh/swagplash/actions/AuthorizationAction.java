package com.github.albertosh.swagplash.actions;

import com.github.albertosh.swagplash.annotations.SecureEndPoint;
import com.github.albertosh.swagplash.authorization.AuthorizationCheck;
import com.github.albertosh.swagplash.authorization.EmptyAuthorization;
import play.inject.Injector;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class AuthorizationAction extends Action<SecureEndPoint> {

    private final Injector injector;
    private final AuthorizationCheck authorizationCheck;

    @Inject
    public AuthorizationAction(Injector injector, AuthorizationCheck authorizationCheck) {
        this.injector = injector;
        this.authorizationCheck = authorizationCheck;
    }

    @Override
    public CompletionStage<Result> call(Http.Context ctx) {
        if (configuration.alternateChecker().equals(EmptyAuthorization.class)) {
            return authorizationCheck.doCheck(ctx, delegate);
        } else {
            AuthorizationCheck checker = injector.instanceOf(configuration.alternateChecker());
            return checker.doCheck(ctx, delegate);
        }

    }

}
