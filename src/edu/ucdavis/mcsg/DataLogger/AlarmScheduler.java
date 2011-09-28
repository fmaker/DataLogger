package edu.ucdavis.mcsg.DataLogger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

public class AlarmScheduler {
	Context context;
	PendingIntent mAlarmSender;
	AlarmManager am;
	boolean started = false;
	
	public AlarmScheduler(Context context){
		this.context = context;

        mAlarmSender = PendingIntent.getService(context, 0, new Intent(context, DataLoggerService.class), 0);
        am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	}
	
	public void startAlarm(){
		if(!started){
	        // We want the alarm to go off 30 seconds from now.
	        long firstTime = SystemClock.elapsedRealtime();
	
	        // Schedule the alarm!
	        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
	                        firstTime, 2*60*1000, mAlarmSender);
	        started = true;
		}
	}
	
	public void stopAlarm(){
		if(started){
			am.cancel(mAlarmSender);
			started = false;
		}
	}
}
