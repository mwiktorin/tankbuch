package de.mwiktorin.tankbuch.map;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import de.mwiktorin.tankbuch.Utils;

public class MapStationItem implements ClusterItem {

    private LatLng position;
    private String name;
    private double price;

    public MapStationItem(LatLng position, String name, double price) {
        this.position = position;
        this.name = name;
        this.price = price;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public String getTitle() {
        return name;
    }

    @Override
    public String getSnippet() {
        return Utils.round(price - 0.005) + "€";
    }

    public double getPrice() {
        return price;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapStationItem that = (MapStationItem) o;

        if (!position.equals(that.position)) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = position.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}
