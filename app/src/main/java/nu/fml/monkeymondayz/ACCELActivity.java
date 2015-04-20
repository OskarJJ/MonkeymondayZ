package nu.fml.monkeymondayz;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.Intent;



public class ACCELActivity extends Activity implements SensorEventListener{
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private TextView text1;
    private TextView text2;
    private TextView text3;

    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accel);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        setUpViews();

    }
    private void setUpViews() {
        text1 = (TextView)findViewById(R.id.xnumber);
        text2 = (TextView)findViewById(R.id.ynumber);
        text3 = (TextView)findViewById(R.id.znumber);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_accel, menu);
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
    protected void onPause(){
        super.onPause();
        senSensorManager.unregisterListener(this);
    }
    protected void onResume(){
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if((curTime - lastUpdate) > 100){
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y +z -last_x -last_y - last_z)/diffTime * 10000;

                if(speed > SHAKE_THRESHOLD){

                    text1.setText("X: " + String.valueOf(Math.round(x)));
                    text2.setText("Y: " + String.valueOf(Math.round(y)));
                    text3.setText("Z: " + String.valueOf(Math.round(z)));
                }
                last_x = x;
                last_y = y;
                last_z = z;

            }

        }
        if(last_x>10||last_y>10||last_z>10){
            Intent intent = new Intent(this, LightActivity.class);
            startActivity(intent);
    }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
