package io.pivotal.android.ttc.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import io.pivotal.android.push.service.GcmService;
import io.pivotal.android.ttc.R;
import io.pivotal.android.ttc.notification.list.NotificationsActivity;

public class TTCPushService extends GcmService {

    public static final int NOTIFICATION_ID = 1;
    private static final int NOTIFICATION_LIGHTS_ON_MS = 500;
    private static final int NOTIFICATION_LIGHTS_OFF_MS = 1000;

    @Override
    public void onReceiveMessage(final Bundle payload) {
        if (payload.containsKey("message")) {
            final String message = payload.getString("message");
            sendNotification(message);
        }
    }

    private void sendNotification(final String msg) {
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, NotificationsActivity.class), 0);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setLights(R.color.app_primary, NOTIFICATION_LIGHTS_ON_MS, NOTIFICATION_LIGHTS_OFF_MS)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setSmallIcon(R.drawable.ic_ttc_logo)
                .setContentIntent(contentIntent)
                .setContentText(msg);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

}