package de.mwiktorin.tankbuch.addrefuel;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import de.mwiktorin.tankbuch.Constants;
import de.mwiktorin.tankbuch.R;
import de.mwiktorin.tankbuch.Utils;
import de.mwiktorin.tankbuch.tasks.InsertTask;
import de.mwiktorin.tankbuch.database.Refuel;

public class AddRefuelActivity extends AppCompatActivity {

    public static final String BUNDLE_PARAM_MILAGE = "milage";

    private EditText dateEditText;
    private EditText timeEditText;
    private EditText milageEditText;
    //private Spinner gasStationSpinner;
    private EditText pricePerLitreEditText;
    private CheckBox fullTankCheckBox;
    private CheckBox missedRefuelCheckBox;
    private EditText priceEditText;
    private EditText volumeEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_refuel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.add_refuel_title);

        dateEditText = findViewById(R.id.add_refuel_date);
        timeEditText = findViewById(R.id.add_refuel_time);
        milageEditText = findViewById(R.id.add_refuel_milage);
        //gasStationSpinner = findViewById(R.id.add_refuel_gas_station_id);
        pricePerLitreEditText = findViewById(R.id.add_refuel_price_per_litre);
        fullTankCheckBox = findViewById(R.id.add_refuel_full_tank);
        missedRefuelCheckBox = findViewById(R.id.add_refuel_missed_refuel);
        priceEditText = findViewById(R.id.add_refuel_price);
        volumeEditText = findViewById(R.id.add_refuel_volume);
        saveButton = findViewById(R.id.add_refuel_save);

        Date now = new Date();
        dateEditText.setText(Constants.DATE_FORMAT.format(now));
        timeEditText.setText(Constants.TIME_FORMAT.format(now));

        dateEditText.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(AddRefuelActivity.this, (view, year, month, dayOfMonth) -> {
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                dateEditText.setText(Constants.DATE_FORMAT.format(c.getTime()));
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            dialog.show();
        });

        timeEditText.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            TimePickerDialog dialog = new TimePickerDialog(AddRefuelActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    c.set(Calendar.MINUTE, minute);
                    timeEditText.setText(Constants.TIME_FORMAT.format(c.getTime()));
                }
            }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);
            dialog.show();
        });

        long milageExtra = getIntent().getLongExtra(BUNDLE_PARAM_MILAGE, -1);
        if (milageExtra != -1) {
            milageEditText.setText(milageExtra + "");
            int start = Math.max(0, (int) Math.round(Math.log10(milageExtra) + 0.5) - 3);
            milageEditText.setSelection(start, milageEditText.getText().length());
        }
        milageEditText.requestFocus();

        milageEditText.addTextChangedListener(new SimpleTextWatcherAfter() {
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Long.parseLong(s.toString());
                } catch (NumberFormatException e) {
                    milageEditText.setError(getString(R.string.add_refuel_milage_format_error));
                }
            }
        });

        pricePerLitreEditText.addTextChangedListener(new SimpleTextWatcherAfter() {
            @Override
            public void afterTextChanged(Editable s) {
                if (getCurrentFocus() != pricePerLitreEditText) {
                    return;
                }
                try {
                    double pricePerLitre = Double.parseDouble(s.toString());
                    if (pricePerLitre < 0) {
                        pricePerLitreEditText.setError(getString(R.string.add_refuel_price_negative_error));
                        return;
                    }
                    if (isEditTextValid(priceEditText)) {
                        volumeEditText.setText(Utils.round(Double.parseDouble(priceEditText.getText().toString()) / pricePerLitre));
                    } else {
                        if (isEditTextValid(volumeEditText)) {
                            priceEditText.setText(Utils.round(Double.parseDouble(priceEditText.getText().toString()) * Double.parseDouble(volumeEditText.getText().toString())));
                        }
                    }
                } catch (NumberFormatException e) {
                    pricePerLitreEditText.setError(getString(R.string.add_refuel_price_format_error));
                }
            }
        });

        priceEditText.addTextChangedListener(new SimpleTextWatcherAfter() {
            @Override
            public void afterTextChanged(Editable s) {
                if (getCurrentFocus() != priceEditText) {
                    return;
                }
                try {
                    double price = Double.parseDouble(s.toString());
                    if (price <= 0) {
                        priceEditText.setError(getString(R.string.add_refuel_price_negative_error));
                        return;
                    }
                    if (isEditTextValid(pricePerLitreEditText)) {
                        volumeEditText.setText(Utils.round(price / Double.parseDouble(pricePerLitreEditText.getText().toString())));
                    } else {
                        if (isEditTextValid(volumeEditText)) {
                            pricePerLitreEditText.setText(Utils.roundThree(price / Double.parseDouble(volumeEditText.getText().toString())));
                        }
                    }
                } catch (NumberFormatException e) {
                    priceEditText.setError(getString(R.string.add_refuel_price_format_error));
                }
            }
        });

        volumeEditText.addTextChangedListener(new SimpleTextWatcherAfter() {
            @Override
            public void afterTextChanged(Editable s) {
                if (getCurrentFocus() != volumeEditText) {
                    return;
                }
                try {
                    double volume = Double.parseDouble(s.toString());
                    if (volume <= 0) {
                        volumeEditText.setError(getString(R.string.add_refuel_volume_negative_error));
                        return;
                    }
                    if (isEditTextValid(pricePerLitreEditText)) {
                        priceEditText.setText(Utils.round(volume * Double.parseDouble(pricePerLitreEditText.getText().toString())));
                    } else {
                        if (isEditTextValid(pricePerLitreEditText)) {
                            pricePerLitreEditText.setText(Utils.roundThree(Double.parseDouble(priceEditText.getText().toString()) / volume));
                        }
                    }
                } catch (NumberFormatException e) {
                    volumeEditText.setError(getString(R.string.add_refuel_volume_format_error));
                }
            }
        });

        saveButton.setOnClickListener((view) -> {
            if(!isEditTextValid(milageEditText) || !isEditTextValid(pricePerLitreEditText) || !isEditTextValid(priceEditText) || !isEditTextValid(volumeEditText)) {
                Snackbar.make(view, R.string.add_refuel_invalid_field, Snackbar.LENGTH_LONG).show();
                return;
            }
            try {
                Refuel r = new Refuel();
                Date date = Constants.DATE_FORMAT.parse(dateEditText.getText().toString());
                Date time = Constants.TIME_FORMAT.parse(timeEditText.getText().toString());
                r.setDate(new Date(date.getTime() + time.getTime()));
                r.setFullTank(fullTankCheckBox.isChecked());
                r.setMissedRefuel(missedRefuelCheckBox.isChecked());
                //r.setGasStationId(Long.parseLong(gasStationIdEditText.getText().toString()));
                r.setMilage(Long.parseLong(milageEditText.getText().toString()));
                r.setPricePerLitre(Double.parseDouble(pricePerLitreEditText.getText().toString()));
                r.setVolume(Double.parseDouble(volumeEditText.getText().toString()));

                new InsertTask(getApplicationContext()).execute(r);

                finish();
            } catch (ParseException e) {
                e.printStackTrace();
                Snackbar.make(view, R.string.add_refuel_unexpected_error, Snackbar.LENGTH_LONG).show();
            }

        });
    }

    private boolean isEditTextValid(EditText editText){
        return editText.getError() == null && !editText.getText().toString().isEmpty();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
