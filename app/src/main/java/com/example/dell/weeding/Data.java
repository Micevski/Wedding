package com.example.dell.weeding;

import java.io.Serializable;
import java.util.List;

/**
 * Created by DELL on 25-Mar-18.
 */

public class Data implements Serializable {
    private Long id;
    private String name;
    private String address;
    private String homeImage;
    private String shortDescription;
    private String phone;
    private List<String> images;
    private List<Double> coordinates;

    public Data(Long id, String name, String address,
                String phone, String imgAddress,
                String shortDescription, List<String> images,
                List<Double> coordinates) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.homeImage = imgAddress;
        this.shortDescription = shortDescription;
        this.images = images;
        this.coordinates = coordinates;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getHomeImage() {
        return homeImage;
    }

    public void setHomeImage(String homeImage) {
        this.homeImage = homeImage;
    }
}
