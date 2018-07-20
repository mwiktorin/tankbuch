package de.mwiktorin.tankbuch.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MapStationItem implements ClusterItem{

    private LatLng position;
    private String title;
    private String snippet;
    private double price;

    public MapStationItem(LatLng position, String title, String snippet, double price) {
        this.position = position;
        this.title = title;
        this.snippet = snippet;
        this.price = price;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return snippet;
    }

    public double getPrice() {
        return price;
    }
}
