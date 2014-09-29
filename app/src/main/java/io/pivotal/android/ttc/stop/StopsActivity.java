package io.pivotal.android.ttc.stop;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import io.pivotal.android.ttc.R;
import io.pivotal.android.ttc.route.RouteParcel;


public class StopsActivity extends Activity {

    private static interface Extras {
        public static final String ROUTE = "route";
        public static final String STOP = "stop";
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

    public static void newInstanceForResult(final Fragment fragment, final int requestCode, final RouteParcel parcel) {
        final Activity activity = fragment.getActivity();
        final Intent intent = new Intent(activity, StopsActivity.class);
        intent.putExtra(Extras.ROUTE, parcel);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void killInstanceWithResult(final Fragment fragment, final Stop stop, final Intent intent) {
        final Activity activity = fragment.getActivity();
        if (activity instanceof StopsActivity) {
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
        setContentView(R.layout.activity_stops);

        final RouteParcel parcel = getIntent().getParcelableExtra(Extras.ROUTE);
        if (parcel == null) {
            Toast.makeText(this, "Route cannot be null.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        passRouteToFragment(parcel);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void passRouteToFragment(final RouteParcel parcel) {
        final int id = R.id.stops_fragment;
        final FragmentManager manager = getFragmentManager();
        final StopsFragment fragment = (StopsFragment) manager.findFragmentById(id);
        fragment.setRoute(parcel.getRoute());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId( )== android.R.id.home) {
            killCancelledInstance();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
