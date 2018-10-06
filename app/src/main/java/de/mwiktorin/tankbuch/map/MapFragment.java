package de.mwiktorin.tankbuch.map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import de.mwiktorin.tankbuch.GsonRequest;
import de.mwiktorin.tankbuch.R;
import de.mwiktorin.tankbuch.RadiusGasstationResult;

public class MapFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_CODE = 12;
    private static final long LOCATION_UPDATE_TIME = 5000;
    private static final float LOCATION_UPDATE_DISTANCE = 5;
    private static final float INIT_ZOOM_FACTOR = 12;
    private static final double SEARCH_RADIUS = 25; // in km
    private static final double NO_RELOAD_DISTANCE = 10000; // in m
    private GoogleMap map;
    private MapView mapView;
    private LocationManager locationManager;
    private ClusterManager<MapStationItem> clusterManager;
    private MyClusterRenderer clusterRenderer;
    private RequestQueue requestQueue;
    private HashSet<RadiusGasstationResult.Station> stations = new HashSet<>();
    private List<Location> loadedLocations = new ArrayList<>();


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
            clusterRenderer = new MyClusterRenderer(getContext(), map, clusterManager);
            clusterManager.setRenderer(clusterRenderer);
            map.setOnCameraIdleListener(() -> {
                clusterManager.onCameraIdle();
                Location location = new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(map.getCameraPosition().target.latitude);
                location.setLongitude(map.getCameraPosition().target.longitude);
                locationUpdate(location);
            });
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
        if (!isLocationLoaded(location)) {
            GsonRequest<RadiusGasstationResult> request = new GsonRequest<>(requestUri.toString(), RadiusGasstationResult.class, null, response -> {
                //TODO handle empty response
                loadedLocations.add(location);
                for (RadiusGasstationResult.Station station : response.stations) {
                    if (stations.contains(station) || station.e5 < 0.1) {
                        continue;
                    }
                    stations.add(station);
                    clusterManager.addItem(new MapStationItem(new LatLng(station.lat, station.lng), station.name, station.e5));
                }
                List<Double> prices = new ArrayList<>();
                for(RadiusGasstationResult.Station s : stations) {
                    prices.add(s.e5);
                }
                Collections.sort(prices);
                clusterRenderer.setPriceRanges(prices.get(0), prices.get((int) (prices.size() * 0.33)), prices.get((int) (prices.size() * 0.66)));
                clusterManager.cluster();
            }, error -> {
                System.out.println("ERROR, request was " + requestUri.toString());
            }); //TODO handle error
            requestQueue.add(request);
        }
    }

    private boolean isLocationLoaded(Location location) {
        for (Location l : loadedLocations) {
            if (l.distanceTo(location) < NO_RELOAD_DISTANCE) {
                return true;
            }
        }
        return false;
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
