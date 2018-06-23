package de.mwiktorin.tankbuch.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.mwiktorin.tankbuch.Constants;
import de.mwiktorin.tankbuch.R;
import de.mwiktorin.tankbuch.Utils;
import de.mwiktorin.tankbuch.database.Refuel;

public class RefuelViewHolder extends RecyclerView.ViewHolder {
    private TextView date;
    private TextView milage;
    //private TextView gasStation;
    private TextView pricePerLitre;
    private TextView price;
    private TextView efficiency;
    private Refuel refuel;
    private View view;

    public RefuelViewHolder(View v) {
        super(v);
        date = v.findViewById(R.id.main_refuel_entry_date);
        milage = v.findViewById(R.id.main_refuel_entry_milage);
        //gasStation = v.findViewById(R.id.main_refuel_entry_gas_station);
        pricePerLitre = v.findViewById(R.id.main_refuel_entry_price_per_litre);
        price = v.findViewById(R.id.main_refuel_entry_price);
        efficiency = v.findViewById(R.id.main_refuel_efficiency);
        view = v;
    }

    public void bindTo(Refuel refuel, Refuel previous) {
        this.refuel = refuel;
        date.setText(Constants.DATE_TIME_FORMAT.format(refuel.getDate()));
        milage.setText(String.format(view.getContext().getString(R.string.main_milage_format), String.valueOf(refuel.getMilage())));
        //holder.gasStation.setText(refuel.getGasStationId() + "");
        pricePerLitre.setText(String.format(view.getContext().getString(R.string.main_price_per_liter_format), Utils.roundThree(refuel.getPricePerLitre())));
        String priceText = Utils.round(refuel.getVolume() * refuel.getPricePerLitre());
        price.setText(String.format(view.getContext().getString(R.string.main_total_price_format), priceText));
        String efficiencyText = "";
        if (refuel.isFullTank() && !refuel.isMissedRefuel() && previous != null) {
            efficiencyText = Utils.round((double) refuel.getVolume() / (refuel.getMilage() - previous.getMilage()) * 100.);
            efficiencyText = String.format(view.getContext().getString(R.string.main_efficiency_format), efficiencyText);
        } else if (!refuel.isFullTank()) {
            efficiencyText = view.getContext().getString(R.string.main_efficiency_not_full);
        } else if (refuel.isMissedRefuel()) {
            efficiencyText = view.getContext().getString(R.string.main_efficiency_missed_refuel);
        } else {
            efficiencyText = view.getContext().getString(R.string.main_efficiency_first);
        }
        efficiency.setText(efficiencyText);
    }

    public Refuel getRefuel() {
        return refuel;
    }
}