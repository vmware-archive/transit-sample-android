package io.pivotal.android.ttc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class NotificationsActivity extends Activity {

    public static void newInstance(final Activity activity) {
        final Intent intent = new Intent(activity, NotificationsActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
    }
}
