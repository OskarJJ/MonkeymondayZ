package nu.fml.monkeymondayz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.ImageView;
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
import java.util.Timer;
import java.util.TimerTask;


public class GPSActivity extends ActionBarActivity implements LocationListener {
    private Button btnOpen;
    private Timer checkTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        //LocationManager lMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //TextView txtDebug = (TextView) findViewById(R.id.txtDebug);

        //SharedPreferences settings = getSharedPreferences(Constants.AVAILABLE_SENSORS,0);
        //btnOpen = (Button) findViewById(R.id.btnOpenMap);
        //btnOpen.setEnabled(true);
        /*if (settings.getBoolean(Constants.PREF_LOCATION_GPS, false)) {
            lMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER,10,1,this);
            txtDebug.setText("Using GPS");
        }else*//*
        if (settings.getBoolean(Constants.PREF_LOCATION_NETWORK,false)) {
            lMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10,1,this);
            txtDebug.setText("Using network provider");
        }else{
            System.out.println("No provider is enabled");
            txtDebug.setText("No location provider is enabled");
        }

        this.checkTimer = new Timer();
        this.checkTimer.schedule(new TimerTask() {
            public void run() {
                checkTerrain();
            }
        },0,5000);
        */
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
    private String currentVegetation;

    public void onLocationChanged(Location location) {
        /*
        final TextView txtTerrain = (TextView) findViewById(R.id.txtTerrain);
        final double lat = location.getLatitude();
        final double lon = location.getLongitude();
        if (currentLocation!=null) {
            if (currentLocation.getLatitude()!=location.getLatitude() || currentLocation.getLongitude()!=location.getLongitude()) {
                final Handler handler = new Handler();
                try {
                    new AsyncTask<Void,Void,Void>() {
                        private String message,terr;
                        private int pixelData;
                        protected Void doInBackground(Void... params) {
                            try {
                                String fetchUrl = "http://maps.googleapis.com/maps/api/staticmap?center=" + lat + "," + lon + "&zoom=20&size=1x1&maptype=terrain&sensor=false"; //Blue?
                                URL u = new URL(fetchUrl);
                                InputStream is = u.openStream();
                                Bitmap d = BitmapFactory.decodeStream(is);
                                pixelData = d.getPixel(0,0);
                                message = "done... (" + pixelData + ")";
                                String hexValue = Integer.toString(pixelData,16);
                                int redV = Color.red(pixelData);
                                int greenV = Color.green(pixelData);
                                int blueV = Color.blue(pixelData);

                                String sRGB = "R:" + redV + " G:" + greenV + " B:" + blueV;
                                String extraData = "(" + pixelData + "/" + sRGB + ")";
                                terr = "none";

                                int hValOne = Math.max(redV,greenV);
                                int hValTwo = Math.max(hValOne,blueV);

                                if (hValTwo==redV) {
                                    pixelData = Color.RED;
                                }else if (hValTwo==greenV) {
                                    pixelData = Color.GREEN;
                                }else if (hValTwo==blueV) {
                                    pixelData = Color.BLUE;
                                }else {
                                    pixelData = Color.WHITE;
                                }

                                if (pixelData==Color.WHITE || pixelData==Color.BLACK) {
                                    //Asphalt
                                    terr = "asphalt";
                                }

                                if (pixelData==Color.RED) {
                                    terr = "building";
                                }

                                if (pixelData==Color.BLUE) {
                                    terr = "water";
                                }

                                if (pixelData==Color.GREEN) {
                                    terr = "forest";

                                }
                                message = terr + ", " + extraData;
                                //txtTerrain.setText("terrain: " + terr + ", " + extraData);
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
                                    currentVegetation = terr;
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
        txtLoc.setText(location.toString());*/
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
       // System.out.println("onStatusChanged()");
    }

    private void checkTerrain() {
       /* if (currentVegetation!=null) {
            if (currentVegetation.compareTo("none")!=0) {
                System.out.println("Current vegetation: " + currentVegetation);
                this.checkTimer.cancel();
            }
        }else{
            System.out.println("Current vegetation is null");
        }*/
    }


    @Override
    public void onProviderEnabled(String provider) {
        //TextView txtLoc = (TextView) findViewById(R.id.txtGPS);

        //txtLoc.setText("Provider enabled");
       // System.out.println("Provider enabled");

        //btnOpen.setEnabled(true);

    }

    @Override
    public void onProviderDisabled(String provider) {
        //TextView txtLoc = (TextView) findViewById(R.id.txtGPS);
        //txtLoc.setText("Provider disabled");
        //System.out.println("Provider disabled");
        //btnOpen.setEnabled(false);
    }

    public void openMap(View v) {
        //Intent intent = new Intent(this, GpsMapActivity.class);
        //startActivity(intent);
        ImageView imgV = (ImageView) findViewById(R.id.imgMonkey);
        Monkey m = new Monkey(this);
        imgV.setImageDrawable(m.getDrawable());
    }

    private int identifyColor(int r,int g,int b) {
        /*int hVal = Math.max(r,g);
        int hValTwo = Math.max(hVal,b);

        if (hValTwo==r) {
            return Color.RED;
        }

        if (hValTwo==g) {
            return Color.GREEN;
        }

        if (hValTwo==b) {
            return Color.BLUE;
        }*/
        return Color.BLACK;

    }
}
