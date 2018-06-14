package de.mwiktorin.tankbuch.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import java.util.Date;

@Database(entities = {Refuel.class, Gasstation.class, GasstationBrand.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "tankbuch.db";

    private static AppDatabase sInstance;

    public abstract RefuelDao refuelDao();
    public abstract GasstationDao gasstationDao();
    public abstract GasstationBrandDao gasstationBrandDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                    .build();
        }
        return sInstance;
    }

}
