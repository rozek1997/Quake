package com.example.marek.earthquakeapp;

/**
 * Created by marek on 24.06.2018.
 */

/**
 * class store information about quake
 */
public class Earthquake {

    private double mag; //magnitude of quake
    private String location; //location uf quake
    private long date; //date od quake
    private String urlAdress;  //urladress on web page with more information

    public Earthquake(double mag, String location, long date, String urlAdress) {
        this.mag = mag;
        this.location = location;
        this.date = date;
        this.urlAdress = urlAdress;
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
