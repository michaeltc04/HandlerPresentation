package exampleapp.com.example.mike.handlerpresentation;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Michael Carr on 3/21/16 for Android Meet-up on 3/23/16.
 */
public class HandlerManager {

    private static final String TAG = "HandlerManager";

    private Context mContext;
    private final Set<EfficientHandler> mHandlers;
    private CommandCenter mCommandCenter;

    public HandlerManager(Context context, CommandCenter commandCenter) {
        mHandlers = new LinkedHashSet<>();
        mContext = context;
        mCommandCenter = commandCenter;
    }

    /**
     * Sends a message with no arguments or objects.
     * @param what The Message type.
     */
    public void sendEmptyMessage(int what) {
        Message msg = Message.obtain(null, what, 0, 0, null);
        sendMessage(msg);
    }

    /**
     * Sends a message with arguments and an object if desired.
     * @param what The Message type.
     * @param arg1 The first argument.
     * @param arg2 The second argument.
     * @param obj The object being passed along with the message to all handlers listening for this Message type.
     */
    public void sendMessage(int what, int arg1, int arg2, Object obj) {
        Message msg = Message.obtain(null, what, arg1, arg2, obj);
        sendMessage(msg);
    }

    /**
     * Sends a message with the specified arguments and object at the specified delay.
     * @param what The Message type.
     * @param arg1 The first argument.
     * @param arg2 The second argument.
     * @param obj The object being passed along with the message to all handlers listening for this Message type.
     * @param delayMillis The specified delay in milliseconds to be sent.
     */
    public void sendMessageDelayed(int what, int arg1, int arg2, Object obj, long delayMillis) {
        Message msg = Message.obtain(null, what, arg1, arg2, obj);
        sendMessageDelayed(msg, delayMillis);
    }

    /**
     * Iterates through all Handlers and sends the desired message out.
     * @param msg The message to be sent to all handler's attached to mHandlers.
     */
    public void sendMessage(Message msg) {
        Message copyMsg;
        synchronized (mHandlers) {
            for (EfficientHandler handler : mHandlers) {
                if (handler.doesHandle(msg.what)) {
                    copyMsg = Message.obtain(handler, msg.what, msg.arg1, msg.arg2, msg.obj);
                    handler.sendMessage(copyMsg);
                    Log.d(TAG, "\nMessage: what=" + mCommandCenter.getMessageText(copyMsg.what) + " sent to Handler=" + handler.getHandlerName() + "\n");
                }
            }
        }
    }

    /**
     * Iterates through all Handlers and sends the desired message out at the specified delay.
     * @param msg The message to be sent to all handler's attached to mHandlers.
     * @param delayMillis The specified delay in milliseconds to be sent.
     */
    public void sendMessageDelayed(Message msg, long delayMillis) {
        Message copyMsg;
        synchronized (mHandlers) {
            for (EfficientHandler handler : mHandlers) {
                if (handler.doesHandle(msg.what)) {
                    copyMsg = Message.obtain(handler, msg.what, msg.arg1, msg.arg2, msg.obj);
                    handler.sendMessageDelayed(copyMsg, delayMillis);
                    Log.d(TAG, "\nMessage: what=" + mCommandCenter.getMessageText(copyMsg.what) + " with Delay=" + delayMillis + " sent to Handler=" + handler.getHandlerName() + "\n");
                }
            }
        }
    }

    public void registerHandler(EfficientHandler handler) {
        if (handler != null) {
            synchronized (mHandlers) {
                if (!mHandlers.contains(handler)) {
                    Log.d(TAG, "\nRegistered Handler(" + handler.getHandlerName() + ")\n");
                    mHandlers.add(handler);
                }
            }
        }
    }

    public void unregisterHandler(EfficientHandler handler) {
        if (handler != null) {
            synchronized (mHandlers) {
                if (mHandlers.contains(handler)) {
                    Log.d(TAG, "\nUnregistered Handler(" + handler.getHandlerName() + ")\n");
                    mHandlers.remove(handler);
                }
            }
        }
    }

    public int getNumberOfHandlers() {
        return mHandlers.size();
    }
}
