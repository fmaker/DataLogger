package edu.ucdavis.mcsg.DataLogger;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


/**
 * @author fmaker
 *
 */
public class DataLoggerActivity extends Activity {
	private static final String TAG = "DataLoggerActivity";
	private static Button startLoggingButton;
	private static TextView loggingTextView;
	private ActivityManager manager;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        loggingTextView = (TextView) findViewById(R.id.loggingTextView);
        startLoggingButton = (Button) findViewById(R.id.startLoggingButton);
        startLoggingButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

					if(isDataLoggerServiceRunning()){
						stopService(new Intent(getApplicationContext(), DataLoggerService.class));
					}
					else{
						startService(new Intent(getApplicationContext(), DataLoggerService.class));
					}
					updateStatus();
				}
        });

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

