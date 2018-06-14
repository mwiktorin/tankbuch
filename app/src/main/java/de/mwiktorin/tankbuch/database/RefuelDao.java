package de.mwiktorin.tankbuch.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.List;

@Dao
public interface RefuelDao {

    @Query("SELECT * FROM " + Refuel.TABLE_NAME + " ORDER BY " + Refuel.COLUMN_DATE + " DESC")
    LiveData<List<Refuel>> selectAll();

    @Query("SELECT * FROM " + Refuel.TABLE_NAME + " WHERE " + Refuel._ID + " = :id")
    LiveData<Refuel> selectById(long id);

    @Query("SELECT COUNT(*) FROM " + Refuel.TABLE_NAME)
    LiveData<Integer> count();

    @Insert
    long insertRefuel(Refuel refuel);

    @Query("DELETE FROM " + Refuel.TABLE_NAME + " WHERE " + Refuel._ID + " = :id")
    int deleteById(int id);

    @Update
    int update(Refuel refuel);
}
