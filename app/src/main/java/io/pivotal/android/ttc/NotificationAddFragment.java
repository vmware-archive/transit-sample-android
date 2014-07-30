package io.pivotal.android.ttc;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

public class NotificationAddFragment extends Fragment {

    private static interface RequestCode {
        public static final int REQUEST_ROUTE = 0;
    }

    private TimePicker mPickerView;
    private TextView mTextView;

    private Route mRoute;
    private Stop mStop;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notification_add, container, false);
        mPickerView = (TimePicker) view.findViewById(R.id.notification_add_time);
        mTextView = (TextView) view.findViewById(R.id.notification_add_stop);
        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Fragment instance = NotificationAddFragment.this;
                RoutesActivity.newInstanceForResult(instance, RequestCode.REQUEST_ROUTE);
            }
        });
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestCode.REQUEST_ROUTE && resultCode == Activity.RESULT_OK) {
            mRoute = RoutesActivity.getRoute(data).getRoute();
            mStop = RoutesActivity.getStop(data).getStop();

            mTextView.setText(mRoute.title + "\n\n" + mStop.title);
        }
    }

    public Notification getNotification() {
        if (mRoute != null && mStop != null) {
            final Integer hour = mPickerView.getCurrentHour();
            final Integer minute = mPickerView.getCurrentMinute();

            final Notification notification = new Notification();
            notification.time = String.format("%02d%02d", hour, minute);
            notification.route = mRoute.title;
            notification.stop = mStop.title;
            notification.enabled = true;
            return notification;
        } else {
            return null;
        }
    }

}
