package exampleapp.com.example.mike.handlerpresentation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by Michael Carr on 3/8/16 for Android Meet-up on 3/23/16.
 */
public class ActivityTwo extends AppCompatActivity {

    private static final String TAG = "ActivityTwo";

    private CommandCenter mCommandCenter;
    private Toolbar mToolbar;
    private TextView mTextView;
    private Button mButton;
    private int mCount;

    private ActivityTwoHandler mHandler = new ActivityTwoHandler(Looper.getMainLooper(), this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);

        setupApplication();
        mToolbar = (Toolbar) findViewById(R.id.activity_two_toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getResources().getString(R.string.activity_two_title));
            getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        }

        mTextView = (TextView) findViewById(R.id.textview_two);
        mButton = (Button) findViewById(R.id.button_two);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ActivityOne.class);
                startActivity(i);
                finish();
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
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCommandCenter.getHandlerManager().unregisterHandler(mHandler);
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

    private void processTimerFinished() {
        mTextView.setText(getResources().getString(R.string.timer_completed));
    }

    /**
     * Instances of static inner classes do not hold an implicit
     * reference to their outer class.
     */
    private static class ActivityTwoHandler extends EfficientHandler {

        private final WeakReference<ActivityTwo> mReference;

        public ActivityTwoHandler(Looper looper, ActivityTwo activityTwo) {
            super(looper);
            mReference = new WeakReference<>(activityTwo);
        }

        @Override
        public void handleMessage(Message msg) {
            ActivityTwo activityTwo = mReference.get();
            if (activityTwo != null) {
                switch (msg.what) {
                    case CommandCenter.TIMER_FINISHED_MESSAGE:
                        Log.i(TAG, "\nActivityTwo: Final Timer Message Received\n");
                        activityTwo.processTimerFinished();
                        break;
                }
            }
        }

        @Override
        public boolean doesHandle(int what) {
            switch (what) {
                case CommandCenter.TIMER_FINISHED_MESSAGE:
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public String getHandlerName() {
            return "ActivityTwoHandler";
        }
    }

}
