package edu.ucdavis.mcsg.DataLogger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {
	private static final String TAG = "BootCompletedReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG,"Received systemChanged intent");
		AlarmScheduler alarm = new AlarmScheduler(context);
		alarm.startAlarm();
	}
	
}
