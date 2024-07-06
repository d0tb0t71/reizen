package com.example.reizen.models;

public class PlaceModel {

    String name, address, img_url;


    public PlaceModel() {
    }

    public PlaceModel(String name, String address, String img_url) {
        this.name = name;
        this.address = address;
        this.img_url = img_url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
