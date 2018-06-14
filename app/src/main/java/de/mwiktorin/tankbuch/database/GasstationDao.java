package de.mwiktorin.tankbuch.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface GasstationDao {

    @Query("SELECT * FROM " + Gasstation.TABLE_NAME)
    LiveData<List<Gasstation>> selectAll();

    @Query("SELECT * FROM " + Gasstation.TABLE_NAME + " WHERE " + Gasstation._ID + " = :id")
    LiveData<Gasstation> selectById(long id);

    @Query("SELECT COUNT(*) FROM " + Gasstation.TABLE_NAME)
    LiveData<Integer> count();

    @Insert
    long insert(Gasstation gasstation);

    @Query("DELETE FROM " + Gasstation.TABLE_NAME + " WHERE " + Gasstation._ID + " = :id")
    int deleteById(long id);

    @Update
    int update(Gasstation gasstation);
}
