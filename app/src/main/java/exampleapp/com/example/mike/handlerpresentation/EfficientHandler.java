package exampleapp.com.example.mike.handlerpresentation;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Michael Carr on 3/22/16 for Android Meet-up on 3/23/16.
 */
public class EfficientHandler extends Handler {

    public EfficientHandler(Looper looper) {
        super(looper);
    }

    public boolean doesHandle(int what) {
        return true;
    }

    public String getHandlerName() {
        return this.toString();
    }
}
