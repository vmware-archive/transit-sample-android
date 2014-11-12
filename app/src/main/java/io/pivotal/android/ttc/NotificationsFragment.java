package io.pivotal.android.ttc;

import android.animation.Animator;
import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class NotificationsFragment extends Fragment {

    private BroadcastReceiver receiver;

    private static interface RequestCode {
        public static final int REQUEST_NOTIFICATION = 0;
    }

    private NotificationsAdapter mAdapter;
    private AbsListView mAdapterView;
    private View mEmptyNotificationsView;
    private View mLastNotificationsView;
    private TextView mLastNotificationsLabelView;
    private TextView mLastNotificationsTextView;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("h:mm aa");

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        mAdapterView = (AbsListView) view.findViewById(android.R.id.list);
        mEmptyNotificationsView = view.findViewById(R.id.empty_notification_instructions_box);
        mLastNotificationsView = view.findViewById(R.id.last_notification_box);
        mLastNotificationsTextView = (TextView) view.findViewById(R.id.last_notification);
        mLastNotificationsLabelView = (TextView) view.findViewById(R.id.last_notification_label);
        mAdapterView.setVisibility(View.GONE);
        mEmptyNotificationsView.setVisibility(View.GONE);
        mLastNotificationsView.setVisibility(View.GONE);
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
            public void onItemLongTouched(final Notification notification) {

                final RemoveNotificationDialogFragment.Listener listener = new RemoveNotificationDialogFragment.Listener() {
                    @Override
                    public void onClickResult(int result) {
                        if (result == RemoveNotificationDialogFragment.REMOVE_NOTIFICATION) {
                            mAdapter.removeItem(notification);
                        }
                    }
                };
                final RemoveNotificationDialogFragment dialog = new RemoveNotificationDialogFragment();
                dialog.setListener(listener);
                dialog.show(getFragmentManager(), "RemoveNotificationDialogFragment");
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

    @Override
    public void onResume() {
        super.onResume();

        final IntentFilter filter = new IntentFilter(TTCPushService.NOTIFICATION_RECEIVED);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final String lastNotificationText = TTCPreferences.getLastNotificationText(getActivity());
                        final long lastNotificationTime = TTCPreferences.getLastNotificationTime(getActivity());
                        showLastNotification(lastNotificationText, lastNotificationTime);
                        if (lastNotificationText != null && !lastNotificationText.isEmpty()) {
                            jiggleView(mLastNotificationsView);
                        }
                    }
                });
            }
        };

        getActivity().registerReceiver(receiver, filter);
        showLastNotification(TTCPreferences.getLastNotificationText(getActivity()), TTCPreferences.getLastNotificationTime(getActivity()));
    }

    @Override
    public void onPause() {
        super.onPause();

        mLastNotificationsView.animate().cancel();
        mLastNotificationsView.setAlpha(1f);

        if (receiver != null && getActivity() != null) {
            getActivity().unregisterReceiver(receiver);
        }
        receiver = null;
    }

    private void showEmptyScreen() {
        mEmptyNotificationsView.setVisibility(View.VISIBLE);
        mAdapterView.setVisibility(View.GONE);
    }

    private void showDataScreen() {
        mEmptyNotificationsView.setVisibility(View.GONE);
        mAdapterView.setVisibility(View.VISIBLE);
    }

    private void showLastNotification(String lastNotificationText, long lastNotificationTime) {

        final String label = getResources().getString(R.string.last_notification_label);

        if (lastNotificationText != null && !lastNotificationText.isEmpty()) {

            mLastNotificationsView.setVisibility(View.VISIBLE);
            mLastNotificationsTextView.setText(lastNotificationText);
            mLastNotificationsLabelView.setText(label + " " + mDateFormat.format(new Date(lastNotificationTime)));
            mLastNotificationsView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    final RemoveNotificationDialogFragment.Listener listener = new RemoveNotificationDialogFragment.Listener() {
                        @Override
                        public void onClickResult(int result) {
                            if (result == RemoveNotificationDialogFragment.REMOVE_NOTIFICATION) {
                                showLastNotification(null, 0);
                            }
                        }
                    };
                    final RemoveNotificationDialogFragment dialog = new RemoveNotificationDialogFragment();
                    dialog.setListener(listener);
                    dialog.show(getFragmentManager(), "RemoveNotificationDialogFragment");
                    return true;
                }
            });

        } else {
            mLastNotificationsView.animate().cancel();
            mLastNotificationsView.setVisibility(View.GONE);
            mLastNotificationsTextView.setText("");
            mLastNotificationsLabelView.setText(label);
        }
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.notifications, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_add:
                addNotification();
                return true;

            case R.id.menu_about:
                showAboutDialog();
                return true;

            case R.id.menu_logout:
                logout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addNotification() {
        final int requestCode = RequestCode.REQUEST_NOTIFICATION;
        NotificationAddActivity.newInstanceForResult(this, requestCode);
    }

    private void showAboutDialog() {
        final AboutDialogFragment dialog = new AboutDialogFragment();
        dialog.show(getFragmentManager(), "AboutDialogFragment");
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
                if (!notificationAlreadyExists(notification)) {
                    mAdapter.addItem(notification);
                } else {
                    Crouton.makeText(getActivity(), R.string.stop_already_exists, Style.INFO).show();
                }
            }
        }
    }

    private boolean notificationAlreadyExists(Notification notification) {
        final List<Notification> existingNotifications = mAdapter.getItems();
        for (final Notification existingNotification : existingNotifications) {
            if (existingNotification.equals(notification)) {
                return true;
            }
        }
        return false;
    }

    private void jiggleView(View view) {

        if (ViewJiggler.remainingJiggleJobs <= 0) {
            ViewJiggler.remainingJiggleJobs = 1;
            new ViewJiggler(view).jiggle();
        } else {
            ViewJiggler.remainingJiggleJobs +=  1;
        }
    }

    private static class ViewJiggler {

        public static int remainingJiggleJobs = 0;

        private final View view;
        private float[] alphaAmounts = new float[] {0, 1, 0, 1, 0, 1};
        public int currentJiggleIteration = 0;

        public ViewJiggler(View view) {
            this.view = view;
        }

        public void jiggle() {

            final float alphaAmount = alphaAmounts[currentJiggleIteration];
            view.animate().
                    alpha(alphaAmount).
                    setDuration(150L).
                    setListener(
                        new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                currentJiggleIteration += 1;
                                if (currentJiggleIteration < alphaAmounts.length) {
                                    jiggle();
                                } else if (remainingJiggleJobs > 0) {
                                    remainingJiggleJobs -= 1;
                                    currentJiggleIteration = 0;
                                    jiggle();
                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {
                            }
                        }
                    );
        }
    }
}
