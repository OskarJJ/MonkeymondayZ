package nu.fml.monkeymondayz;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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

        this.client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        if (googleMap == null) {
            System.out.println("Setting googleMap variable");
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            System.out.println("Value of googleMap var: " + googleMap.toString());
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
        System.out.println("Connected!");
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(this.client);
        googleMap.setMyLocationEnabled(true);
        System.out.println("Last location: " + lastLocation.toString());
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