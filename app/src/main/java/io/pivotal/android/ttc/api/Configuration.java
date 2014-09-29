package io.pivotal.android.ttc.api;

import android.content.SharedPreferences;

public class Configuration {

    private static final class Keys {
        public static final String AUTH_SERVICE_URL = "authServiceUrl";
        public static final String AUTH_CLIENT_ID = "authClientId";
        public static final String AUTH_CLIENT_SECRET = "authClientSecret";
        public static final String AUTH_REDIRECT_URL = "authRedirectUrl";
        public static final String DATA_SERVICE_URL = "dataServiceUrl";
        public static final String PUSH_SERVICE_URL = "pushServiceUrl";
        public static final String PUSH_GCM_SENDER_ID = "pushGcmSenderId";
        public static final String PUSH_VARIANT_UUID = "pushVariantUuid";
        public static final String PUSH_VARIANT_SECRET = "pushVariantSecret";
        public static final String ROUTE_SERVICE_URL = "routeServiceUrl";
    }

    public String authServiceUrl;
    public String authClientId;
    public String authClientSecret;
    public String authRedirectUrl;
    public String dataServiceUrl;
    public String pushServiceUrl;
    public String pushGcmSenderId;
    public String pushVariantUuid;
    public String pushVariantSecret;
    public String routeServiceUrl;


    public static Configuration getDefault() {
        // TODO - put in the real default configuration
        return new Configuration();
    }

    public Configuration() {
    }

    /**
     * Loads configuration from the app's shared preferences.
     *
     * @param prefs
     */
    public Configuration(final SharedPreferences prefs) {
        authServiceUrl = prefs.getString(Keys.AUTH_SERVICE_URL, null);
        authClientId = prefs.getString(Keys.AUTH_CLIENT_ID, null);
        authClientSecret = prefs.getString(Keys.AUTH_CLIENT_SECRET, null);
        authRedirectUrl = prefs.getString(Keys.AUTH_REDIRECT_URL, null);
        dataServiceUrl = prefs.getString(Keys.DATA_SERVICE_URL, null);
        pushServiceUrl = prefs.getString(Keys.PUSH_SERVICE_URL, null);
        pushGcmSenderId = prefs.getString(Keys.PUSH_GCM_SENDER_ID, null);
        pushVariantUuid = prefs.getString(Keys.PUSH_VARIANT_UUID, null);
        pushVariantSecret = prefs.getString(Keys.PUSH_VARIANT_SECRET, null);
        routeServiceUrl = prefs.getString(Keys.ROUTE_SERVICE_URL, null);
    }

    /**
     * Saves this configuration object to the given shared preferences.
     *
     * @param editor
     */
    public void save(final SharedPreferences.Editor editor) {
        editor.putString(Keys.AUTH_SERVICE_URL, authServiceUrl);
        editor.putString(Keys.AUTH_CLIENT_ID, authClientId);
        editor.putString(Keys.AUTH_CLIENT_SECRET, authClientSecret);
        editor.putString(Keys.AUTH_REDIRECT_URL, authRedirectUrl);
        editor.putString(Keys.DATA_SERVICE_URL,dataServiceUrl);
        editor.putString(Keys.PUSH_SERVICE_URL, pushServiceUrl);
        editor.putString(Keys.PUSH_GCM_SENDER_ID, pushGcmSenderId);
        editor.putString(Keys.PUSH_VARIANT_UUID, pushVariantUuid);
        editor.putString(Keys.PUSH_VARIANT_SECRET, pushVariantSecret);
        editor.putString(Keys.ROUTE_SERVICE_URL, routeServiceUrl);
    }
}
