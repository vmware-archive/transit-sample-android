package io.pivotal.android.ttc.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.pivotal.android.ttc.Const;
import io.pivotal.android.ttc.R;
import io.pivotal.android.ttc.TTCApi;
import io.pivotal.android.ttc.models.Notification;
import io.pivotal.android.ttc.models.RouteTitleModel;
import io.pivotal.android.ttc.util.RouteUtil;

public abstract class NotificationsAdapter extends RemoteAdapter<Notification> {

    public NotificationsAdapter(final Context context) {
        super(context);

        mTextDrawableBuilder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .rect();
    }

    protected Type getListType() {
        return new TypeToken<ArrayList<Notification>>(){}.getType();
    }

    public abstract void onItemLongTouched(Notification notification);

    protected final TextDrawable.IBuilder mTextDrawableBuilder;

    @Override
    public void onItemsChanged() {
        final Set<String> tags = getTagsFromNotifications();

        Log.v(Const.TAG, "setupPush: " + tags);
        TTCApi.setupPush(getContext(), tags);
    }

    private Set<String> getTagsFromNotifications() {
        final Set<String> tags = new HashSet<>();
        final List<Notification> notifications = getItems();
        for (final Notification notification : notifications) {
            if (notification.enabled) {
                tags.add(notification.tag);
            }
        }
        return tags;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (convertView == null) {
            final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item_notification, parent, false);
        }

        final Notification notification = getItem(position);

        Log.i(Const.TAG, "Notification: " + notification);

        RouteTitleModel routeTitle = RouteUtil.parseRouteTitle(notification.route);

        int imageColor;
        if (position % 2 == 0) {
            imageColor = getContext().getResources().getColor(R.color.ttc_red);
        } else {
            imageColor = getContext().getResources().getColor(R.color.ttc_blue);
        }

        final ImageView routeNumberView = (ImageView) convertView.findViewById(R.id.notification_route_number);
        TextDrawable textDrawable = mTextDrawableBuilder.build(routeTitle.getRouteNumber(), imageColor);
        routeNumberView.setImageDrawable(textDrawable);

        final TextView routeView = (TextView) convertView.findViewById(R.id.notification_route);
        routeView.setText(routeTitle.getRouteName());

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
                Log.v(Const.TAG, "onCheckedChanged: " + isChecked);
                notification.enabled = isChecked;
                sync();
            }
        });

        convertView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                Log.v(Const.TAG, "onLongClick");
                onItemLongTouched(notification);
                return true;
            }
        });

        return convertView;
    }
}