package io.pivotal.android.ttc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AuthenticationActivity extends Activity {

    public static void newInstance(final Activity activity) {
        final Intent intent = new Intent(activity, AuthenticationActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        TTCApi.setupData(this);
    }

    public void onAuthenticateClicked(final View view) {
        TTCApi.authenticate(this);
    }
}
