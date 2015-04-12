package nu.fml.monkeymondayz;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.*;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.Plus;

public class GpsMapActivity extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,com.google.android.gms.location.LocationListener {

    private GoogleApiClient client;
    private GoogleMap googleMap;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_map);
        SharedPreferences settings = getSharedPreferences(Constants.AVAILABLE_SENSORS,0);
        if (settings.getBoolean(Constants.PREF_LOCATION_NETWORK,false) || settings.getBoolean(Constants.PREF_LOCATION_GPS,false)) {
            System.out.println("Have either network or gps location support");
            LocationManager lMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!lMgr.isProviderEnabled(LocationManager.GPS_PROVIDER) && !lMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                //Neither GPS or Network location provider is enabled
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
                dialogBuilder.setMessage("Please enable a location provider under settings on your device");
                dialogBuilder.setTitle("Enable location provider");
                dialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialogBuilder.setCancelable(false);
                dialogBuilder.create().show();
                System.out.println("No provider is enabled");
            }
        }else{
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setMessage("It seems like your device doesn't have support for any location services");
            dialogBuilder.setTitle("No location support");
            dialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            dialogBuilder.setCancelable(false);
            dialogBuilder.create().show();
        }

        this.client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        this.mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        client.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(this.client);
        googleMap.setMyLocationEnabled(true);
        LocationServices.FusedLocationApi.requestLocationUpdates(client,mLocationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("Connection was suspended: " + i);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("Could not connect to Google Services API for some reason");
    }

    @Override
    public void onLocationChanged(Location location) {
        System.out.println("Lat: " + location.getLatitude() + ", Long: " + location.getLongitude());
    }

}