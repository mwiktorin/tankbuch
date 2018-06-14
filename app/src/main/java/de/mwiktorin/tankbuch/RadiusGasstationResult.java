package de.mwiktorin.tankbuch;

public class RadiusGasstationResult {
    /*
    {
    "ok": true,
    "license": "CC BY 4.0 -  https:\/\/creativecommons.tankerkoenig.de",
    "data": "MTS-K",
    "status": "ok",
    "stations": [
        {                                                     Datentyp, Bedeutung
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
            "isOpen": true,                                 - boolean, true, wenn die Tanke zum Zeitpunkt der
                                                              Abfrage offen hat, sonst false
            "houseNumber": "2",                             - String, Hausnummer
            "postCode": 10407                               - integer, PLZ
        },
        ... weitere Tankstellen
    ]
}
    */
    public String ok;
    public String license;
    public String data;
    public String status;
    public Station[] stations;

    class Station {
        public String id;
        public String name;
        public String brand;
        public String street;
        public String place;
        public double lat;
        public double lng;
        public double dist;
        public double diesel;
        public double e5;
        public double e10;
        public boolean isOpen;
        public String houseNumber;
        public int postCode;
    }
}
