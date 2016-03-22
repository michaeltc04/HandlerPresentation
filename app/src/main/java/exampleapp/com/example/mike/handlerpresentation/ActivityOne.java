package exampleapp.com.example.mike.handlerpresentation;

import android.content.Intent;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by Michael Carr on 3/7/16 for Android Meet-up on 3/23/16.
 */
public class ActivityOne extends AppCompatActivity {

    private static final String TAG = "ActivityOne";

    private CommandCenter mCommandCenter;
    private Toolbar mToolbar;
    private TextView mTextView;
    private Button mButton, mResetButton;
    private int mCount;

    private ActivityOneHandler mHandler = new ActivityOneHandler(Looper.getMainLooper(), this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);

        setupApplication();
        mToolbar = (Toolbar) findViewById(R.id.activity_one_toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.activity_one_title));
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        }

        mTextView = (TextView) findViewById(R.id.textview_one);
        mButton = (Button) findViewById(R.id.button_one);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ActivityTwo.class);
                startActivity(i);
                finish();
            }
        });
        mResetButton = (Button) findViewById(R.id.button_reset);
        mResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommandCenter.resetCount();
            }
        });
    }

    @Override
         protected void onResume() {
        super.onResume();
        if (mCommandCenter == null) {
            setupApplication();
        } else {
            mCommandCenter.getHandlerManager().registerHandler(mHandler);
        }
        mCount = mCommandCenter.getCount();
        if (mCount >= getResources().getInteger(R.integer.max_count_value)) {
            processTimerFinished();
        } else {
            processTimerMessage(Message.obtain(mHandler, CommandCenter.TIMER_MESSAGE, mCount, 0, null));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mCommandCenter.getHandlerManager().unregisterHandler(mHandler);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCommandCenter.getHandlerManager().unregisterHandler(mHandler);
        mHandler = null;
    }

    private void setupApplication() {
        final ApplicationHandler app = (ApplicationHandler) getApplication();
        mCommandCenter = app.getCommandCenter();
        mCommandCenter.getHandlerManager().registerHandler(mHandler);
    }

    private void processTimerMessage(Message msg) {
        mCount = msg.arg1;
        String countString = "" + mCount;
        mTextView.setText(countString);
    }

    private void processTimerFinished() {
        mTextView.setText(getResources().getString(R.string.timer_completed));
    }

    /**
     * Instances of static inner classes do not hold an implicit
     * reference to their outer class.
     */
    private static class ActivityOneHandler extends EfficientHandler {

        private final WeakReference<ActivityOne> mReference;

        public ActivityOneHandler(Looper looper, ActivityOne activityOne) {
            super(looper);
            mReference = new WeakReference<>(activityOne);
        }

        @Override
        public void handleMessage(Message msg) {
            ActivityOne activityOne = mReference.get();
            if (activityOne != null) {
                switch (msg.what) {
                    case CommandCenter.TIMER_MESSAGE:
                        Log.i(TAG, "\nActivityOne: Timer Message Received: Count=" + msg.arg1 + "\n");
                        activityOne.processTimerMessage(msg);
                        break;

                    case CommandCenter.TIMER_FINISHED_MESSAGE:
                        Log.i(TAG, "\nActivityOne: Final Timer Message Received\n");
                        activityOne.processTimerFinished();
                        break;
                }
            }
        }

        @Override
        public boolean doesHandle(int what) {
            switch (what) {
                case CommandCenter.TIMER_MESSAGE:
                case CommandCenter.TIMER_FINISHED_MESSAGE:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public String getHandlerName() {
            return "ActivityOneHandler";
        }
    }
}
