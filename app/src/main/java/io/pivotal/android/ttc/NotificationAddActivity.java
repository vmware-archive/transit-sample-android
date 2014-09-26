package io.pivotal.android.ttc;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class NotificationAddActivity extends Activity {

    private static interface Extras {
        public static final String NOTIFICATION = "notification";
    }

    public static Notification getNotification(final Intent intent) {
        if (intent != null) {
            final NotificationParcel parcel = intent.getParcelableExtra(Extras.NOTIFICATION);
            if (parcel != null) {
                return parcel.getNotification();
            }
        }
        return null;
    }

    public static void newInstanceForResult(final Fragment fragment, final int requestCode) {
        final Activity activity = fragment.getActivity();
        final Intent intent = new Intent(activity, NotificationAddActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_add);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notifications_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.menu_done) {
            setResult();
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            killCancelledInstance();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void setResult() {
        final Notification notification = getNotification();
        if (notification != null) {
            setResult(RESULT_OK, createIntent(notification));
        }
        finish();
    }

    private Notification getNotification() {
        final int id = R.id.notification_add_fragment;
        final FragmentManager manager = getFragmentManager();
        final NotificationAddFragment fragment = (NotificationAddFragment) manager.findFragmentById(id);
        return fragment.getNotification();
    }

    private Intent createIntent(final Notification notification) {
        final NotificationParcel parcel = new NotificationParcel(notification);
        final Intent intent = new Intent();
        intent.putExtra(Extras.NOTIFICATION, parcel);
        return intent;
    }

    private void killCancelledInstance() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
