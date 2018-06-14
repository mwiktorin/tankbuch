package de.mwiktorin.tankbuch.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.provider.BaseColumns;

import java.util.Date;

@Entity(tableName = Gasstation.TABLE_NAME)
public class Gasstation implements BaseColumns {
    /*
    tankerkoenig API
    "id": "474e5046-deaf-4f9b-9a32-9797b778f047",   - UUID, eindeutige Tankstellen-ID
    "name": "TOTAL BERLIN",                         - String, Name
    "brand": "TOTAL",                               - String, Marke
    "street": "MARGARETE-SOMMER-STR.",              - String, Straße
    "place": "BERLIN",                              - String, Ort
    "lat": 52.53083,                                - float, geographische Breite
    "lng": 13.440946,                               - float, geographische Länge
    "dist": 1.1,                                    - float, Entfernung zum Suchstandort in km
    "diesel": 1.109,                                \
    "e5": 1.339,                                     - float, Spritpreise in Euro
    "e10": 1.319,                                   /
    "isOpen": true,                                 - boolean, true, wenn die Tanke zum Zeitpunkt derAbfrage offen hat, sonst false
    "houseNumber": "2",                             - String, Hausnummer
    "postCode": 10407                               - integer, PLZ
    */
    public static final String TABLE_NAME = "gasstations";
    public static final String COLUMN_API_ID = "api_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_BRAND_ID = "brand_id";
    public static final String COLUMN_STREET = "street";
    public static final String COLUMN_HOUSE_NUMBER = "house_number";
    public static final String COLUMN_PLACE = "place";
    public static final String COLUMN_POSTAL_CODE = "postal_code";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LON = "lon";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = BaseColumns._ID, index = true)
    private int id;

    @ColumnInfo(name = COLUMN_API_ID)
    private String apiId;

    @ColumnInfo(name = COLUMN_NAME)
    private String name;

    @ColumnInfo(name = COLUMN_BRAND_ID)
    @ForeignKey(entity = GasstationBrand.class, parentColumns = GasstationBrand._ID, onDelete = ForeignKey.SET_NULL, childColumns = COLUMN_BRAND_ID)
    private int brandId;

    @ColumnInfo(name = COLUMN_STREET)
    private String street;

    @ColumnInfo(name = COLUMN_HOUSE_NUMBER)
    private int houseNumber;

    @ColumnInfo(name = COLUMN_PLACE)
    private String place;

    @ColumnInfo(name = COLUMN_POSTAL_CODE)
    private String postalCode;

    @ColumnInfo(name = COLUMN_LAT)
    private double lat;

    @ColumnInfo(name = COLUMN_LON)
    private double lon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}