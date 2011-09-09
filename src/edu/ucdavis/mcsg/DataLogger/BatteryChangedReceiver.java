package edu.ucdavis.mcsg.DataLogger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.BatteryManager;

import com.android.internal.os.PowerProfile;

/**
 * Records log of battery percent changes
 * 
 * @author fmaker
 */
public class BatteryChangedReceiver extends BroadcastReceiver{
	private static final String TAG = "BatteryChangedReceiver";

	private static final int SECS_IN_HOUR = 60*60;
	private static final float MA_IN_AMP = 1000;
	private static final float MV_IN_VOLT = 1000;

	@Override
	public void onReceive(Context context, Intent intent) {
		PowerProfile mPowerProfile = new PowerProfile(context);

		int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
		int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
		float voltage = (float) intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) / (float) MV_IN_VOLT;
		float mPercent = (float) level / (float)scale;

		float mEnergy = (float) ((mPowerProfile.getBatteryCapacity() / MA_IN_AMP) * SECS_IN_HOUR * voltage * mPercent);

		/* Open database */
		BatteryLogOpenHelper mDbHelper = new BatteryLogOpenHelper(context);

		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		String stmt = String.format("INSERT INTO %s ( %s, %s, %s, %s, %s) VALUES (%s, %s, %s, %s, %s);",
				BatteryLogOpenHelper.TABLE_NAME, 
				BatteryLogOpenHelper.KEY_TIMESTAMP, 
				BatteryLogOpenHelper.KEY_LEVEL,
				BatteryLogOpenHelper.KEY_VOLTAGE,
				BatteryLogOpenHelper.KEY_PERCENT,
				BatteryLogOpenHelper.KEY_ENERGY	,
				"strftime('%s', 'now')", 
				level, voltage, mPercent, mEnergy);
		db.execSQL(stmt);
		db.close();

	}

}
