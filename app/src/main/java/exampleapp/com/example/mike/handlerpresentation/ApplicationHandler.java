package exampleapp.com.example.mike.handlerpresentation;

import android.app.Application;

/**
 * Created by Michael Carr on 3/7/16 for Android Meet-up on 3/23/16.
 */
public class ApplicationHandler extends Application {

    private CommandCenter mCommandCenter;

    public ApplicationHandler() {
        //do nothing
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mCommandCenter = new CommandCenter(this);
    }

    public CommandCenter getCommandCenter() {
        return mCommandCenter;
    }
}
