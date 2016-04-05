package com.example.noel2.activity7;

import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private GoogleMap gm;
    private MapFragment mf;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LatLng myPosition;
    private FoodLocationsRequest foodLocations;
    private static final String URL = "https://raw.githubusercontent.com/Chechare/Android-Jason-Request-Test/master/JSONFood.json";
    private static final LatLng DEFAULT_POSITION = new LatLng(20.7349493, -103.4560317);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mf = (MapFragment) getFragmentManager().findFragmentById(R.id.mapsFragment);

        gm = mf.getMap();
        gm.setMyLocationEnabled(true);
        gm.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gm.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_POSITION, 16));

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }


    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);
        }
        else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Failed retrieving your location, retrying.", Toast.LENGTH_SHORT).show();
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        handleNewLocation(location);
    }

    private void handleNewLocation(Location location) {
        location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        myPosition = new LatLng(currentLatitude, currentLongitude);
        foodLocations = new FoodLocationsRequest(this, gm, myPosition);
        foodLocations.execute(URL);
    }


}
