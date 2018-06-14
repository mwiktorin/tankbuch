package de.mwiktorin.tankbuch.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;

import java.util.Date;

@Entity(tableName = GasstationBrand.TABLE_NAME)
public class GasstationBrand implements BaseColumns {

    public static final String TABLE_NAME = "gasstation_brand";
    public static final String COLUMN_NAME = "name";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = BaseColumns._ID, index = true)
    private int id;

    @ColumnInfo(name = COLUMN_NAME)
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}