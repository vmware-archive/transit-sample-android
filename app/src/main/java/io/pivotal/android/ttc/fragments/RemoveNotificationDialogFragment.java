package io.pivotal.android.ttc.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import io.pivotal.android.ttc.R;

public class RemoveNotificationDialogFragment extends DialogFragment {

    public static final CharSequence[] items = new CharSequence[] {"Remove Notification", "Cancel"};
    public static final int REMOVE_NOTIFICATION = 0;
    public static final int CANCELLED = 1;
    private Listener listener;

    public interface Listener {
        void onClickResult(int result);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.notification);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onClickResult(which);
                }
            }
        });
        return builder.create();
    }
}
