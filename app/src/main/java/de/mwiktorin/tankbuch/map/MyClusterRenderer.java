package de.mwiktorin.tankbuch.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.android.ui.SquareTextView;

import java.util.HashMap;
import java.util.Map;

import de.mwiktorin.tankbuch.R;
import de.mwiktorin.tankbuch.Utils;

public class MyClusterRenderer extends DefaultClusterRenderer<MapStationItem> {

    private final IconGenerator mClusterIconGenerator;
    private final IconGenerator mItemIconGenerator;
    private final float mDensity;
    private Context context;
    private GoogleMap map;
    private Map<Double, BitmapDescriptor> mIcons = new HashMap<>();
    private ShapeDrawable mColoredCircleBackground;
    private double medium = 100;
    private double high = 200;
    private double best = 200;

    public MyClusterRenderer(Context context, GoogleMap map, ClusterManager<MapStationItem> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
        this.map = map;

        this.mItemIconGenerator = new IconGenerator(context);

        this.mClusterIconGenerator = new IconGenerator(context);
        this.mClusterIconGenerator.setContentView(this.makeSquareTextView(context));
        this.mClusterIconGenerator.setContentPadding(10, 10, 10, 10);
        this.mClusterIconGenerator.setBackground(this.makeClusterBackground());
        //this.mClusterIconGenerator.setStyle(IconGenerator.STYLE_GREEN);
        this.mDensity = context.getResources().getDisplayMetrics().density;
        setMinClusterSize(1);
    }

    @Override
    protected void onBeforeClusterItemRendered(MapStationItem mapStationItem, MarkerOptions markerOptions) {
        mItemIconGenerator.setStyle(getStyle(mapStationItem.getPrice()));
        Bitmap icon = mItemIconGenerator.makeIcon(mapStationItem.getSnippet());
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }


    @Override
    protected void onBeforeClusterRendered(Cluster<MapStationItem> cluster, MarkerOptions markerOptions) {
        double minPrice = Double.MAX_VALUE;
        for (MapStationItem item : cluster.getItems()) {
            minPrice = item.getPrice() < minPrice ? item.getPrice() : minPrice;
        }
        mClusterIconGenerator.setStyle(getStyle(minPrice));
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromBitmap(this.mClusterIconGenerator.makeIcon(cluster.getSize() + " ab\n" + Utils
                .round(minPrice - 0.005) + "€"));

        markerOptions.icon(descriptor);
    }

    public void setPriceRanges(double best, double medium, double high) {
        this.best = best;
        this.medium = medium;
        this.high = high;
    }

    protected int getStyle(double price) {
        if(price == best) {
            return IconGenerator.STYLE_BLUE;
        }
        if(price < medium) {
            return IconGenerator.STYLE_GREEN;
        }
        if (price < high) {
            return IconGenerator.STYLE_ORANGE;
        }
        return IconGenerator.STYLE_RED;
    }

    private LayerDrawable makeClusterBackground() {
        this.mColoredCircleBackground = new ShapeDrawable(new OvalShape());
        ShapeDrawable outline = new ShapeDrawable(new OvalShape());
        outline.getPaint().setColor(-2130706433);
        LayerDrawable background = new LayerDrawable(new Drawable[]{outline, this.mColoredCircleBackground});
        int strokeWidth = (int) (this.mDensity * 3.0F);
        background.setLayerInset(1, strokeWidth, strokeWidth, strokeWidth, strokeWidth);
        return background;
    }

    private SquareTextView makeSquareTextView(Context context) {
        SquareTextView squareTextView = new SquareTextView(context);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -2);
        squareTextView.setLayoutParams(layoutParams);
        squareTextView.setId(com.google.maps.android.R.id.amu_text);
        int twelveDpi = (int) (12.0F * this.mDensity);
        squareTextView.setPadding(twelveDpi, twelveDpi, twelveDpi, twelveDpi);
        return squareTextView;
    }
}
