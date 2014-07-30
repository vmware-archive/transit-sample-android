package io.pivotal.android.ttc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import io.pivotal.android.data.activity.BaseAuthorizationActivity;

public class AuthorizationActivity extends BaseAuthorizationActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
    }

    @Override
    public void onAuthorizationComplete() {
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