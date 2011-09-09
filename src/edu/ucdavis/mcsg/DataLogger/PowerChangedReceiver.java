package edu.ucdavis.mcsg.DataLogger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * Records log of battery charging and discharging events
 * @todo Make content provider
 * @todo Handle battery charging events when system not running
 * 
 * @author fmaker
 */
public class PowerChangedReceiver extends BroadcastReceiver{
	private static final String TAG = "BatteryChangedReceiver";
	
	public static final int POWER_DISCONNECTED = 0;
	public static final int POWER_CONNECTED = 1;

	@Override
	public void onReceive(Context context, Intent intent) {

		String action = intent.getAction();
		int powerConnected = -1;
		
		if(action.equals(Intent.ACTION_POWER_CONNECTED)){
			powerConnected = POWER_CONNECTED;
		}
		else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)){
			powerConnected = POWER_DISCONNECTED;
		}
		
		Log.d(TAG, "powerConnect = "+powerConnected);
		
		if(powerConnected != -1){
			
			/* Open database */
			PowerLogOpenHelper mDbHelper = new PowerLogOpenHelper(context);

			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			String stmt = String.format("INSERT INTO %s ( %s, %s) VALUES (%s, %s);",
					PowerLogOpenHelper.TABLE_NAME, 
					PowerLogOpenHelper.KEY_TIMESTAMP, 
					PowerLogOpenHelper.KEY_POWER_CONNECTED,
					"strftime('%s', 'now')", powerConnected);
			db.execSQL(stmt);
			db.close();
		}

	}
    
    public int secondsSinceLastCharge(Context context){
		
		/* Open database */
		PowerLogOpenHelper mDbHelper = new PowerLogOpenHelper(context);

		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor c = db.query(
					PowerLogOpenHelper.TABLE_NAME, 
					new String[]{PowerLogOpenHelper.KEY_TIMESTAMP},
					String.format("%s == %d",
								PowerLogOpenHelper.KEY_POWER_CONNECTED,
								String.valueOf(POWER_CONNECTED)),
					null, null, null,
					String.format("%s DESC",PowerLogOpenHelper.KEY_TIMESTAMP),
					"1");

		int lastChargeTimestamp = c.getInt(c.getColumnIndex(PowerLogOpenHelper.KEY_TIMESTAMP));

		db.close();
		
		return lastChargeTimestamp;
    }
    


}
