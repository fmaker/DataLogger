package edu.ucdavis.mcsg.DataLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


/**
 * @author fmaker
 *
 */
public class DataLoggerActivity extends Activity implements OnClickListener{
	private static final String TAG = "DataLoggerActivity";
	private static Button startLoggingButton, exportButton;
	private static TextView loggingTextView;
	private ActivityManager manager;
	private AlarmScheduler alarm;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        loggingTextView = (TextView) findViewById(R.id.loggingTextView);
        startLoggingButton = (Button) findViewById(R.id.startLoggingButton);
        exportButton = (Button) findViewById(R.id.exportButton);
        alarm = new AlarmScheduler(getApplicationContext());

        startLoggingButton.setOnClickListener(this);
        exportButton.setOnClickListener(this);

     }
    

    
	public void onClick(View v) {
		if(v.getId() == R.id.startLoggingButton){

			if(isDataLoggerServiceRunning()){
				alarm.stopAlarm();
			}
			else{
				alarm.startAlarm();
			}
			updateStatus();
		}
		else{
			Log.d(TAG, "Exporting database files...");
			
			String[] databases = {"power_log.db", "battery_log.db"};
			for(String db : databases){
				try {
			        File sd = Environment.getExternalStorageDirectory();
			        File data = Environment.getDataDirectory();
	
			        Log.d(TAG, "Seeing if can write to sdcard...");
			        if (sd.canWrite()) {
			            String currentDBPath = "/data/edu.ucdavis.mcsg.DataLogger/databases/"+db;
			            String backupDBPath = db;
			            File currentDB = new File(data, currentDBPath);
			            File backupDB = new File(sd, backupDBPath);
	
			            if (currentDB.exists()) {
			                FileChannel src = new FileInputStream(currentDB).getChannel();
			                FileChannel dst = new FileOutputStream(backupDB).getChannel();
			                dst.transferFrom(src, 0, src.size());
			                src.close();
			                dst.close();
			            }
			        }
			    } catch (Exception e) {
					Log.e(TAG, "Could not export files to sdcard!");
					e.printStackTrace();
				}
			}
		}
        updateStatus();
	}

	@Override
	protected void onDestroy() {
        stopService(new Intent(this, DataLoggerService.class));
		super.onDestroy();
	}

	private boolean isDataLoggerServiceRunning() {
	    manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (DataLoggerService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public void updateStatus(){

	        if(isDataLoggerServiceRunning()){
	    		startLoggingButton.setText("Stop");
	        	loggingTextView.setText("Running");
	        	loggingTextView.setTextColor(Color.GREEN);
	        }
	        else{
	    		startLoggingButton.setText("Start");
	        	loggingTextView.setText("Stopped");
	        	loggingTextView.setTextColor(Color.RED);
	        }
		}

}

