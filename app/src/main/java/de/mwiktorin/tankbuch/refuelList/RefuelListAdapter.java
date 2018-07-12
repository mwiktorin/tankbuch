package de.mwiktorin.tankbuch.refuelList;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.mwiktorin.tankbuch.R;
import de.mwiktorin.tankbuch.database.Refuel;

public class RefuelListAdapter extends ListAdapter<Refuel, RefuelViewHolder> {


    public RefuelListAdapter(@NonNull DiffUtil.ItemCallback<Refuel> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public RefuelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_refuel_entry, parent, false);
        return new RefuelViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RefuelViewHolder holder, int position) {
        Refuel item = getItem(position);
        if(position < getItemCount() - 1){
            holder.bindTo(item, getItem(position + 1));
        } else {
            holder.bindTo(item, null);
        }
    }

    public Refuel getRefuel(int position) {
        return getItem(position);
    }
}