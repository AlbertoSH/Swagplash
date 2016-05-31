package com.github.albertosh.swagplash.actions;

import com.github.albertosh.swagplash.annotations.SecureEndPoint;
import com.github.albertosh.swagplash.annotations.SecureEndPoints;
import com.github.albertosh.swagplash.authorization.AuthorizationCheck;
import play.inject.Injector;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class AuthorizationsAction extends Action<SecureEndPoints> {

    private final Injector injector;
    private final AuthorizationCheck authorizationCheck;

    @Inject
    public AuthorizationsAction(Injector injector, AuthorizationCheck authorizationCheck) {
        this.injector = injector;
        this.authorizationCheck = authorizationCheck;
    }

    @Override
    public CompletionStage<Result> call(Http.Context ctx) {
        if (configuration.value().length > 0) {
            int actions = configuration.value().length;
            List<Action<SecureEndPoint>> actionList = new ArrayList<>();
            for (int i = 0; i < actions; i++) {
                AuthorizationAction authorizationAction = new AuthorizationAction(injector, authorizationCheck);
                authorizationAction.configuration = configuration.value()[i];
                actionList.add(authorizationAction);
            }
            actionList.get(actions - 1).delegate = delegate;
            for (int i = 0; i < actions - 1; i++) {
                actionList.get(i).delegate = actionList.get(i + 1);
            }
            return actionList.get(0).call(ctx);
        } else {
            return delegate.call(ctx);
        }
    }

}
