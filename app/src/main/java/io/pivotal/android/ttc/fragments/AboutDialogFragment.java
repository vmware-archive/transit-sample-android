package io.pivotal.android.ttc.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

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
        return sb.toString();
    }
}
