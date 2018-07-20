package de.mwiktorin.tankbuch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import de.mwiktorin.tankbuch.map.MapFragment;
import de.mwiktorin.tankbuch.refuelList.RefuelList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar actionbar = (Toolbar) findViewById(R.id.main_actionbar);
        setSupportActionBar(actionbar);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.main_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener
                (item -> {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.tab_menu_charts:
                            selectedFragment = new RefuelList();
                            break;
                        case R.id.tab_menu_refuel_list:
                            selectedFragment = new RefuelList();
                            break;
                        case R.id.tab_menu_map:
                            selectedFragment = new MapFragment();
                            break;
                    }
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_frame_layout, selectedFragment);
                    transaction.commit();
                    return true;
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame_layout, new RefuelList());
        transaction.commit();
        bottomNavigationView.setSelectedItemId(R.id.tab_menu_refuel_list);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_prefs_filename), MODE_PRIVATE);
        if (sharedPreferences.getBoolean(getString(R.string.preference_key_first_start), true)) {
            Intent intent = new Intent(this, FirstStartActivity.class);
            startActivity(intent);
        }
    }


}
