package io.pivotal.android.ttc.notification.list;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import io.pivotal.android.ttc.R;
import io.pivotal.android.ttc.service.TTCPushService;

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

    @Override
    protected void onResume() {
        super.onResume();
        clearNotificationsFromStatus();
    }

    private void clearNotificationsFromStatus() {
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(TTCPushService.NOTIFICATION_ID);
    }
}
