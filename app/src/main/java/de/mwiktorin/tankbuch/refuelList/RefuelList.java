package de.mwiktorin.tankbuch.refuelList;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import de.mwiktorin.tankbuch.addrefuel.AddRefuelActivity;
import de.mwiktorin.tankbuch.tasks.DeleteTask;
import de.mwiktorin.tankbuch.FirstStartActivity;
import de.mwiktorin.tankbuch.tasks.InsertTask;
import de.mwiktorin.tankbuch.R;
import de.mwiktorin.tankbuch.database.AppDatabase;
import de.mwiktorin.tankbuch.database.Refuel;

public class RefuelList extends Fragment {

    private RecyclerView recyclerView;
    private RefuelListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refuel_list, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.shared_prefs_filename), getContext().MODE_PRIVATE);
        if (sharedPreferences.getBoolean(getString(R.string.preference_key_first_start), true)) {
            Intent intent = new Intent(getContext(), FirstStartActivity.class);
            startActivity(intent);
        }

        setupRecyclerView(view);

        LiveData<List<Refuel>> refuelLiveData = AppDatabase.getInstance(getContext()).refuelDao().selectAll();
        refuelLiveData.observe(this, (list) -> {
            adapter.submitList(list);
        });

        FloatingActionButton addButton = view.findViewById(R.id.main_add_button);
        addButton.setOnClickListener(v -> {
            Intent addRefuelIntent = new Intent(getContext(), AddRefuelActivity.class);
            if (adapter.getItemCount() > 0 && adapter.getRefuel(0) != null ) {
                addRefuelIntent.putExtra(AddRefuelActivity.BUNDLE_PARAM_MILAGE, adapter.getRefuel(0).getMilage());
            }
            startActivity(addRefuelIntent);
        });

        /*
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://creativecommons.tankerkoenig.de/json/list.php";
        GsonRequest<RadiusGasstationResult> request = new GsonRequest<>(url, RadiusGasstationResult.class, null, new Response.Listener<RadiusGasstationResult>() {
            @Override
            public void onResponse(RadiusGasstationResult response) {
                System.out.println("SUCCESS");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ERROR");
            }
        });
        queue.add(request);
        */

        return view;
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.main_recycler_view);

        DiffUtil.ItemCallback<Refuel> diffCallback =
                new DiffUtil.ItemCallback<Refuel>() {
                    @Override
                    public boolean areItemsTheSame(@NonNull Refuel oldRefuel, @NonNull Refuel newRefuel) {
                        return oldRefuel.getId() == newRefuel.getId();
                    }

                    @Override
                    public boolean areContentsTheSame(@NonNull Refuel oldRefuel, @NonNull Refuel newRefuel) {
                        return oldRefuel.equals(newRefuel);
                    }
                };
        adapter = new RefuelListAdapter(diffCallback);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Refuel deletedItem = ((RefuelViewHolder)viewHolder).getRefuel();
                new DeleteTask(getContext()).execute(deletedItem.getId());
                Snackbar.make(recyclerView, R.string.main_deleted, Snackbar.LENGTH_LONG).setAction(R.string.main_undo, v -> {
                    new InsertTask(getContext()).execute(deletedItem);
                }).show();
            }

        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }
}