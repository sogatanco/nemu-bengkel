package site.team2dev.nemubengkel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class Routes extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Location location;
    private GoogleApiClient googleApiClient;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    MapView mapView;
    GoogleMap mGoogleMap;
    LatLng locUser;
    private MarkerOptions tujuan;
    private Polyline currentPolyline;

    private LocationRequest locationRequest;
    private static final long UPDATE_INTERVAL = 1000, FASTEST_INTERVAL = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routes);

        mapView = (MapView) findViewById(R.id.peta);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        Intent intent=getIntent();
        tujuan =new MarkerOptions().position( new LatLng(intent.getDoubleExtra("lat",0), intent.getDoubleExtra("long",0))).title("Destinantion");
    }

    @Override
    protected void onStart() {
        super.onStart();

        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!checkPlayServices()) {
            Toast.makeText(this, "You need to INstall ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (googleApiClient != null && googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
            }

            return false;
        }

        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.style_json)));
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.moveCamera(CameraUpdateFactory.zoomTo(16.5f));
        mGoogleMap.addMarker(tujuan);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        checkPermission();
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        onLocationChanged(location);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(locUser));
        startLocationUpdates();

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void startLocationUpdates() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        checkPermission();
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {

            locUser = new LatLng(location.getLatitude(), location.getLongitude());

            //move map camera
            checkPermission();
            mGoogleMap.setMyLocationEnabled(true);
            String url=getUrl(locUser, tujuan.getPosition(), "walking");
            new FetchURL(Routes.this).execute(url, "driving");
        }
    }

    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    private String getUrl(LatLng origin, LatLng dest, String directionmode){
        String str_ori="origin="+origin.latitude+","+origin.longitude;
        String srt_dest="destination="+dest.latitude+","+dest.longitude;
        String mode="mode="+directionmode;

        String parameter=str_ori+"&"+srt_dest+"&"+mode;
        String output="json";
        String url="https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameter+"&key="+getString(R.string.google_key);
        return url;
    }


    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mGoogleMap.addPolyline((PolylineOptions) values[0]);
    }

    public void close(View view) {
        finish();
    }
}
