package io.pivotal.android.ttc.route;

import android.content.AsyncTaskLoader;
import android.content.Context;

import io.pivotal.android.ttc.api.TTCApi;

public class RoutesLoader extends AsyncTaskLoader<Route.List> {

    public RoutesLoader(final Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Route.List loadInBackground() {
        try {
            return TTCApi.getRoutes();
        } catch (final Exception e) {
            return null;
        }
    }
}
