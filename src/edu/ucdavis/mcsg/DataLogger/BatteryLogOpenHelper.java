package edu.ucdavis.mcsg.DataLogger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author fmaker
 *
 */
public class BatteryLogOpenHelper extends SQLiteOpenHelper {

	public static final String TABLE_NAME = "log";
	public static final String KEY_TIMESTAMP = "timestamp";
	public static final String KEY_LEVEL = "level";
	public static final String KEY_VOLTAGE = "voltage";
	public static final String KEY_PERCENT = "percent";
	public static final String KEY_ENERGY = "energy";
	
	private static final String DATABASE_NAME = "battery_log.db";

	protected static final int DATABASE_VERSION = 1;
    
    private static final String TABLE_CREATE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                KEY_TIMESTAMP + " INTEGER, " +
                KEY_LEVEL + " INTEGER, " +
                KEY_VOLTAGE + " REAL, " +
                KEY_PERCENT + " REAL, " +
                KEY_ENERGY + " REAL);";

	public BatteryLogOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}

}
