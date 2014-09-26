package io.pivotal.android.ttc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RoutesAdapter extends ArrayAdapter<Route> {

    public RoutesAdapter(final Context context) {
        super(context, 0);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (convertView == null) {
            final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item_route, parent, false);
        }

        final Route route = getItem(position);

        final TextView stopView = (TextView) convertView.findViewById(R.id.route_name);
        stopView.setText(route.title);

        return convertView;
    }
}
