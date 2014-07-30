package io.pivotal.android.ttc;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

public class NotificationsFragment extends Fragment {

    private static interface RequestCode {
        public static final int REQUEST_NOTIFICATION = 0;
    }

    private NotificationsAdapter mAdapter;
    private AbsListView mAdapterView;
    private ProgressBar mProgressBar;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        mAdapterView = (AbsListView) view.findViewById(android.R.id.list);
        mProgressBar = (ProgressBar) view.findViewById(android.R.id.progress);
        return view;
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        mAdapter = new NotificationsAdapter(getActivity());
        mAdapterView.setAdapter(mAdapter);

        mAdapter.refresh();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.notifications_add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            final int requestCode = RequestCode.REQUEST_NOTIFICATION;
            NotificationAddActivity.newInstanceForResult(this, requestCode);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestCode.REQUEST_NOTIFICATION && resultCode == Activity.RESULT_OK) {
            final Notification notification = NotificationAddActivity.getNotification(data);
            mAdapter.addItem(notification);
        }
    }

}
