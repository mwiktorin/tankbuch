package de.mwiktorin.tankbuch.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface GasstationBrandDao {

    @Query("SELECT * FROM " + GasstationBrand.TABLE_NAME)
    LiveData<List<GasstationBrand>> selectAll();

    @Query("SELECT * FROM " + GasstationBrand.TABLE_NAME + " WHERE " + GasstationBrand._ID + " = :id")
    LiveData<GasstationBrand> selectById(long id);

    @Query("SELECT COUNT(*) FROM " + GasstationBrand.TABLE_NAME)
    LiveData<Integer> count();

    @Insert
    long insert(GasstationBrand gasstationBrand);

    @Query("DELETE FROM " + GasstationBrand.TABLE_NAME + " WHERE " + GasstationBrand._ID + " = :id")
    int deleteById(long id);

    @Update
    int update(GasstationBrand gasstationBrand);
}
