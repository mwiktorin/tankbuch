package de.mwiktorin.tankbuch.main;

import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import de.mwiktorin.tankbuch.addrefuel.AddRefuelActivity;
import de.mwiktorin.tankbuch.tasks.DeleteTask;
import de.mwiktorin.tankbuch.FirstStartActivity;
import de.mwiktorin.tankbuch.tasks.InsertTask;
import de.mwiktorin.tankbuch.R;
import de.mwiktorin.tankbuch.database.AppDatabase;
import de.mwiktorin.tankbuch.database.Refuel;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RefuelListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar actionbar = (Toolbar) findViewById(R.id.main_actionbar);
        setSupportActionBar(actionbar);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_prefs_filename), MODE_PRIVATE);
        if (sharedPreferences.getBoolean(getString(R.string.preference_key_first_start), true)) {
            Intent intent = new Intent(this, FirstStartActivity.class);
            startActivity(intent);
        }

        setupRecyclerView();

        LiveData<List<Refuel>> refuelLiveData = AppDatabase.getInstance(this).refuelDao().selectAll();
        refuelLiveData.observe(this, (list) -> {
            adapter.submitList(list);
        });

        FloatingActionButton addButton = findViewById(R.id.main_add_button);
        addButton.setOnClickListener(view -> {
            Intent addRefuelIntent = new Intent(MainActivity.this, AddRefuelActivity.class);
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
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.main_recycler_view);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                Refuel deletedItem = ((RefuelViewHolder)viewHolder).getRefuel();
                new DeleteTask(getApplicationContext()).execute(deletedItem.getId());
                Snackbar.make(recyclerView, R.string.main_deleted, Snackbar.LENGTH_LONG).setAction(R.string.main_undo, v -> {
                    new InsertTask(getApplicationContext()).execute(deletedItem);
                }).show();
            }

        };
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





}
