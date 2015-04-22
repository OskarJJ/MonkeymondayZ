package nu.fml.monkeymondayz;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;

import java.io.InputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MonkeyFinderService extends Service {
    private Location currentLocation;
    private LocationManager l;
    private Timer checkTimer;

    public void onCreate() {
        super.onCreate();
        this.l = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        l.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,1,new MonkeyListener(this));
        this.checkTimer = new Timer();
        this.checkTimer.schedule(new TimerTask() {
            public void run() {
                checkLocation();
            }
        },0,20000);
    }

    private void checkLocation() {
        System.out.println("checkLocation()");
        if (currentLocation!=null) {
            checkTerrain();
        }
    }

    private void checkTerrain() {
        System.out.println("CheckTerrain()");
        Thread t = new FetchMap(this);
        t.start();
    }
    public void setTerrain(String t) {
        if (t!=null) {
            if (t.compareTo("none")!=0) {
                System.out.println("Identified terrain: " + t);
                this.checkTimer.cancel();

                Data d = new Data();

                //Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Uri alarmSound = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.alert);

                Intent intent = new Intent(this, MainActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

                Intent fIntent = new Intent(this, FightActivity.class);
                fIntent.putExtra("MONKEY",d.getApa(t));

                PendingIntent fightIntent = PendingIntent.getActivity(this, 0, fIntent, 0);
                Notification noti = new Notification.Builder(this)
                        .setContentTitle("A wild Monkeymon appeared" + "!")
                        .setContentText(d.getApa(t)).setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pIntent)
                        .setContentIntent(fightIntent)
                        .addAction(R.drawable.fig, "Fight", fightIntent)
                        .addAction(R.drawable.run, "Run", pIntent)
                        .build();
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                noti.flags |= Notification.FLAG_AUTO_CANCEL;
                noti.sound = alarmSound;

                Vibrator s = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (s.hasVibrator()) {
                    s.vibrate(4000);
                }


                notificationManager.notify(0, noti);
            }
        }
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_NOT_STICKY;
    }

    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public class MonkeyListener implements LocationListener {
        private MonkeyFinderService monkey;
        public MonkeyListener(MonkeyFinderService monkey) {
            this.monkey = monkey;
        }

        @Override
        public void onLocationChanged(Location location) {
            System.out.println("Location changed");
            monkey.currentLocation = location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    public class FetchMap extends Thread {
        private MonkeyFinderService monkey;
        public FetchMap(MonkeyFinderService monkey) {
            this.monkey = monkey;
        }
        public void run() {
            int pixelData;
            String terr;
            double lat = monkey.currentLocation.getLatitude();
            double lon = monkey.currentLocation.getLongitude();
            try {
                String fetchUrl = "http://maps.googleapis.com/maps/api/staticmap?center=" + lat + "," + lon + "&zoom=20&size=1x1&maptype=terrain&sensor=false"; //Blue?
                URL u = new URL(fetchUrl);
                InputStream is = u.openStream();
                Bitmap d = BitmapFactory.decodeStream(is);
                pixelData = d.getPixel(0,0);
                int redV = Color.red(pixelData);
                int greenV = Color.green(pixelData);
                int blueV = Color.blue(pixelData);
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

                if (pixelData==Color.WHITE || pixelData==Color.BLACK || pixelData==Color.RED) {
                    terr = "asphalt"; //Asphalt
                }

                if (pixelData==Color.BLUE) {
                    terr = "water";
                }

                if (pixelData==Color.GREEN) {
                    terr = "forest";
                }
                monkey.setTerrain(terr);

            }catch(Exception e) {
                if (e!=null) {
                    terr = e.getMessage();
                    monkey.setTerrain(terr);
                }
            }
        }

    }
}
