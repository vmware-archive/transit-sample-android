package io.pivotal.android.ttc;

import android.app.Application;

import io.pivotal.android.push.util.Logger;

public class TTCApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.setup(this);
    }
}
