package io.pivotal.android.ttc;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

public class NotificationAddActivity extends Activity {

    private static interface Extras {
        public static final String NOTIFICATION = "notification";
    }

    public static Notification getNotification(final Intent intent) {
        return (Notification) intent.getParcelableExtra(Extras.NOTIFICATION);
    }

    public static void newInstanceForResult(final Fragment fragment, final int requestCode) {
        final Intent intent = new Intent(fragment.getActivity(), NotificationAddActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_add);
    }
}
