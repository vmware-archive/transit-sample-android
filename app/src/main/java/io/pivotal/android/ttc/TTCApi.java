package io.pivotal.android.ttc;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Set;

import io.pivotal.android.data.DataStore;
import io.pivotal.android.data.DataStoreParameters;
import io.pivotal.android.push.Push;
import io.pivotal.android.push.RegistrationParameters;
import io.pivotal.android.push.registration.UnregistrationListener;

public class TTCApi {

    public static final String CLIENT_ID = "cd68e385-c0e8-4740-a563-748e643a2280";
    public static final String CLIENT_SECRET = "IaioD3Mcj4XU67ySMidiFDNrKwv68RB4Cft2zLrdJHoWcdqjsCSWf1U1EZDR6JKufpNp9NTcBqSxbR6bA95_eg";
    public static final String AUTHORIZATION_URL = "http://datasync-authentication.demo.vchs.cfms-apps.com";
    public static final String REDIRECT_URL = "io.pivotal.android.ttc://identity/oauth2callback";
    public static final String DATA_SERVICES_URL = "http://datasync-datastore.demo.vchs.cfms-apps.com";

    public static final String GCM_SENDER_ID = "960682130245";
    public static final String VARIANT_UUID = "665d74d8-32b8-4521-92db-62f6979dbeea";
    public static final String VARIANT_SECRET = "96fe7aae-069f-4551-9e03-6aa77fc7c611";
    public static final String PUSH_BASE_SERVER_URL = "http://push-notifications.demo.vchs.cfms-apps.com";
    public static final String DEVICE_ALIAS = "ttc-app";

    public static final String API_GATEWAY_BASE_URL = "http://transit-gateway.demo.vchs.cfms-apps.com/ttc/routes";


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

    public static void dataLogout(final Context context) {
        DataStore.getInstance().clearAuthorization(context);
    }

    public static void authenticate(Activity activity) {
        DataStore.getInstance().obtainAuthorization(activity);
    }

    public static void logout(final Activity activity) {
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
        TTCPreferences.setIsAuthenticated(activity, false);
        TTCApi.dataLogout(activity);
        Toast.makeText(activity, activity.getString(R.string.user_logged_out), Toast.LENGTH_SHORT).show();
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

    private static <T> T execute(final HttpUriRequest request, final Gson gson, final Type type) throws Exception {
        final HttpResponse response = new DefaultHttpClient().execute(request);
        final InputStream inputStream = response.getEntity().getContent();
        final InputStreamReader inputReader = new InputStreamReader(inputStream);
        final JsonReader jsonReader = new JsonReader(inputReader);
        final T list = gson.fromJson(jsonReader, type);
        jsonReader.close();
        return list;
    }
}
