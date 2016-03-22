package exampleapp.com.example.mike.handlerpresentation;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

/**
 * Created by Michael Carr on 3/7/16 for Android Meet-up on 3/23/16.
 */
public class CommandCenter {

    private static final String TAG = "CommandCenter";

    public static final int TIMER_MESSAGE = 111;
    public static final int TIMER_FINISHED_MESSAGE = 222;

    private Context mContext;
    private HandlerManager mHandlerManager;

    public CommandCenter(Context context) {
        mContext = context;
        mHandlerManager = new HandlerManager(context, this);
        mTimer.postDelayed(mTimerRunnable, mContext.getResources().getInteger(R.integer.timer_delay));
    }

    private int mCount = 0;
    private Handler mTimer = new Handler();
    private Runnable mTimerRunnable = new Runnable() {
        @Override
        public void run() {
            mCount++;
            Message msg = new Message();
            if (mCount <= mContext.getResources().getInteger(R.integer.max_count_value)) {
                msg.what = TIMER_MESSAGE;
                msg.arg1 = mCount;
                mTimer.postDelayed(mTimerRunnable, mContext.getResources().getInteger(R.integer.timer_delay));
            } else {
                msg.what = TIMER_FINISHED_MESSAGE;
                msg.arg1 = 0;
            }
            if (mHandlerManager != null && mHandlerManager.getNumberOfHandlers() > 0) {
                mHandlerManager.sendMessage(msg);
            }
        }
    };

    public int getCount() {
        return mCount;
    }

    public void resetCount() {
        mCount = 0;
        mTimer.removeCallbacks(mTimerRunnable);
        mTimer.post(mTimerRunnable);
    }

    public HandlerManager getHandlerManager() {
        return mHandlerManager;
    }

    public String getMessageText(int what) {
        switch (what) {
            case TIMER_MESSAGE:
                return "TIMER_INCREMENT";
            case TIMER_FINISHED_MESSAGE:
                return "TIMER_FINISHED";
            default:
                return "UNKNOWN MESSAGE - Throw Exception?";
        }
    }
}
