package io.pivotal.android.ttc;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

public class TTCApi {

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
