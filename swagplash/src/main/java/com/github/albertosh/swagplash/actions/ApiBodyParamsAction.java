package com.github.albertosh.swagplash.actions;

import com.github.albertosh.swagplash.annotations.ApiBodyParam;
import com.github.albertosh.swagplash.annotations.ApiBodyParams;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class ApiBodyParamsAction extends Action<ApiBodyParams> {

    @Override
    public CompletionStage<Result> call(Http.Context ctx) {
        if (configuration.value().length > 0) {
            int actions = configuration.value().length;
            List<Action<ApiBodyParam>> actionList = new ArrayList<>();
            for (int i = 0; i < actions; i++) {
                ApiBodyParamAction apiBodyParamAction = new ApiBodyParamAction();
                apiBodyParamAction.configuration = configuration.value()[i];
                actionList.add(apiBodyParamAction);
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
