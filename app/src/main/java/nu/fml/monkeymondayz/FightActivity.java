package nu.fml.monkeymondayz;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Vibrator;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class FightActivity extends Activity implements SensorEventListener {
    Data d = new Data();
    TextView myNameText;
    private ImageView imgHostile;
    private Monkey hostileMonkey;
    private ProgressBar health;
    private Vibrator vibrator;
    private FightAccelerometer fightListener;
    private SensorManager smgr;
    private Sensor acc;
    private Sensor prox;

    private String monkey;
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
        monkey = i.getStringExtra("apa");

        health.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
        health.setScaleY(3.0f);

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

        animImg = (ImageView) findViewById(R.id.imgGestureHelp);
        animImg.setImageResource(R.drawable.m1);

        animation = AnimationUtils.loadAnimation(this,R.anim.rotimg);
        animImg.startAnimation(animation);
        //animImg.setBackgroundResource(R.drawable.hitanimation);
        //animation = (AnimationDrawable) animImg.getBackground();
        //animation.start();

        //Animation rotAnim = new RotateAnimation(0f,45f,Animation.RELATIVE_TO_SELF,animImg.getWidth()/2,Animation.RELATIVE_TO_SELF,animImg.getHeight()/2);
        //rotAnim.setInterpolator(new LinearInterpolator());
        //animImg.startAnimation(rotAnim);

        prox = smgr.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        smgr.registerListener(this,prox,SensorManager.SENSOR_DELAY_FASTEST);

        //removeWhenDone(animation);
    }

    private ImageView animImg;
    Animation animation;

    private void removeWhenDone(AnimationDrawable anim) {
        final AnimationDrawable a = anim;
        Handler h = new Handler();
        h.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (a.getCurrent()!=a.getFrame(a.getNumberOfFrames()-1)) {
                    removeWhenDone(a);
                }else{
                    removeAnimation();
                }
            }
        },300);
    }

    private void removeAnimation() {
        animation.cancel();
        animImg.setVisibility(View.GONE);
    }

    private void setUpViews() {
    }
    private void update() {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fight, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void CancelNotification(Context ctx) {
        NotificationManager notifManager= (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
    }
    private boolean isTouching = false;
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println(event);
        if (event.getAction()==MotionEvent.ACTION_DOWN) {
            this.isTouching = true;
        }else if (event.getAction()==MotionEvent.ACTION_UP) {
            this.isTouching = false;
        }
        return true;
    }

    public void hitOnHead() {
        vibrator.vibrate(100);
        this.dmgMonkey(3);
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
                this.hostileMonkey.doDamage(dmg);
                this.health.setProgress(this.hostileMonkey.getHealth());
                if (this.hostileMonkey.getHealth()<=0) {
                    long pattern[] = {0,200,100,200};
                    vibrator.vibrate(pattern,-1);
                    Intent prison = new Intent(this,CatchActivity.class);
                    prison.putExtra("monkey",monkey);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] val = event.values;
        if (val[0]<prox.getMaximumRange()) {
            System.out.println("val under max range");
            if (this.isTouching) {
                System.out.println("is touching :D");
                hitOnHead();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

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
