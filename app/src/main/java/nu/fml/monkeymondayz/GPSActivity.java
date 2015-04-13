package nu.fml.monkeymondayz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpRetryException;
import java.net.MalformedURLException;
import java.net.URL;


public class GPSActivity extends ActionBarActivity implements LocationListener {
    private Button btnOpen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        LocationManager lMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        TextView txtDebug = (TextView) findViewById(R.id.txtDebug);

        SharedPreferences settings = getSharedPreferences(Constants.AVAILABLE_SENSORS,0);
        btnOpen = (Button) findViewById(R.id.btnOpenMap);
        btnOpen.setEnabled(true);
        if (settings.getBoolean(Constants.PREF_LOCATION_GPS, false)) {
            lMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,1,this);
            txtDebug.setText("Using GPS");
        }else if (settings.getBoolean(Constants.PREF_LOCATION_NETWORK,false)) {
            lMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10,1,this);
            txtDebug.setText("Using network provider");
        }else{
            System.out.println("No provider is enabled");
            txtDebug.setText("No location provider is enabled");
            //btnOpen.setEnabled(false);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_g, menu);
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

    private Location currentLocation;

    @Override
    public void onLocationChanged(Location location) {
        final TextView txtTerrain = (TextView) findViewById(R.id.txtTerrain);
        final double lat = location.getLatitude();
        final double lon = location.getLongitude();
        if (currentLocation!=null) {
            if (currentLocation.getLatitude()!=location.getLatitude() || currentLocation.getLongitude()!=location.getLongitude()) {
                final Handler handler = new Handler();
                try {
                    new AsyncTask<Void,Void,Void>() {
                        private String message;
                        private int pixelData;
                        protected Void doInBackground(Void... params) {
                            try {
                                //String fetchUrl = "http://maps.googleapis.com/maps/api/staticmap?center=55.7039512,13.1807435&zoom=20&size=1x1&maptype=terrain&sensor=false"; //White?
//                                String fetchUrl = "http://maps.googleapis.com/maps/api/staticmap?center=15.326572,-76.157227&zoom=20&size=1000x1000&maptype=terrain&sensor=false"; //Blue?
                                String fetchUrl = "http://maps.googleapis.com/maps/api/staticmap?center=" + lat + "," + lon + "&zoom=20&size=1000x1000&maptype=terrain&sensor=false"; //Blue?

                                //System.out.println("Setting URL!");
                                URL u = new URL(fetchUrl);
                                //System.out.println("Done");
                                //System.out.println("Opening stream");
                                InputStream is = u.openStream();
                                //System.out.println("Done!");
                                //System.out.println("Decoding bitmapstream");
                                Bitmap d = BitmapFactory.decodeStream(is);
                                //System.out.println("Done!");
                                pixelData = d.getPixel(0,0);

                                message = "done... (" + pixelData + ")";
                            }catch(Exception e) {
                               // System.out.println("Inner async error");
                               // e.printStackTrace();
                            }
                            return null;
                        }

                        public void onPostExecute(Void result) {
                            handler.post(new Runnable() {
                                public void run() {
                                    txtTerrain.setText("Fetched terrain: " + message);
                                    txtTerrain.setBackgroundColor(pixelData);
                                }
                            });
                        }
                    }.execute();
                } catch (Exception e) {
                   // System.out.println("Async error");
                   // e.printStackTrace();
                }
            }
        }


        currentLocation = location;
        TextView txtLoc = (TextView) findViewById(R.id.txtGPS);
       // System.out.println("onLocationChanged()");
        txtLoc.setText(location.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
       // System.out.println("onStatusChanged()");
    }

    @Override
    public void onProviderEnabled(String provider) {
        TextView txtLoc = (TextView) findViewById(R.id.txtGPS);

        txtLoc.setText("Provider enabled");
       // System.out.println("Provider enabled");

        //btnOpen.setEnabled(true);

    }

    @Override
    public void onProviderDisabled(String provider) {
        TextView txtLoc = (TextView) findViewById(R.id.txtGPS);
        txtLoc.setText("Provider disabled");
        //System.out.println("Provider disabled");
        //btnOpen.setEnabled(false);
    }

    public void openMap(View v) {
        Intent intent = new Intent(this, GpsMapActivity.class);
        startActivity(intent);
    }
}
