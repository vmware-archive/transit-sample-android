package io.pivotal.android.ttc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class TTCPreferences {

    private static final class Keys {
        public static final String AUTHENTICATED = "authenticated";
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
}
