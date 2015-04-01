package io.pivotal.android.ttc.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;

import java.io.InputStream;
import java.util.Properties;

import io.pivotal.android.ttc.BuildConfig;
import io.pivotal.android.ttc.R;

public class AboutDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.app_name);
        builder.setMessage(getMessage());
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();
    }

    private String getMessage() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Application version: ");
        sb.append(BuildConfig.VERSION_NAME);
        sb.append("\n\nPivotal CF Mobile Services\ncomponent versions:\n\n  ");
        sb.append(BuildConfig.AUTH_SDK_VERSION);
        sb.append("\n  ");
        sb.append(BuildConfig.DATA_SDK_VERSION);
        sb.append("\n  ");
        sb.append(BuildConfig.PUSH_SDK_VERSION);

        try {
            Resources resources = this.getResources();
            AssetManager assetManager = resources.getAssets();
            Properties props = new Properties();
            InputStream inputStream = assetManager.open("pivotal.properties");
            props.load(inputStream);

            String tokenUrl = props.getProperty("pivotal.push.serviceUrl");
            String authUrl = props.getProperty("pivotal.auth.authorizeUrl");
            String serviceUrl = props.getProperty("pivotal.data.serviceUrl");
            sb.append("\n\nPush Url: \n ");
            sb.append(tokenUrl);
            sb.append("\n\nAuthorize Url: \n ");
            sb.append(authUrl);
            sb.append("\n\nData Service Url: \n ");
            sb.append(serviceUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
