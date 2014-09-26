package io.pivotal.android.ttc;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class NotificationsFragment extends Fragment {

    private static interface RequestCode {
        public static final int REQUEST_NOTIFICATION = 0;
    }

    private NotificationsAdapter mAdapter;
    private AbsListView mAdapterView;
    private View mEmptyNotificationsView;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        mAdapterView = (AbsListView) view.findViewById(android.R.id.list);
        mEmptyNotificationsView = view.findViewById(R.id.empty_notification_instructions_box);
        mAdapterView.setVisibility(View.GONE);
        mEmptyNotificationsView.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        final RemoteAdapter.UnauthorizedListener unauthorizedListener = new RemoteAdapter.UnauthorizedListener() {

            @Override
            public void onUnauthorized() {
                // The user's access token must have expired so log the user out of the data SDK.
                // Note that we want push notifications to remain registered so that the user
                // will still know when their stops are imminent.
                TTCApi.dataLogoutOnlyFromActivity(getActivity());
            }
        };

        mAdapter = new NotificationsAdapter(getActivity(), unauthorizedListener) {

            @Override
            public void refresh() {
                super.refresh();
                Crouton.makeText(getActivity(), "Loading...", Style.INFO).show();
            }

            @Override
            public void sync() {
                super.sync();
                Crouton.makeText(getActivity(), "Loading...", Style.INFO).show();
            }

            @Override
            public void onItemsChanged() {
                Crouton.cancelAllCroutons();
                super.onItemsChanged();
                if (getCount() <= 0) {
                    showEmptyScreen();
                } else {
                    showDataScreen();
                }
                Log.v(Const.TAG, "Loaded " + super.getCount() + " items.");
            }
        };

        mAdapterView.setAdapter(mAdapter);

        mAdapter.refresh();
    }

    private void showEmptyScreen() {
        mEmptyNotificationsView.setVisibility(View.VISIBLE);
        mAdapterView.setVisibility(View.GONE);
    }

    private void showDataScreen() {
        mEmptyNotificationsView.setVisibility(View.GONE);
        mAdapterView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.notifications, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            addNotification();
            return true;
        } else if (item.getItemId() == R.id.menu_logout) {
            logout();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void addNotification() {
        final int requestCode = RequestCode.REQUEST_NOTIFICATION;
        NotificationAddActivity.newInstanceForResult(this, requestCode);
    }

    private void logout() {
        TTCApi.fullLogoutFromActivity(getActivity());
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestCode.REQUEST_NOTIFICATION && resultCode == Activity.RESULT_OK) {
            final Notification notification = NotificationAddActivity.getNotification(data);
            if (notification != null) {
                mAdapter.addItem(notification);
            }
        }
    }
}
