package com.accepted.acceptedtalentplanet;

import java.io.Serializable;

/**
 * Created by kwonhong on 2017-11-25.
 */

public class GeoPoint implements Serializable {
    double lat, lng;

    public GeoPoint(double lat, double lng){
        this.lat = lat;
        this.lng = lng;
    }

    public void setLat(double lat){
        this.lat = lat;
    }

    public double getLat(){
        return this.lat;
    }

    public void setLng(double lng){
        this.lng = lng;
    }

    public double getLng(){
        return this.lng;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("lat = ").append(String.valueOf(lat)).append(", lng = ").append(String.valueOf(lng));
        return sb.toString();
    }

}
