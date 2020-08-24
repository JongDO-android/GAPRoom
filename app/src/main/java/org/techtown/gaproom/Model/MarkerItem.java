package org.techtown.gaproom.Model;

import java.io.Serializable;

public class MarkerItem implements Serializable {
    private double lat;
    private double lon;
    private String phone;
    private String address;

    public MarkerItem(double lat, double lon, String phone, String address){
        this.lat = lat;
        this.lon = lon;
        this.phone = phone;
        this.address = address;
    }

    public double getLat(){
        return lat;
    }

    public double getLon(){
        return lon;
    }

    public String getPhone(){
        return phone;
    }

    public String getAddress(){
        return address;
    }


}
