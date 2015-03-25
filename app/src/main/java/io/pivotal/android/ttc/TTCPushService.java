package io.pivotal.android.ttc;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import io.pivotal.android.push.service.GcmService;
import io.pivotal.android.push.util.Logger;
import io.pivotal.android.ttc.activities.NotificationsActivity;

public class TTCPushService extends GcmService {

    public static final int NOTIFICATION_ID = 1;
    public static final String NOTIFICATION_RECEIVED = "io.pivotal.android.ttc.TTCPushService.NOTIFICATION_RECEIVED";
    private static final int NOTIFICATION_LIGHTS_ON_MS = 500;
    private static final int NOTIFICATION_LIGHTS_OFF_MS = 1000;

    @Override
    public void onReceiveMessage(final Bundle payload) {
        Logger.d("Received push notification: " + payload);
        if (payload.containsKey("message")) {
            final String message = payload.getString("message");
            saveNotification(message);
            sendNotification(message);
            sendBroadcast();
        }
    }

    private void saveNotification(String message) {
        TTCPreferences.setLastNotificationText(this, message.trim());
        TTCPreferences.setLastNotificationTime(this, System.currentTimeMillis());
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

    private void sendBroadcast() {
        final Intent intent = new Intent(NOTIFICATION_RECEIVED);
        sendBroadcast(intent);
    }

}