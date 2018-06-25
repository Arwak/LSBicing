import org.omg.CORBA.INTERNAL;

import java.util.ArrayList;

/**
 * Created by xavierromacastells on 17/12/16.
 */
public class Estacio {
    private String id;
    private String type;
    private String latitude;
    private String longitude;
    private String streetName;
    private String streetNumber;
    private String altitude;
    private String slots;
    private String bikes;
    private String nearbyStations;
    private String status;

    public int getId() {

        return Integer.parseInt(id);
    }

    public String getType() {
        return type;
    }

    public float getLatitude() {
        return Float.parseFloat(latitude);
    }

    public float getLongitude() {
        return Float.parseFloat(longitude);
    }

    public String getStreetName() {
        return streetName;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public int getAltitude() {
        return Integer.parseInt(altitude);
    }

    public String getSlots() {
        return slots;
    }

    public int getBikes() {
        return Integer.parseInt(bikes);
    }

    public String getNearbyStations() {

        return nearbyStations;
    }

    public String getStatus() {

        return status;
    }

}
