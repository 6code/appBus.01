package br.com.prottipo01.edu.prottipo_01.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by edu on 27/08/17.
 */

public class loadMarkers {

    private String parada;
    private double latitude;
    private double longitude;

    public loadMarkers() {
    }

    public String getParada() {
        return parada;
    }

    public void setParada(String parada) {
        this.parada = parada;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("parada", parada);
        result.put("latitude", latitude);
        result.put("longitude", longitude);

        return result;
    }
}
