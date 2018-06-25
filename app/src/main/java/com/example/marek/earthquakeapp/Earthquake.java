package com.example.marek.earthquakeapp;

/**
 * Created by marek on 24.06.2018.
 */

public class Earthquake {

    private double mag;
    private String location;
    private long date;
    private String urlAdress;

    public Earthquake(double mag, String location, long date, String urlAdress) {
        this.mag = mag;
        this.location = location;
        this.date = date;
        this.urlAdress=urlAdress;
    }

    public double getMag() {
        return mag;
    }

    public void setMag(double mag) {
        this.mag = mag;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getUrlAdress() {
        return urlAdress;
    }

    public void setUrlAdress(String urlAdress) {
        this.urlAdress = urlAdress;
    }
}
