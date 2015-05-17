package nu.fml.monkeymondayz;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class CatchActivity extends Activity implements SensorEventListener {
    private static final long HANDLER_DELAY = 100;
    private TextView txtCatchText;
    private Animation textAnimation;
    private AnimationDrawable imgAnimation;
    private ImageView imgCatchHelp;

    private SensorManager mgr;
    private Sensor sensor;
    private Vibrator vibrate;
    private String monkey;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catch);

        imgCatchHelp = (ImageView) findViewById(R.id.imgCatchHelp);
        imgCatchHelp.setBackgroundResource(R.drawable.catchanimation);
        imgAnimation = (AnimationDrawable) imgCatchHelp.getBackground();
        imgAnimation.start();

        txtCatchText = (TextView) findViewById(R.id.txtCatch);
        textAnimation = AnimationUtils.loadAnimation(this,R.anim.catchtextanimation);
        txtCatchText.startAnimation(textAnimation);

        monkey = getIntent().getStringExtra("monkey");

        mgr = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensor = mgr.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        mp = MediaPlayer.create(this,R.raw.stressad);
        mp.setLooping(true);

        this.lastCheck = System.currentTimeMillis();
        this.startCheck = System.currentTimeMillis();
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            private long totalTime;
            public void run() {
                long now = System.currentTimeMillis();
                long diff = now-lastCheck;
                long checkIt = now-startCheck;
                if (checkIt>10000) {
                    isDone(totalTime);
                    return;
                }
                if (isCatching()) {
                    if (vibrate.hasVibrator()) {
                        float vol = (float)checkIt/(float)10000;
                        mp.setVolume(vol,vol);
                        System.out.println("Setting volume to: " + vol);
                        vibrate.vibrate((checkIt/125));
                        if (!mp.isPlaying()) {
                            System.out.println("Not playing, start it!");
                            mp.start();
                        }
                    }
                    totalTime+=diff;
                }else{
                    if (mp.isPlaying()) {
                        System.out.println("is playing, pause it!");
                        mp.pause();
                    }
                }
                lastCheck = now;
                System.out.println(String.format("now=%d, diff=%d, checkIt=%d",now,diff,checkIt));
                h.postDelayed(this,HANDLER_DELAY);
            }
        },HANDLER_DELAY);
    }

    long lastCheck,startCheck;
    @Override
    protected void onResume() {
        super.onResume();
        mgr.registerListener(this,sensor,SensorManager.SENSOR_DELAY_UI);
    }

    private void isDone(long total) {
        Intent i = new Intent(this,CaughtActivity.class);
        i.putExtra("monkey",monkey);
        if (total>5000) {
            i.setAction("OK");
        }else{
            i.setAction("FAIL");
        }
        mp.stop();
        mp.release();
        System.out.println(String.format("Captured for a total of %d seconds",total));
        this.finish();
        startActivity(i);
    }

    private boolean isCatching = false;
    @Override
    public final void onSensorChanged(SensorEvent event) {
        float[] val = event.values;
        if (val[0]<sensor.getMaximumRange()) {
            this.isCatching = true;
        }else{
            this.isCatching = false;
        }
    }

    private boolean isCatching() {
        return isCatching;
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
        // to do something
    }

    @Override
    protected void onPause() {
        super.onPause();
        mgr.unregisterListener(this);
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
}
