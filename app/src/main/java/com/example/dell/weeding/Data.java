package com.example.dell.weeding;

/**
 * Created by DELL on 25-Mar-18.
 */

public class Data {
    private int id;
    private String name;
    private String address;
    private String imgAddress;

    public Data(String name, String address, String imgAddress) {
        this.name = name;
        this.address = address;
        this.imgAddress = imgAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImgAddress() {
        return imgAddress;
    }

    public void setImgAddress(String imgAddress) {
        this.imgAddress = imgAddress;
    }
}
