package io.pivotal.android.ttc.activities;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import io.pivotal.android.ttc.R;
import io.pivotal.android.ttc.models.Route;
import io.pivotal.android.ttc.models.RouteParcel;
import io.pivotal.android.ttc.models.Stop;
import io.pivotal.android.ttc.models.StopParcel;

public class RoutesActivity extends Activity {

    private static interface Extras {
        public static final String ROUTE = "route";
        public static final String STOP = "stop";
    }

    public static Route getRoute(final Intent intent) {
        if (intent != null) {
            final RouteParcel parcel = intent.getParcelableExtra(Extras.ROUTE);
            if (parcel != null) {
                return parcel.getRoute();
            }
        }
        return null;
    }

    public static Stop getStop(final Intent intent) {
        if (intent != null) {
            final StopParcel parcel = intent.getParcelableExtra(Extras.STOP);
            if (parcel != null) {
                return parcel.getStop();
            }
        }
        return null;
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

    private void killCancelledInstance() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);
        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==  android.R.id.home) {
            killCancelledInstance();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Crouton.cancelAllCroutons();
    }
}
