package io.pivotal.android.ttc.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import io.pivotal.android.data.activity.BaseAuthorizationActivity;
import io.pivotal.android.ttc.R;
import io.pivotal.android.ttc.api.TTCPreferences;
import io.pivotal.android.ttc.notification.list.NotificationsActivity;

public class AuthorizationActivity extends BaseAuthorizationActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
    }

    @Override
    public void onAuthorizationComplete() {
        TTCPreferences.setIsAuthenticated(this, true);
        final Intent intent = new Intent(this, NotificationsActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onAuthorizationDenied() {
        Toast.makeText(this, "Authorization Denied", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthorizationFailed(final String reason) {
        Toast.makeText(this, "Authorization Failed", Toast.LENGTH_SHORT).show();
    }

}