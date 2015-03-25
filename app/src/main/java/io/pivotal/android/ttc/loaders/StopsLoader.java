package io.pivotal.android.ttc.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;

import io.pivotal.android.ttc.TTCApi;
import io.pivotal.android.ttc.models.Stop;

public class StopsLoader extends AsyncTaskLoader<Stop.List> {

    private String mTag;

    public StopsLoader(final Context context, final String routeId) {
        super(context);
        mTag = routeId;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public Stop.List loadInBackground() {
        try {
            return TTCApi.getStops(mTag);
        } catch (final Exception e) {
            return null;
        }
    }
}
