package com.example.reizen.models;

public class UserModel {

    String email,name,mobile,address;

    public UserModel() {
    }

    public UserModel(String email, String name, String mobile, String address) {
        this.email = email;
        this.name = name;
        this.mobile = mobile;
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
