package nu.fml.monkeymondayz;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ListView;
import android.widget.Toast;



public class MainActivity extends Activity {
    ListView listView ;
//    MediaPlayer mySound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent notificationIntent = new Intent(this, NotificationActivity.class);
        final Handler handler = new Handler();
//        mySound = MediaPlayer.create(this, R.raw.apa);

        new AsyncTask<Void,Void,Void>() {
            protected Void doInBackground(Void... params) {

                PackageManager p = getPackageManager();
                boolean hasAccelerometer = p.hasSystemFeature(PackageManager.FEATURE_SENSOR_ACCELEROMETER);
                boolean hasGyroscope = p.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
                boolean hasLight = p.hasSystemFeature(PackageManager.FEATURE_SENSOR_LIGHT);
                boolean hasMicrophone = p.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
                boolean hasGPSLocation = p.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);
                boolean hasNetworkLocation = p.hasSystemFeature(PackageManager.FEATURE_LOCATION_NETWORK);

                SharedPreferences settings = getSharedPreferences(Constants.AVAILABLE_SENSORS, 0);
                SharedPreferences.Editor editor = settings.edit();

                editor.putBoolean(Constants.PREF_ACCELEROMETER, hasAccelerometer);
                editor.putBoolean(Constants.PREF_GYROSCOPE, hasGyroscope);
                editor.putBoolean(Constants.PREF_LIGHT, hasLight);
                editor.putBoolean(Constants.PREF_MICROPHONE, hasMicrophone);
                editor.putBoolean(Constants.PREF_LOCATION_GPS, hasGPSLocation);
                editor.putBoolean(Constants.PREF_LOCATION_NETWORK, hasNetworkLocation);

                editor.commit();
            /*
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                */
                return null;
            }

            public void onPostExecute(Void result) {
                handler.post(new Runnable() {
                    public void run() {
                        setContentView(R.layout.activity_main);
                        setupList();
                    }
                });
            }

        }.execute();
        Intent monkey = new Intent(this,MonkeyFinderService.class);
        startService(monkey);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public void goGPS(View view) {
        Intent intent = new Intent(this, GPSActivity.class);
        startActivity(intent);
    }
    public void goACCEL(View view) {
        Intent intent = new Intent(this, ACCELActivity.class);
        startActivity(intent);
//        mySound.start();
    }
    public void goLight(View view) {
        Intent intent = new Intent(this, LightActivity.class);
        startActivity(intent);
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



    //Stoppar notificationen, kan innebära problems
    public static void CancelNotification(Context ctx) {
        NotificationManager notifManager= (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
    }
    @Override
    protected void onResume() {
        super.onResume();
        CancelNotification(this);
    }

    //LISTVIEW
    private void setupList() {
    }



}
