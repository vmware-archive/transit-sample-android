package io.pivotal.android.ttc;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

public class LauncherActivity extends Activity {

	private static final int LAUNCH_MSG = 100;
	private static final int LAUNCH_DURATION = 1000;

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
	}

	@Override
	protected void onPause() {
		super.onPause();
		mHandler.removeMessages(LAUNCH_MSG);
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

    public void launch() {
//        if (!TTCPreferences.isAuthenticated(this)) {
//            AuthenticationActivity.newInstance(this);
//        } else {
            NotificationsActivity.newInstance(this);
//        }
	}

	private static final class LaunchHandler extends Handler {

		private final LauncherActivity mActivity;

		public LaunchHandler(final LauncherActivity activity) {
			mActivity = activity;
		}

		@Override
		public void handleMessage(final Message msg) {
			super.handleMessage(msg);

			if (msg.what == LAUNCH_MSG) {
				mActivity.launch();
			}
		}
	}
}