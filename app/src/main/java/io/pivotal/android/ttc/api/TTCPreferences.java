package io.pivotal.android.ttc.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TTCPreferences {

    private static final class Keys {
        public static final String AUTHENTICATED = "authenticated";
        public static final String CONFIGURED = "configured";
    }

    public static Boolean isAuthenticated(final Context context) {
        return getSharedPreferences(context).getBoolean(Keys.AUTHENTICATED, false);
    }

    public static void setIsAuthenticated(final Context context, final boolean authenticated) {
        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(Keys.AUTHENTICATED, authenticated);
        editor.commit();
    }

    public static Boolean isConfigured(final Context context) {
        return getSharedPreferences(context).getBoolean(Keys.CONFIGURED, false);
    }

    public static Configuration getConfiguration(final Context context) {
        final SharedPreferences prefs = getSharedPreferences(context);
        return new Configuration(prefs);
    }

    public static void setConfiguration(final Context context, final Configuration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("configuration may not be null");
        }

        final SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        configuration.save(editor);
        editor.putBoolean(Keys.CONFIGURED, true);
        editor.commit();
    }

    private static SharedPreferences getSharedPreferences(final Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
