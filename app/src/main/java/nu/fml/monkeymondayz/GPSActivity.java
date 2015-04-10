package nu.fml.monkeymondayz;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class GPSActivity extends ActionBarActivity implements LocationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        LocationManager lMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //lMgr.req
        lMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,500,0,this);
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

    @Override
    public void onLocationChanged(Location location) {
        TextView txtLoc = (TextView) findViewById(R.id.txtGPS);
        System.out.println("onLocationChanged()");
        txtLoc.setText(location.toString());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        System.out.println("onStatusChanged()");
    }

    @Override
    public void onProviderEnabled(String provider) {
        TextView txtLoc = (TextView) findViewById(R.id.txtGPS);

        txtLoc.setText("Provider enabled");
        System.out.println("Provider enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        TextView txtLoc = (TextView) findViewById(R.id.txtGPS);
    txtLoc.setText("Provider disabled");
    System.out.println("Provider disabled");
    }
}
