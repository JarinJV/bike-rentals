package com.example.rapidrentals.DataModel;

public class Location {
    private String pickup;
    private String drop;

    private String carId;

    public Location() {
    }

    public Location(String pickup,String drop,String carId) {
        this.pickup = pickup;
        this.drop = drop;
        this.carId = carId;
    }

    @Override
    public String toString() {
        return "Location{" +
                "pickup=" + pickup +
                ", drop=" + drop +
                ", carId="+ carId +
                '}';
    }

    public String getPickup() {
        return pickup;
    }

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public String getDrop() {
        return drop;
    }

    public void setDrop(String drop) {
        this.drop= drop;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

}

