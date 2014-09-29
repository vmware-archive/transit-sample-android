package io.pivotal.android.ttc.notification.add;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import io.pivotal.android.ttc.notification.Notification;
import io.pivotal.android.ttc.R;
import io.pivotal.android.ttc.util.TagUtil;
import io.pivotal.android.ttc.route.Route;
import io.pivotal.android.ttc.route.RouteParcel;
import io.pivotal.android.ttc.route.RoutesActivity;
import io.pivotal.android.ttc.stop.Stop;
import io.pivotal.android.ttc.stop.StopParcel;

public class NotificationAddFragment extends Fragment {

    public static final String ROUTE_KEY = "ROUTE";
    public static final String STOP_KEY = "STOP";

    private static interface RequestCode {
        public static final int REQUEST_ROUTE = 0;
    }

    private TimePicker mTimePickerView;
    private TextView mRouteAndStopTextView;

    private Route mRoute;
    private Stop mStop;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notification_add, container, false);
        mTimePickerView = (TimePicker) view.findViewById(R.id.notification_add_time);
        mRouteAndStopTextView = (TextView) view.findViewById(R.id.notification_add_stop);
        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
        return view;
    }

    private void restoreState(Bundle bundle) {
        if (bundle.containsKey(ROUTE_KEY)) {
            mRoute = ((RouteParcel)bundle.getParcelable(ROUTE_KEY)).getRoute();
        }
        if (bundle.containsKey(STOP_KEY)) {
            mStop = ((StopParcel)bundle.getParcelable(STOP_KEY)).getStop();
        }
        showRouteAndStop();
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRouteAndStopTextView.setOnClickListener(new View.OnClickListener() {
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
            mRoute = RoutesActivity.getRoute(data);
            mStop = RoutesActivity.getStop(data);
            showRouteAndStop();
        }
    }

    private void showRouteAndStop() {
        if (mRoute != null && mStop != null) {
            mRouteAndStopTextView.setText(mRoute.title + "\n\n" + mStop.title);
        } else {
            mRouteAndStopTextView.setText(R.string.select_a_stop);
        }
    }

    public Notification getNotification() {
        if (mRoute != null && mStop != null) {
            final int hour = mTimePickerView.getCurrentHour();
            final int minute = mTimePickerView.getCurrentMinute();
            return createNotification(hour, minute);
        } else {
            return null;
        }
    }

    private Notification createNotification(final int hour, final int minute) {
        final Notification notification = new Notification();
        notification.tag = TagUtil.getTag(hour, minute, mRoute, mStop);
        notification.time = getTimeFormat(hour, minute);
        notification.route = mRoute.title;
        notification.stop = mStop.title;
        notification.enabled = true;
        return notification;
    }

    private String getTimeFormat(final int hour, final int minute) {
        if (hour == 0) {
            return String.format("12:%02d AM", minute);
        } else if (hour > 12) {
            return String.format("%d:%02d PM", hour - 12, minute);
        } else {
            return String.format("%d:%02d AM", hour, minute);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRoute != null) {
            outState.putParcelable(ROUTE_KEY, new RouteParcel(mRoute));
        }
        if (mStop != null) {
            outState.putParcelable(STOP_KEY, new StopParcel(mStop));
        }
    }
}
