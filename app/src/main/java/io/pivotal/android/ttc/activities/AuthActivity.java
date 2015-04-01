package io.pivotal.android.ttc.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import io.pivotal.android.auth.LoginActivity;
import io.pivotal.android.data.Logger;
import io.pivotal.android.ttc.R;

public class AuthActivity extends LoginActivity {

    private View mLoginFlowView;
    private View mPasswordFlowView;
    private View mAuthCodeFlowView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mLoginFlowView = findViewById(R.id.login_flow_buttons);
        mPasswordFlowView = findViewById(R.id.login_grant_type_password);
        mAuthCodeFlowView = findViewById(R.id.login_grant_type_auth_code);

    }

    protected String getUserName() {
        final EditText editText = (EditText) findViewById(R.id.login_user_name);
        final String username = editText.getText().toString();
        return !TextUtils.isEmpty(username) ? username : getString(R.string.auth_account);
    }

    @Override
    public void onBackPressed() {
        if (mLoginFlowView.getVisibility() == View.GONE) {
            mLoginFlowView.setVisibility(View.VISIBLE);
            mPasswordFlowView.setVisibility(View.GONE);
            mAuthCodeFlowView.setVisibility(View.GONE);
        }
    }

    // ================= PASSWORD FLOW =================


    public void onPasswordFlowClicked(final View view) {
        mLoginFlowView.setVisibility(View.GONE);
        mPasswordFlowView.setVisibility(View.VISIBLE);
        mAuthCodeFlowView.setVisibility(View.GONE);
    }

    protected String getPassword() {
        final EditText editText = (EditText) findViewById(R.id.login_password);
        return editText.getText().toString();
    }

    public void onStartLoading() {
        final Button button = (Button) findViewById(R.id.password_login_submit);
        button.setText(getString(R.string.auth_loading));
        button.setEnabled(false);
    }

    public void onPasswordLoginClicked(final View view) {
        onStartLoading();

        final String username = getUserName();
        final String password = getPassword();

        fetchTokenWithPasswordGrantType(username, password);
    }

    @Override
    public void onAuthorizationFailed(final Error error) {
        Toast.makeText(this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

        final Button button = (Button) findViewById(R.id.password_login_submit);
        button.setText("Submit");
        button.setEnabled(true);
    }

    // =================================================



    // ================= AUTH_CODE FLOW ================


    public void onAuthCodeFlowClicked(final View view) {
        mLoginFlowView.setVisibility(View.GONE);
        mPasswordFlowView.setVisibility(View.GONE);
        mAuthCodeFlowView.setVisibility(View.VISIBLE);

        final WebView webView = (WebView) findViewById(R.id.login_web_view);

        fetchTokenWithAuthCodeGrantType(webView);
    }

    @Override
    protected boolean handleRedirectUrl(final WebView webView, final String url) {
        Logger.v("URL: " + url);
        final boolean handled = super.handleRedirectUrl(webView, url);
        if (handled) {
            webView.setVisibility(View.GONE);
        }
        return handled;
    }

    // =================================================

}