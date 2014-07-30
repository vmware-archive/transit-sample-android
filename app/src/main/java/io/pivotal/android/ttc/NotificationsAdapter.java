package io.pivotal.android.ttc;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class NotificationsAdapter extends RemoteAdapter<Notification> {

    public NotificationsAdapter(final Context context) {
        super(context);
    }

    protected Type getListType() {
        return new TypeToken<ArrayList<Notification>>(){}.getType();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (convertView == null) {
            final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item_notification, parent, false);
        }

        final Notification notification = getItem(position);

        Log.i("ttc", "Notification: " + notification);

        final TextView routeView = (TextView) convertView.findViewById(R.id.notification_route);
        routeView.setText(notification.route);

        final TextView stopView = (TextView) convertView.findViewById(R.id.notification_stop);
        stopView.setText(notification.stop);

        final TextView timeView = (TextView) convertView.findViewById(R.id.notification_time);
        timeView.setText(notification.time);

        final Switch enabledView = (Switch) convertView.findViewById(R.id.notification_enabled);
        enabledView.setChecked(notification.enabled);
        enabledView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final boolean isChecked = enabledView.isChecked();
                Log.v("ttc", "onCheckedChanged: " + isChecked);
                notification.enabled = isChecked;
                sync();
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                Log.v("ttc", "onLongClick");
                removeItem(notification);
                return true;
            }
        });

        return convertView;
    }
}