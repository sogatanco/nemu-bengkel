package site.team2dev.nemubengkel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.Context.LOCATION_SERVICE;

public class HomeFragment extends Fragment implements
        OnMapReadyCallback, LocationListener {

    GoogleMap mGoogleMap;
    MapView mapView;
    View mView;

    LocationManager locationManager;
    Location locat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.home_fragment, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) mView.findViewById(R.id.peta);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locat = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
        onLocationChanged(locat);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

//        MapsInitializer.initialize(getContext());
        mGoogleMap=googleMap;
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        LatLng sydney=new LatLng(-34, 151);
        mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("marker in sidney"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10.2f));

    }


    @Override
    public void onLocationChanged(Location location) {
        if(location!=null){
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
            Toast.makeText(getActivity(), ""+latitude, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
