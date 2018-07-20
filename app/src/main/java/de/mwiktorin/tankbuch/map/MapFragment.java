package de.mwiktorin.tankbuch.map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import de.mwiktorin.tankbuch.GsonRequest;
import de.mwiktorin.tankbuch.R;
import de.mwiktorin.tankbuch.RadiusGasstationResult;

public class MapFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_CODE = 12;
    private static final long LOCATION_UPDATE_TIME = 5000;
    private static final float LOCATION_UPDATE_DISTANCE = 5;
    private static final float INIT_ZOOM_FACTOR = 12;
    private static final double SEARCH_RADIUS = 25;
    private GoogleMap map;
    private MapView mapView;
    private LocationManager locationManager;
    private ClusterManager<MapStationItem> clusterManager;
    private RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestQueue = Volley.newRequestQueue(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.map);
        mapView.onCreate(null);
        mapView.getMapAsync(googleMap -> {
            map = googleMap;
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            clusterManager = new ClusterManager<>(getContext(), map);
            clusterManager.setRenderer(new MyClusterRenderer(getContext(), map, clusterManager));
            map.setOnCameraIdleListener(clusterManager);
            map.setOnMarkerClickListener(clusterManager);
            requestLocationUpdates();
        });


        return view;
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest
                    .permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE);
        } else {
            map.setMyLocationEnabled(true);

            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()),
                    INIT_ZOOM_FACTOR));
            locationUpdate(lastKnownLocation);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_UPDATE_TIME, LOCATION_UPDATE_DISTANCE, new
                    LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {
                            locationUpdate(location);
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
                    });
        }
    }

    public void locationUpdate(Location location) {
        String apikey = getString(R.string.tankerkoenig_key);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.shared_prefs_filename), getContext().MODE_PRIVATE);
        int gasTypeIndex = sharedPreferences.getInt(getString(R.string.preference_key_gas_type), -1);
        gasTypeIndex = -1; //TODO all works, specific type gives different response
        String type = gasTypeIndex == -1 ? getResources().getString(R.string.gas_type_tankerkoenig_all) : getResources().getStringArray(R.array
                .gas_types_tankerkoenig)[gasTypeIndex];

        String tankerkoenigApiKey = getString(R.string.tankerkoenig_url_param_api_key);
        String tankerkoenigRadius = getString(R.string.tankerkoenig_url_param_radius);
        String tankerkoenigType = getString(R.string.tankerkoenig_url_param_type);
        String tankerkoenigLat = getString(R.string.tankerkoenig_url_param_lat);
        String tankerkoenigLng = getString(R.string.tankerkoenig_url_param_lng);
        String tankerkoenigSort = getString(R.string.tankerkoenig_url_param_sort);
        Uri requestUri = Uri.parse(getString(R.string.tankerkoenig_url))
                .buildUpon()
                .appendQueryParameter(tankerkoenigLat, String.valueOf(location.getLatitude()))
                .appendQueryParameter(tankerkoenigLng, String.valueOf(location.getLongitude()))
                .appendQueryParameter(tankerkoenigType, type).appendQueryParameter(tankerkoenigRadius, String.valueOf(SEARCH_RADIUS))
                .appendQueryParameter(tankerkoenigSort, getString(R.string.tankerkoenig_sort_dist))
                .appendQueryParameter(tankerkoenigApiKey, apikey)
                .build();
        GsonRequest<RadiusGasstationResult> request = new GsonRequest<>(requestUri.toString(), RadiusGasstationResult.class, null, response -> {
            //TODO handle empty response
            for (RadiusGasstationResult.Station station : response.stations) {
                clusterManager.addItem(new MapStationItem(new LatLng(station.lat, station.lng), station.e5 + " €", station.name, station.e5));
            }
        }, error -> System.out.println("ERROR")); //TODO handle error
        requestQueue.add(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            for (int x : grantResults) {
                if (x == PackageManager.PERMISSION_DENIED) {
                    return;
                }
            }
            requestLocationUpdates();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mapView != null) {
            mapView.onStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mapView != null) {
            mapView.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }
}
