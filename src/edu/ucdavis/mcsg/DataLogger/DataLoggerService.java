package edu.ucdavis.mcsg.DataLogger;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import com.android.internal.os.PowerProfile;

public class DataLoggerService extends Activity {
	private static final String TAG = "DataLoggerService";
	public PowerProfile mPowerProfile;
	private IntentFilter mFilter;

	@Override
	public void onCreate(Bundle b) {
		Log.d(TAG, "Creating");
		mPowerProfile = new PowerProfile(this);
		super.onCreate(b);
	}

	@Override
	public void onResume(){
		Log.d(TAG, "Starting");

		/* Setup and register battery information receiver */
        mFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		registerReceiver(batteryChanged, mFilter);

		super.onResume();
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "Destroying");
		unregisterReceiver(batteryChanged);
		mPowerProfile = null;
		super.onDestroy();
	}
	
	BatteryChangedReceiver batteryChanged = new BatteryChangedReceiver();
	BroadcastReceiver powerChanged = new PowerChangedReceiver();
	
	BroadcastReceiver systemChanged = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG,"Received systemChanged intent");
			
			/*if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
				startService(new Intent(getApplicationContext(), DataLoggerService.class));
			}
			else if(intent.getAction().equals(Intent.ACTION_SHUTDOWN)){
				stopService(new Intent(getApplicationContext(), DataLoggerService.class));
			}*/
		}
		
	};
	

/*	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "Binding");
		return null;
	}*/
	
	
}