package io.pivotal.android.ttc.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import io.pivotal.android.ttc.TTCApi;
import io.pivotal.android.ttc.models.Route;

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
