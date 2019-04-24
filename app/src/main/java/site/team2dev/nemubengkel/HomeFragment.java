package site.team2dev.nemubengkel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeFragment extends Fragment implements
        OnMapReadyCallback{

    GoogleMap mGoogleMap;
    MapView mapView;
    View mView;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.home_fragment, container, false);
        return mView;
    }

    @Override
    public void onViewCreated( View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView=(MapView) mView.findViewById(R.id.peta);
        if(mapView!=null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
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


}
