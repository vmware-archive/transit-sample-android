package io.pivotal.android.ttc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import io.pivotal.android.data.DataStore;
import io.pivotal.android.data.DataStoreParameters;
import io.pivotal.android.push.Push;
import io.pivotal.android.push.RegistrationParameters;

public class AuthenticationActivity extends Activity {

    private static final String CLIENT_ID = "PushSDKDemoApp";
    private static final String CLIENT_SECRET = "secret";
    private static final String AUTHORIZATION_URL = "http://ident.one.pepsi.cf-app.com/oauth/authorize";
    private static final String TOKEN_URL = "http://ident.one.pepsi.cf-app.com/token";
    private static final String REDIRECT_URL = "io.pivotal.android.ttc://identity/oauth2callback";
    private static final String DATA_SERVICES_URL = "http://data-service.one.pepsi.cf-app.com";

    private static final String GCM_SENDER_ID = "960682130245";
    private static final String VARIANT_UUID = "665d74d8-32b8-4521-92db-62f6979dbeea";
    private static final String VARIANT_SECRET = "96fe7aae-069f-4551-9e03-6aa77fc7c611";
    private static final String PUSH_BASE_SERVER_URL = "http://cfms-push-service-dev.main.vchs.cfms-apps.com";
    private static final String DEVICE_ALIAS = "push-demo-alias";


    public static void newInstance(final Activity activity) {
        final Intent intent = new Intent(activity, AuthenticationActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        DataStore.getInstance().setParameters(this, new DataStoreParameters(
            CLIENT_ID, CLIENT_SECRET, AUTHORIZATION_URL, TOKEN_URL, REDIRECT_URL, DATA_SERVICES_URL
        ));

        Push.getInstance(this).startRegistration(new RegistrationParameters(
            GCM_SENDER_ID, VARIANT_UUID, VARIANT_SECRET, DEVICE_ALIAS, PUSH_BASE_SERVER_URL
        ));
    }

    public void onAuthenticateClicked(final View view) {
        DataStore.getInstance().obtainAuthorization(this);
    }
}
