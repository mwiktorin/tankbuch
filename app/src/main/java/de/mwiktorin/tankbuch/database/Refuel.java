package de.mwiktorin.tankbuch.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;

import java.util.Date;
import java.util.Objects;

@Entity(tableName = Refuel.TABLE_NAME)
public class Refuel implements BaseColumns {

    public static final String TABLE_NAME = "refuels";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_MILAGE = "milage";
    public static final String COLUMN_VOLUME = "volume";
    public static final String COLUMN_PRICE_PER_LITRE = "price";
    public static final String COLUMN_GAS_STATION_ID = "gas_station";
    public static final String COLUMN_FULL_TANK = "full_tank";
    public static final String COLUMN_MISSED_REFUEL = "missed_refuel";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = BaseColumns._ID, index = true)
    private int id;

    @ColumnInfo(name = COLUMN_DATE)
    private Date date;

    @ColumnInfo(name = COLUMN_MILAGE)
    private long milage;

    @ColumnInfo(name = COLUMN_VOLUME)
    private double volume;

    @ColumnInfo(name = COLUMN_PRICE_PER_LITRE)
    private double pricePerLitre;

    @ColumnInfo(name = COLUMN_GAS_STATION_ID)
    private long gasStationId;

    @ColumnInfo(name = COLUMN_FULL_TANK)
    private boolean fullTank;

    @ColumnInfo(name = COLUMN_MISSED_REFUEL)
    private boolean missedRefuel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getMilage() {
        return milage;
    }

    public void setMilage(long milage) {
        this.milage = milage;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public double getPricePerLitre() {
        return pricePerLitre;
    }

    public void setPricePerLitre(double pricePerLitre) {
        this.pricePerLitre = pricePerLitre;
    }

    public long getGasStationId() {
        return gasStationId;
    }

    public void setGasStationId(long gasStationId) {
        this.gasStationId = gasStationId;
    }

    public boolean isFullTank() {
        return fullTank;
    }

    public void setFullTank(boolean fullTank) {
        this.fullTank = fullTank;
    }

    public boolean isMissedRefuel() {
        return missedRefuel;
    }

    public void setMissedRefuel(boolean missedRefuel) {
        this.missedRefuel = missedRefuel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Refuel refuel = (Refuel) o;

        if (id != refuel.id) return false;
        if (milage != refuel.milage) return false;
        if (Double.compare(refuel.volume, volume) != 0) return false;
        if (Double.compare(refuel.pricePerLitre, pricePerLitre) != 0) return false;
        if (gasStationId != refuel.gasStationId) return false;
        if (fullTank != refuel.fullTank) return false;
        if (missedRefuel != refuel.missedRefuel) return false;
        return date.equals(refuel.date);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + date.hashCode();
        result = 31 * result + (int) (milage ^ (milage >>> 32));
        temp = Double.doubleToLongBits(volume);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(pricePerLitre);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (int) (gasStationId ^ (gasStationId >>> 32));
        result = 31 * result + (fullTank ? 1 : 0);
        result = 31 * result + (missedRefuel ? 1 : 0);
        return result;
    }
}