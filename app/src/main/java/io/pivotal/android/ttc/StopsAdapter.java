package io.pivotal.android.ttc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StopsAdapter extends ArrayAdapter<Stop> {

    public StopsAdapter(final Context context) {
        super(context, 0);
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (convertView == null) {
            final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item_stop, parent, false);
        }

        final Stop stop = getItem(position);

        final TextView tagView = (TextView) convertView.findViewById(R.id.stop_tag);
        tagView.setText(stop.tag);

        final TextView titleView = (TextView) convertView.findViewById(R.id.stop_name);
        titleView.setText(stop.title);

        final TextView idView = (TextView) convertView.findViewById(R.id.stop_id);
        idView.setText(stop.stopId);

        return convertView;
    }
}
