package za.co.joker.utils;

import android.app.Activity;
import android.content.Context;

public interface WSCallsUtilsTaskCaller
{
    public void taskCompleted(String response, int reqCode, boolean isOffline);
    public Activity getActivity();
    public Context getCallingContext();
}
