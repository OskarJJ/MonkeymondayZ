package nu.fml.monkeymondayz;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Date;
import java.util.Timer;


public class LightActivity extends Activity implements SensorEventListener, Runnable {

    private SensorManager sensorManager;
    private Sensor sensor;
    private boolean caught;
    private Handler myHandler;
    private long tid1;
    private int secondsCaught;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light);
        caught = false;
        myHandler = new Handler();
        sensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        run();
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
            if (proximity < 2) {
                caught = true;
            } else {
                caught = false;
            }
        }

    public final void result(){
        if (secondsCaught>5) {
            Intent intent = new Intent(this, CaughtActivity.class);
            startActivity(intent);

        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
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

    @Override
    public void run() {
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                if(caught){
                    secondsCaught++;
                }
                System.out.println(secondsCaught);

            }

            public void onFinish() {
                result();
            }
        }.start();
    }
}
