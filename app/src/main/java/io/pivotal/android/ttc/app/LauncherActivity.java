package io.pivotal.android.ttc.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;

import io.pivotal.android.ttc.R;
import io.pivotal.android.ttc.api.Configuration;
import io.pivotal.android.ttc.api.TTCApi;
import io.pivotal.android.ttc.api.TTCPreferences;
import io.pivotal.android.ttc.auth.AuthenticationActivity;
import io.pivotal.android.ttc.notification.list.NotificationsActivity;
import io.pivotal.android.ttc.util.Const;

public class LauncherActivity extends Activity {

	private static final int LAUNCH_MSG = 100;
	private static final int LAUNCH_DURATION = 1000;
    private static final int CONFIGURATION_RECEIVED_MSG = 200;
    private static final int CONFIGURATION_ERROR_MSG = 300;

	private final LaunchHandler mHandler = new LaunchHandler(this);

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
	}

    @Override
	protected void onStart() {
		super.onStart();
		mHandler.sendEmptyMessageDelayed(LAUNCH_MSG, LAUNCH_DURATION);
        fetchConfiguration();
	}

    private void fetchConfiguration() {
        final FetchConfigurationTask task = new FetchConfigurationTask();
        task.execute();
    }

	@Override
	protected void onPause() {
		super.onPause();
        clearHandlerQueue();
	}

    private void clearHandlerQueue() {
		mHandler.removeMessages(LAUNCH_MSG);
        mHandler.removeMessages(CONFIGURATION_RECEIVED_MSG);
        mHandler.removeMessages(CONFIGURATION_ERROR_MSG);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mHandler.removeMessages(LAUNCH_MSG);
            mHandler.sendEmptyMessage(LAUNCH_MSG);
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void launch() {
        if (!TTCPreferences.isAuthenticated(this)) {
            AuthenticationActivity.newInstance(this);
        } else {
            NotificationsActivity.newInstance(this);
        }
	}

	private static final class LaunchHandler extends Handler {

		private final LauncherActivity mActivity;
        private boolean isLauncherTimerElapsed;
        private Configuration configuration;

		public LaunchHandler(final LauncherActivity activity) {
			mActivity = activity;
            configuration = null;
            isLauncherTimerElapsed = false;
		}

		@Override
		public void handleMessage(final Message msg) {

            switch (msg.what) {

                case LAUNCH_MSG:
                    isLauncherTimerElapsed = true;
                    break;

                case CONFIGURATION_RECEIVED_MSG:
                    configuration = (Configuration) msg.obj;
                    saveConfiguration(configuration);
                    break;

                case CONFIGURATION_ERROR_MSG:
                    if (isConfigured()) {
                        configuration = TTCPreferences.getConfiguration(mActivity);
                    } else {
                        configuration = Configuration.getDefault();
                        saveConfiguration(configuration);
                    }
                    break;
            }

            if (isLaunchComplete()) {
                mActivity.launch();
            }
		}

        private void saveConfiguration(Configuration configuration) {
            TTCPreferences.setConfiguration(mActivity, configuration);
        }

        private Boolean isConfigured() {
            return TTCPreferences.isConfigured(mActivity);
        }

        private boolean isLaunchComplete() {
            return isLauncherTimerElapsed && configuration != null;
        }
    }

    private final class FetchConfigurationTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                final Configuration result = TTCApi.getConfiguration();
                final Message message = Message.obtain(mHandler, CONFIGURATION_RECEIVED_MSG, result);
                mHandler.sendMessage(message);
            } catch (Exception e) {
                Log.e(Const.TAG, "Exception upon getting configuration: " + e.getLocalizedMessage());
                mHandler.sendEmptyMessage(CONFIGURATION_ERROR_MSG);
            }
            return null;
        }
    }
}