package nu.fml.monkeymondayz;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class LightActivity extends Activity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean caught;
    private Handler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        caught = false;
        myHandler = new Handler();
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register a listener for the sensor.
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        // get reading from the sensor
        float proximity = event.values[0];
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("Proximity is " + proximity + " cm");
        for(int i = 0; i<10;i++){
            if (proximity < 2) {
                myHandler.postDelayed(isCaught, 1000);
            } else {
                caught = false;
            }

        }
        if(caught == true){
        Intent intent = new Intent(this, CaughtActivity.class);
            Notify();
            startActivity(intent);
            
        }
    }

    private void Notify() {
        NotificationActivity  noti= new NotificationActivity();
        noti.createNotification(null);
    }

    private Runnable isCaught = new Runnable()
    {
        @Override
        public void run()
        {
            caught = true;
        }
    };

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // to do something
    }

    @Override
    protected void onPause() {
        super.onPause();
        // unregister the sensor to prevent battery draining
        sensorManager.unregisterListener(this);
    }

    // other code
//    private SensorManager mSensorManager;
//    private Sensor mLightSensor;
//    private float mLux = 0.0f;
//    private TextView text1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_light);
//
//        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        mLightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
//    }
//
//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_light, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }








}

//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
//            mLux = event.values[0];
//            String luxStr = String.valueOf(mLux);
//            TextView tv = (TextView) findViewById(R.id.text1);
//            tv.setText(luxStr);
//            Log.d("LUXTAG", "Lux value: " + event.values[0]);
//
//        }
//    }
//
//
//
//
//
//
//    @Override
//    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mSensorManager.registerListener(this, mLightSensor,
//                SensorManager.SENSOR_DELAY_FASTEST);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        mSensorManager.unregisterListener(this);
//    }
