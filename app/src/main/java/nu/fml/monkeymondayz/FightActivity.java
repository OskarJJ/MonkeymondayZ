package nu.fml.monkeymondayz;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class FightActivity extends Activity {
    Data d = new Data();
    TextView myNameText;
    private ImageView imgHostile;
    private Monkey hostileMonkey;
    private ProgressBar health;
    private Vibrator vibrator;
    private FightAccelerometer fightListener;
    private SensorManager smgr;
    private Sensor acc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);
        CancelNotification(this);
        setUpViews();
        update();
        this.imgHostile = (ImageView) findViewById(R.id.imgHostile);
        this.health = (ProgressBar) findViewById(R.id.prgHealth);
        Intent i = getIntent();
        String m = i.getStringExtra("apa");

        this.hostileMonkey = new Monkey(this.getIntent().getStringExtra("apa"),this);
        this.imgHostile.setImageDrawable(this.hostileMonkey.getDrawable());
        this.health.setMax(hostileMonkey.getMaxHealth());
        this.health.setProgress(hostileMonkey.getHealth());

        /* Debug code */
        TextView txtValues = (TextView) findViewById(R.id.txtAccelerometer);
        smgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        acc = smgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        fightListener = new FightAccelerometer(txtValues,this);
        smgr.registerListener(fightListener,acc,SensorManager.SENSOR_DELAY_UI);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    private void setUpViews() {
        //myNameText = (TextView)findViewById(R.id.myNameTextView);
    }
    private void update() {
       // myNameText.setText(d.getApaName());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fight, menu);
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
    //Stoppar notificationen, kan innebära problems
    public static void CancelNotification(Context ctx) {
        NotificationManager notifManager= (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()!=MotionEvent.ACTION_DOWN) {
            return true;
        }

        if (this.health!=null) {
            if (this.hostileMonkey!=null) {
                this.hostileMonkey.doDamage(1);
                this.health.setProgress(this.hostileMonkey.getHealth());
                if (this.hostileMonkey.getHealth()<=0) {
                    Intent prison = new Intent(this,LightActivity.class);
                    startActivity(prison);
                    this.finish();
                }
            }
        }
        return true;
    }

    public void hitOnHead() {
        vibrator.vibrate(100);
        this.dmgMonkey(3);
       // this.sleep();
    }

    public void smackOnNose() {
        vibrator.vibrate(50);
        this.dmgMonkey(2);
     //   this.sleep();
    }

    private void sleep() {
        try {
            Thread.sleep(800);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void dmgMonkey(int dmg) {
        if (this.health!=null) {
            if (this.hostileMonkey!=null) {
                this.hostileMonkey.doDamage(1);
                this.health.setProgress(this.hostileMonkey.getHealth());
                if (this.hostileMonkey.getHealth()<=0) {
                    long pattern[] = {0,200,100,200};
                    vibrator.vibrate(pattern,-1);
                    Intent prison = new Intent(this,LightActivity.class);
                    this.finish();
                    startActivity(prison);
                }
            }
        }
    }

    public void onPause() {
        super.onPause();
        smgr.unregisterListener(fightListener);
    }

    public void onResume() {
        super.onResume();
        smgr.registerListener(fightListener,acc,SensorManager.SENSOR_DELAY_UI);
    }

    private class FightAccelerometer implements SensorEventListener {
        private TextView report;
        private FightActivity fighter;
        public FightAccelerometer(TextView values,FightActivity fighter) {
            this.report = values;
            this.fighter = fighter;
        }

        private boolean isTiltedDown = false,hitOnNose = false;
        public void onSensorChanged(SensorEvent event) {
            float[] g = new float[3];
            g = event.values.clone();
            float norm = (float) Math.sqrt(g[0]*g[0] + g[1]*g[1] + g[2]*g[2]);

            g[0] = g[0]/norm;
            g[1] = g[1]/norm;
            g[2] = g[2]/norm;

            int inclination = (int) Math.round(Math.toDegrees(Math.acos(g[2])));
            if (inclination < 25 || inclination > 155) {
                report.setText("Flat: " + inclination);

                if (!hitOnNose) {
                    hitOnNose = true;
                }

            }else{
                int tilt = (int) Math.round(Math.toDegrees(Math.atan2(g[0], g[1])));
                report.setText("Not flat: " + inclination + ",tilt=" + tilt);
                if (!isTiltedDown && tilt>80) {
                    this.isTiltedDown=true;
                }

                if (isTiltedDown && tilt<10) {
                    this.isTiltedDown = false;
                    fighter.hitOnHead();
                }

                if (hitOnNose && tilt < 20) {
         //           fighter.smackOnNose();
                    this.hitOnNose = false;
                }

                if (hitOnNose && tilt>19) {
                    this.hitOnNose = false;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    }
}
