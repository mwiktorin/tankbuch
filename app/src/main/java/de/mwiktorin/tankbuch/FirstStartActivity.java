package de.mwiktorin.tankbuch;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class FirstStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_start);

        Spinner gasType = findViewById(R.id.first_start_gas_type_spinner);
        Button start = findViewById(R.id.first_start_start_button);
        start.setOnClickListener(v -> {
            SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.shared_prefs_filename), MODE_PRIVATE).edit();
            editor.putBoolean(getString(R.string.preference_key_first_start), false);
            editor.putInt(getString(R.string.preference_key_gas_type), gasType.getSelectedItemPosition());
            editor.apply();
            finish();
        });
    }
}
