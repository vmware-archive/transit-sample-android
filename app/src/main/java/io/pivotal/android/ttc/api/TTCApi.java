package io.pivotal.android.ttc.api;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Set;

import io.pivotal.android.data.DataStore;
import io.pivotal.android.data.DataStoreParameters;
import io.pivotal.android.push.Push;
import io.pivotal.android.push.RegistrationParameters;
import io.pivotal.android.push.registration.UnregistrationListener;
import io.pivotal.android.ttc.R;
import io.pivotal.android.ttc.auth.AuthenticationActivity;
import io.pivotal.android.ttc.route.Route;
import io.pivotal.android.ttc.stop.Stop;

public class TTCApi {

    // Dynamic configuration parameters
    public static final String CONFIGURATION_URL = "https://simulator.xlstudio.com/apis/126/transit/config";
    public static final String SIMULATOR_ACCESS_KEY = "b6ae2454a377687cdbf5970abb819bf8";

    // Data service parameters
    public static final String CLIENT_ID = "cd68e385-c0e8-4740-a563-748e643a2280";
    public static final String CLIENT_SECRET = "IaioD3Mcj4XU67ySMidiFDNrKwv68RB4Cft2zLrdJHoWcdqjsCSWf1U1EZDR6JKufpNp9NTcBqSxbR6bA95_eg";
    public static final String AUTHORIZATION_URL = "http://datasync-authentication.demo.vchs.cfms-apps.com";
    public static final String DATA_SERVICES_URL = "http://datasync-datastore.demo.vchs.cfms-apps.com";
    public static final String REDIRECT_URL = "io.pivotal.android.ttc://identity/oauth2callback";

    // Push service parameters
    public static final String GCM_SENDER_ID = "960682130245";
    public static final String VARIANT_UUID = "665d74d8-32b8-4521-92db-62f6979dbeea";
    public static final String VARIANT_SECRET = "96fe7aae-069f-4551-9e03-6aa77fc7c611";
    public static final String PUSH_BASE_SERVER_URL = "http://push-notifications.demo.vchs.cfms-apps.com";
    public static final String DEVICE_ALIAS = "ttc-app";

    // API gateway parameters
    public static final String API_GATEWAY_BASE_URL = "http://transit-gateway.demo.vchs.cfms-apps.com/ttc/routes";

    // Request timeout duration
    private static int TEN_SECONDS = 10000;

    public static void setupPush(final Context context, Set<String> tags) {
        Push.getInstance(context).startRegistration(getPushParameters(tags));
    }

    public static void pushUnregister(final Context context, UnregistrationListener listener) {
        Push.getInstance(context).startUnregistration(getPushParameters(null), listener);
    }

    private static RegistrationParameters getPushParameters(Set<String> tags) {
        return new RegistrationParameters(
                GCM_SENDER_ID, VARIANT_UUID, VARIANT_SECRET, DEVICE_ALIAS, PUSH_BASE_SERVER_URL, tags
        );
    }

    public static void setupData(final Context context) {
        DataStore.getInstance().initialize(context, new DataStoreParameters(
                CLIENT_ID, CLIENT_SECRET, AUTHORIZATION_URL, REDIRECT_URL, DATA_SERVICES_URL
        ));
    }

    public static void authenticate(Activity activity) {
        DataStore.getInstance().obtainAuthorization(activity);
    }

    public static void dataLogout(final Context context) {
        DataStore.getInstance().clearAuthorization(context);
    }

    public static void fullLogoutFromActivity(final Activity activity) {
        TTCApi.pushUnregister(activity, new UnregistrationListener() {

            @Override
            public void onUnregistrationComplete() {
                // Does nothing
            }

            @Override
            public void onUnregistrationFailed(String s) {
                final String message = activity.getString(R.string.unable_to_unregister_push) + ": " + s;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        Toast.makeText(activity, activity.getString(R.string.user_logged_out), Toast.LENGTH_SHORT).show();
        dataLogoutOnlyFromActivity(activity);
    }

    public static void dataLogoutOnlyFromActivity(final Activity activity) {
        TTCPreferences.setIsAuthenticated(activity, false);
        TTCApi.dataLogout(activity);
        AuthenticationActivity.newInstance(activity);
        activity.finish();
    }

    public static Route.List getRoutes() throws Exception {
        final HttpGet request = new HttpGet(API_GATEWAY_BASE_URL);
        return execute(request, new Gson(), Route.List.class);
    }

    public static Stop.List getStops(final String tag) throws Exception {
        final HttpGet request = new HttpGet(API_GATEWAY_BASE_URL + "/" + tag);
        final Stop.Response response = execute(request, new Gson(), Stop.Response.class);
        return response != null ? response.stops : null;
    }

    public static Configuration getConfiguration() throws Exception {
        final HttpGet request = new HttpGet(CONFIGURATION_URL);
        request.addHeader("X-SIMULATOR-ACCESS-KEY", SIMULATOR_ACCESS_KEY);
        final Configuration configuration = execute(request, new Gson(), Configuration.class);
        return configuration;
    }

    private static <T> T execute(final HttpUriRequest request, final Gson gson, final Type type) throws Exception {
        final DefaultHttpClient client = getClient();
        final HttpResponse response = client.execute(request);
        final InputStream inputStream = response.getEntity().getContent();
        final InputStreamReader inputReader = new InputStreamReader(inputStream);
        final JsonReader jsonReader = new JsonReader(inputReader);
        final T list = gson.fromJson(jsonReader, type);
        jsonReader.close();
        return list;
    }

    private static DefaultHttpClient getClient() {
        final DefaultHttpClient client = new DefaultHttpClient();
        final HttpParams params = client.getParams();
        HttpConnectionParams.setConnectionTimeout(params, TEN_SECONDS);
        HttpConnectionParams.setSoTimeout(params, TEN_SECONDS);
        return client;
    }
}
