package io.pivotal.android.ttc;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;


public class RoutesActivity extends Activity {

    private static interface Extras {
        public static final String ROUTE = "route";
        public static final String STOP = "stop";
    }

    public static Route getRoute(final Intent intent) {
        final RouteParcel parcel = intent.getParcelableExtra(Extras.ROUTE);
        return parcel.getRoute();
    }

    public static Stop getStop(final Intent intent) {
        final StopParcel parcel = intent.getParcelableExtra(Extras.STOP);
        return parcel.getStop();
    }

    public static void newInstanceForResult(final Fragment fragment, final int requestCode) {
        final Activity activity = fragment.getActivity();
        final Intent intent = new Intent(activity, RoutesActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void killInstanceWithResult(final Fragment fragment, final Route route, final Stop stop) {
        final Activity activity = fragment.getActivity();
        if (activity instanceof  RoutesActivity) {
            final Intent intent = new Intent();
            intent.putExtra(Extras.ROUTE, new RouteParcel(route));
            intent.putExtra(Extras.STOP, new StopParcel(stop));
            activity.setResult(RESULT_OK, intent);
            activity.finish();
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
    }
}
