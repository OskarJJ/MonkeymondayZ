package nu.fml.monkeymondayz;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class FightActivity extends ActionBarActivity {
    Data d = new Data();
    TextView myNameText;
    private ImageView imgHostile;
    private Monkey hostileMonkey;
    private ProgressBar health;
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
}
