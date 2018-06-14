package de.mwiktorin.tankbuch.tasks;

import android.content.Context;
import android.os.AsyncTask;

import de.mwiktorin.tankbuch.database.AppDatabase;
import de.mwiktorin.tankbuch.database.Refuel;

public class DeleteTask extends AsyncTask<Integer, Void, Long> {

    private Context context;

    public DeleteTask(Context context) {
        this.context = context;
    }

    @Override
    protected Long doInBackground(Integer... ids) {
        long result = AppDatabase.getInstance(context).refuelDao().deleteById(ids[0]);
        context = null;
        return result;
    }

}
