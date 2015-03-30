package io.pivotal.android.ttc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import io.pivotal.android.ttc.R;
import io.pivotal.android.ttc.models.Route;

public class RoutesAdapter extends ArrayAdapter<Route> {

    private final TextDrawable.IBuilder mTextDrawableBuilder;
    private final ColorGenerator mGenerator;

    public RoutesAdapter(final Context context) {
        super(context, 0);

        mTextDrawableBuilder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .rect();

        mGenerator = ColorGenerator.MATERIAL;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if (convertView == null) {
            final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_item_route, parent, false);
        }

        final Route route = getItem(position);


        int delimiterIndex = route.title.indexOf("-");
        String routeNumber = "";
        String routeName = route.title;
        if (delimiterIndex != -1) {
            routeNumber = route.title.substring(0, delimiterIndex);
            routeName = route.title.substring(delimiterIndex + 1);
        }

        final TextView stopView = (TextView) convertView.findViewById(R.id.route_name);
        stopView.setText(routeName);

        final ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view);
        TextDrawable textDrawable = mTextDrawableBuilder.build(routeNumber, mGenerator.getRandomColor());
        imageView.setImageDrawable(textDrawable);


        return convertView;
    }
}
