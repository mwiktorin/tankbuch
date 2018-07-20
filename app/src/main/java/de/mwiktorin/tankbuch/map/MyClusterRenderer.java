package de.mwiktorin.tankbuch.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.android.ui.SquareTextView;

import java.util.HashMap;
import java.util.Map;

public class MyClusterRenderer extends DefaultClusterRenderer<MapStationItem>{

    private GoogleMap map;

    private Map<Double,BitmapDescriptor> mIcons = new HashMap<>();
    private ShapeDrawable mColoredCircleBackground;
    private final IconGenerator mIconGenerator;
    private final float mDensity;

    public MyClusterRenderer(Context context, GoogleMap map, ClusterManager<MapStationItem> clusterManager) {
        super(context, map, clusterManager);
        this.map = map;

        this.mIconGenerator = new IconGenerator(context);
        this.mIconGenerator.setContentView(this.makeSquareTextView(context));
        this.mIconGenerator.setTextAppearance(com.google.maps.android.R.style.amu_ClusterIcon_TextAppearance);
        this.mIconGenerator.setContentPadding(10, 10, 10, 10);
        this.mIconGenerator.setBackground(this.makeClusterBackground());
        this.mDensity = context.getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<MapStationItem> cluster, MarkerOptions markerOptions) {
        double minPrice = Double.MAX_VALUE;
        for (MapStationItem item : cluster.getItems()) {
            minPrice = item.getPrice() < minPrice ? item.getPrice() : minPrice;
        }
        BitmapDescriptor descriptor = this.mIcons.get(minPrice);
        if (descriptor == null) {
            this.mColoredCircleBackground.getPaint().setColor(this.getColor(minPrice));
            descriptor = BitmapDescriptorFactory.fromBitmap(this.mIconGenerator.makeIcon(cluster.getSize() + " ab\n" + minPrice + " €"));
            this.mIcons.put(minPrice, descriptor);
        }

        markerOptions.icon(descriptor);
    }

    protected int getColor(double minPrice) {
        float hueRange = 220.0F;
        float sizeRange = 300.0F;
        float size = Math.min((float)minPrice, 300.0F);
        float hue = (300.0F - size) * (300.0F - size) / 90000.0F * 220.0F;
        return Color.HSVToColor(new float[]{hue, 1.0F, 0.6F});
    }

    private LayerDrawable makeClusterBackground() {
        this.mColoredCircleBackground = new ShapeDrawable(new OvalShape());
        ShapeDrawable outline = new ShapeDrawable(new OvalShape());
        outline.getPaint().setColor(-2130706433);
        LayerDrawable background = new LayerDrawable(new Drawable[]{outline, this.mColoredCircleBackground});
        int strokeWidth = (int)(this.mDensity * 3.0F);
        background.setLayerInset(1, strokeWidth, strokeWidth, strokeWidth, strokeWidth);
        return background;
    }

    private SquareTextView makeSquareTextView(Context context) {
        SquareTextView squareTextView = new SquareTextView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -2);
        squareTextView.setLayoutParams(layoutParams);
        squareTextView.setId(com.google.maps.android.R.id.amu_text);
        int twelveDpi = (int)(12.0F * this.mDensity);
        squareTextView.setPadding(twelveDpi, twelveDpi, twelveDpi, twelveDpi);
        return squareTextView;
    }
}
