package edu.ucdavis.mcsg.DataLogger;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.android.internal.os.PowerProfile;

public class DataLoggerService extends Service{
	private static final String TAG = "DataLoggerService";
	public static final String STOP_SERVICE = "STOP_SERVICE";
	public PowerProfile mPowerProfile;
	private IntentFilter mFilter;
	public static boolean mLogging = false;
	BatteryChangedReceiver batteryChanged;
	NotificationManager notificationManager;

	private static final int STATUS_ID = 1;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		mPowerProfile = new PowerProfile(this);
		batteryChanged = new BatteryChangedReceiver();
		notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		boolean stopService = intent.getBooleanExtra(STOP_SERVICE, false);
		
		if(!stopService){
			Log.d(TAG, "Starting service");
	
			/* Setup and register battery information receiver */
	        mFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			registerReceiver(batteryChanged, mFilter);
			notification(true);
		}
		else{
			stopSelf();
		}

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "Destroying");
		unregisterReceiver(batteryChanged);
		mPowerProfile = null;
		notification(false);
		
		super.onDestroy();
	}
	
	private void notification(boolean isRunning){
		mLogging = isRunning;
		
		int icon = R.drawable.icon;
		String title = getString(R.string.app_name);
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, title, when);

		Context context = getApplicationContext();
		String contentText;
		Intent notificationIntent;
		if(isRunning){
			contentText = "Click to stop DataLogger service";
			notificationIntent = new Intent(this, DataLoggerService.class);
			notificationIntent.putExtra(STOP_SERVICE, true);
		}
		else{
			contentText = "DataLogger service stopped";
			notificationIntent = new Intent();
		}

		PendingIntent contentIntent = PendingIntent.getService(this, 0, notificationIntent, Notification.FLAG_AUTO_CANCEL|Notification.DEFAULT_VIBRATE);

		notification.setLatestEventInfo(context, title, contentText, contentIntent);

		notificationManager.notify(STATUS_ID, notification);
	}
}