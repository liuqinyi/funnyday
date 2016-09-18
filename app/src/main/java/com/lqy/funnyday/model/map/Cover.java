package com.lqy.funnyday.model.map;

import java.io.Serializable;

/**
 * Created by mrliu on 16-9-17.
 */
public class Cover implements Serializable {
    private double latitude;
    private double longitude;
    private int imgId;
    private String name;
    private int zan;

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
}
