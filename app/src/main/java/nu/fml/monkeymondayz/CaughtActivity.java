package nu.fml.monkeymondayz;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class CaughtActivity extends Activity {
    private TextView txtCaught;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caught);
        String action = getIntent().getAction();

        txtCaught = (TextView) findViewById(R.id.caughtText);
        String monkeyName = getIntent().getStringExtra("monkey");
        int sound = 0;
        switch (action) {
            case "OK":
                sound = R.raw.uppgiven;
                txtCaught.setText(String.format("You caught the %s!",monkeyName));
                break;
            case "FAIL":
                sound = R.raw.skadeglad;
                txtCaught.setText(String.format("The %s escaped! Maybe some other time?",monkeyName));
                break;
            default:
                txtCaught.setText("Something went wrong :/");
                break;
        }

        MediaPlayer mp = MediaPlayer.create(this,sound);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        mp.start();
    }

    private void setText() {
       // TextView caughtView = (TextView) findViewById(R.id.textView);
       // caughtView.setText("Defaulttext");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_caught, menu);
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

    public void doContinue(View view) {
        Intent monkey = new Intent(this, MonkeyFinderService.class);
        stopService(monkey);
        startService(monkey);
        this.finish();
    }
}
