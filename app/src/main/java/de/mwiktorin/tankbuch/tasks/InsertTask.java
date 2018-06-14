package de.mwiktorin.tankbuch.tasks;

import android.content.Context;
import android.os.AsyncTask;

import de.mwiktorin.tankbuch.database.AppDatabase;
import de.mwiktorin.tankbuch.database.Refuel;

public class InsertTask extends AsyncTask<Refuel, Void, Long> {

    private Context context;

    public InsertTask(Context context) {
        this.context = context;
    }

    @Override
    protected Long doInBackground(Refuel... refuels) {
        long result = AppDatabase.getInstance(context).refuelDao().insertRefuel(refuels[0]);
        context = null;
        return result;
    }

}
