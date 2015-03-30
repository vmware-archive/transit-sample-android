package io.pivotal.android.ttc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import io.pivotal.android.ttc.R;
import io.pivotal.android.ttc.models.Route;
import io.pivotal.android.ttc.models.RouteTitleModel;
import io.pivotal.android.ttc.util.RouteUtil;

public class RoutesAdapter extends ArrayAdapter<Route> {

    private final TextDrawable.IBuilder mTextDrawableBuilder;

    public RoutesAdapter(final Context context) {
        super(context, 0);

        mTextDrawableBuilder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .rect();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (convertView == null) {
            final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item_route, parent, false);
        }

        final Route route = getItem(position);

        RouteTitleModel routeTitle = RouteUtil.parseRouteTitle(route.title);

        final TextView stopView = (TextView) convertView.findViewById(R.id.route_name);
        stopView.setText(routeTitle.getRouteName());

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view);

        int imageColor;
        if (position % 2 == 0) {
            imageColor = getContext().getResources().getColor(R.color.ttc_red);
        } else {
            imageColor = getContext().getResources().getColor(R.color.ttc_blue);
        }

        TextDrawable textDrawable = mTextDrawableBuilder.build(routeTitle.getRouteNumber(), imageColor);
        imageView.setImageDrawable(textDrawable);

        return convertView;
    }
}
