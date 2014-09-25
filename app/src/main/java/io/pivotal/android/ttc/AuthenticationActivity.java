package io.pivotal.android.ttc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import io.pivotal.android.push.registration.UnregistrationListener;

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

    public void onLogOutClicked(final View view) {
        TTCApi.pushUnregister(this, new UnregistrationListener() {

            @Override
            public void onUnregistrationComplete() {
                // Does nothing
            }

            @Override
            public void onUnregistrationFailed(String s) {
                final String message = getString(R.string.unable_to_unregister_push) + ": " + s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(AuthenticationActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        TTCApi.dataLogout(this);
        Toast.makeText(this, getString(R.string.user_logged_out), Toast.LENGTH_SHORT).show();
    };
}
