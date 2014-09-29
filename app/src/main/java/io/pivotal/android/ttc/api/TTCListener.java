package io.pivotal.android.ttc.api;

import android.os.Handler;
import android.os.Looper;

import io.pivotal.android.data.DataListener;
import io.pivotal.android.data.DataObject;

public abstract class TTCListener implements DataListener {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public abstract void success(DataObject dataObject);
    public abstract void unauthorized(DataObject dataObject);
    public abstract void failure(DataObject dataObject, String s);

    @Override
    public final void onSuccess(final DataObject dataObject) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                TTCListener.this.success(dataObject);
            }
        });
    }

    @Override
    public final void onUnauthorized(final DataObject dataObject) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                TTCListener.this.unauthorized(dataObject);
            }
        });
    }

    @Override
    public final void onFailure(final DataObject dataObject, final String s) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                TTCListener.this.failure(dataObject, s);
            }
        });
    }
}
