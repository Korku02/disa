package com.example.korku02.disa;


public class UserInformation {

    public String name;
    public String hostel;

    public UserInformation(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHostel() {
        return hostel;
    }

    public void setAddress(String address) {
        this.hostel = address;
    }

    public UserInformation(String name, String address) {
        this.name = name;
        this.hostel = address;
    }
}
