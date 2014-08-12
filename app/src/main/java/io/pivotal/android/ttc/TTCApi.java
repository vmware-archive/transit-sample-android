package io.pivotal.android.ttc;

import android.app.Activity;
import android.content.Context;

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

public class TTCApi {

    public static final String CLIENT_ID = "6006fa24-2757-481d-b894-f79ed8037e1f";
    public static final String CLIENT_SECRET = "C8eJhboAHq_h-oP6po5MoRWQsjWATFDZM8dqbKXBZ8RdeMvv_faF88DVBAp6OsAozU9brBqhYt0RTwpZABYRIQ";
    public static final String AUTHORIZATION_URL = "http://datasync-authentication.kona.coffee.cfms-apps.com";
    public static final String REDIRECT_URL = "io.pivotal.android.ttc://identity/oauth2callback";
    public static final String DATA_SERVICES_URL = "http://datasync-datastore.kona.coffee.cfms-apps.com";

    public static final String GCM_SENDER_ID = "960682130245";
    public static final String VARIANT_UUID = "665d74d8-32b8-4521-92db-62f6979dbeea";
    public static final String VARIANT_SECRET = "96fe7aae-069f-4551-9e03-6aa77fc7c611";
    public static final String PUSH_BASE_SERVER_URL = "http://cfms-push-service-dev.main.vchs.cfms-apps.com";
    public static final String DEVICE_ALIAS = "push-demo-alias";


    public static void setupPush(final Context context, Set<String> tags) {
        Push.getInstance(context).startRegistration(new RegistrationParameters(
                GCM_SENDER_ID, VARIANT_UUID, VARIANT_SECRET, DEVICE_ALIAS, PUSH_BASE_SERVER_URL, tags
        ));
    }

    public static void setupData(final Context context) {
        DataStore.getInstance().initialize(context, new DataStoreParameters(
                CLIENT_ID, CLIENT_SECRET, AUTHORIZATION_URL, REDIRECT_URL, DATA_SERVICES_URL
        ));
    }

    public static void authenticate(Activity activity) {
        DataStore.getInstance().obtainAuthorization(activity);
    }

    public static Route.List getRoutes() throws Exception {
        final HttpGet request = new HttpGet("http://nextbus.one.pepsi.cf-app.com/ttc/routes");
        return execute(request, new Gson(), Route.List.class);
    }

    public static Stop.List getStops(final String tag) throws Exception {
        final HttpGet request = new HttpGet("http://nextbus.one.pepsi.cf-app.com/ttc/routes/" + tag);
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
