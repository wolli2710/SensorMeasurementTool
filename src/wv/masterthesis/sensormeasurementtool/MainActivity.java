/**
 * Copyright 2014 Wolfgang Vogl
 *  
 * This software is released under the terms of the
 * MIT license. See <a href="http://opensource.org/licenses/MIT" >MIT License</a>
 * for more information.
 * 
 * @author Wolfgang Vogl
 */

package wv.masterthesis.sensormeasurementtool;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

	public class MainActivity extends Activity implements SensorEventListener {
		
		float currentUpdateRate = 0.0f;
		long previousTimeStamp = 0;
		int counter = 0;
		private SensorManager mSensorManager;
	    private Sensor accelerationSensor;
	    
	    private Button button1;
	    private Button button2;
	    private Button button3;
	    private Button button4;
	    private Button exitButton;
	    
	    private TextView currentUpdateRateDescription = null;
	    TextView currentUpdateRateString = null;
	    
	    private String updateRateMethods[] = new String[]{"SENSOR_DELAY_FASTEST", "SENSOR_DELAY_GAME", "SENSOR_DELAY_UI","SENSOR_DELAY_NORMAL"};
	    	    
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_main);
			
			currentUpdateRateString = (TextView)findViewById(R.id.updateRate);
			
			mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
			boolean isAccelerationSupported = mSensorManager.registerListener(this, 
			        mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
			        SensorManager.SENSOR_DELAY_NORMAL);
			accelerationSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
			currentUpdateRateDescription = (TextView)findViewById(R.id.currentUpdateRateText);
			if(isAccelerationSupported){
				currentUpdateRateDescription.setText("Accelerometer is not supported!");
				mSensorManager.unregisterListener(this, accelerationSensor);
			}
			
	        createButtons();
		}
		
	    public void createButtons(){
	    	button1 = (Button)findViewById(R.id.button1);
	        button2 = (Button)findViewById(R.id.button2);
	        button3 = (Button)findViewById(R.id.button3);
	        button4 = (Button)findViewById(R.id.button4);
	        exitButton = (Button)findViewById(R.id.exitButton);

	        createButtonListener(button1, SensorManager.SENSOR_DELAY_NORMAL);
	        createButtonListener(button2, SensorManager.SENSOR_DELAY_UI);
	        createButtonListener(button3, SensorManager.SENSOR_DELAY_GAME);
	        createButtonListener(button4, SensorManager.SENSOR_DELAY_FASTEST);
	        exitButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
	    }
	    
	    public void createButtonListener(Button btn, final int rate){
	        btn.setOnClickListener( new View.OnClickListener() {
				public void onClick(View v) {
					updateSensorListenerUpdateRate( rate );
					currentUpdateRateDescription.setText( updateRateMethods[rate] );
				}
	        });
	    }

	    public void updateSensorListenerUpdateRate(int rate){
	    	mSensorManager.unregisterListener(this, accelerationSensor);
			mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), rate);
	    }
	    
	    protected void onPause() {
	        super.onPause();
	        mSensorManager.unregisterListener(this);
	    }
	    
	    protected void onResume() {
	        super.onResume();
	        mSensorManager.registerListener(this, accelerationSensor, SensorManager.SENSOR_DELAY_NORMAL);
	    }

	    protected void onDestroy() {
	        super.onDestroy();
	        mSensorManager.unregisterListener(this);
	    }
	    
		@Override
		public void onSensorChanged(SensorEvent event) {
			if( event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
				double t = (event.timestamp - previousTimeStamp) / 1000000000.0;
				counter++;
				if(t >= 1.0){
					String c = String.valueOf(counter);
					currentUpdateRateString.setText("Update Frequency: " + c + " Hz");
					previousTimeStamp = event.timestamp;
					counter = 0;
				}
			}
		}
		
	    @Override
	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	        // method stub
	    }
	    
	}