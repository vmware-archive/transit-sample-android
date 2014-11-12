package io.pivotal.android.ttc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TTCPreferences {

    private static final class Keys {
        public static final String AUTHENTICATED = "authenticated";
        public static final String LAST_NOTIFICATION_TEXT = "last_notification_text";
        public static final String LAST_NOTIFICATION_TIME = "last_notification_time";
    }

    private static SharedPreferences getSharedPreferences(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static Boolean isAuthenticated(final Context context) {
        return getSharedPreferences(context).getBoolean(Keys.AUTHENTICATED, false);
    }

    public static void setIsAuthenticated(final Context context, final boolean authenticated) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(Keys.AUTHENTICATED, authenticated);
        editor.commit();
    }

    public static String getLastNotificationText(final Context context) {
        return getSharedPreferences(context).getString(Keys.LAST_NOTIFICATION_TEXT, null);
    }

    public static void setLastNotificationText(final Context context, final String lastNotification) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(Keys.LAST_NOTIFICATION_TEXT, lastNotification);
        editor.commit();
    }

    public static long getLastNotificationTime(final Context context) {
        return getSharedPreferences(context).getLong(Keys.LAST_NOTIFICATION_TIME, 0);
    }

    public static void setLastNotificationTime(final Context context, final long lastNotification) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(Keys.LAST_NOTIFICATION_TIME, lastNotification);
        editor.commit();
    }
}
