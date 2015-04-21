package nu.fml.monkeymondayz;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class FindMonkeyService extends IntentService implements LocationListener {
    public static final String ACTION_FIND_MONKEY = "nu.fml.monkeymondayz.action.FINDMONKEY";

    public FindMonkeyService() {
        super("FindMonkeyService");
    }

    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FIND_MONKEY.equals(action)) {
                handleFindMonkey();
            }
        }
    }
    private Timer findTimer;
    private void handleFindMonkey() {
        LocationManager l = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        l.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,10,this);
        this.findTimer = new Timer();
        this.findTimer.schedule(new TimerTask() {
            public void run() {
                executeTimer();
            }
        },20000,0);
    }
    private Location currentLocation;
    public void onLocationChanged(Location location) {
        this.currentLocation = location;
    }
    private void executeTimer() {
        if (this.currentLocation!=null) {
            System.out.println("Current location: " + currentLocation.getLongitude());
            this.fetchTerrain();
        }
    }
    private void fetchTerrain() {
        final Handler handler = new Handler();
        final double lat = currentLocation.getLatitude();
        final double lon = currentLocation.getLongitude();
        try {
            new AsyncTask<Void,Void,Void>() {
                private String terr;
                private int pixelData;
                protected Void doInBackground(Void... params) {
                    try {
                        String fetchUrl = "http://maps.googleapis.com/maps/api/staticmap?center=" + lat + "," + lon + "&zoom=20&size=1x1&maptype=terrain&sensor=false"; //Blue?
                        URL u = new URL(fetchUrl);
                        InputStream is = u.openStream();
                        Bitmap d = BitmapFactory.decodeStream(is);
                        pixelData = d.getPixel(0,0);
                        String hexValue = Integer.toString(pixelData,16);
                        int redV = Color.red(pixelData);
                        int greenV = Color.green(pixelData);
                        int blueV = Color.blue(pixelData);
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
                    }catch(Exception e) {
                        if (e!=null) {
                            terr = e.getMessage();
                        }
                    }
                    return null;
                }

                public void onPostExecute(Void result) {
                    handler.post(new Runnable() {
                        public void run() {
                            fetchTerrainDone(terr);
                        }
                    });
                }
            }.execute();
        } catch (Exception e) {
        }
    }
    public void fetchTerrainDone(String terrain) {
        System.out.println("Found terrain: " + terrain);
    }
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    public void onProviderEnabled(String provider) {

    }
    public void onProviderDisabled(String provider) {

    }
}
