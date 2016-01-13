package com.ironfactory.onestopexpress.entities;

/**
 * Created by IronFactory on 2016. 1. 12..
 */
public class ProductEntity {
    private int imageRes;
    private int category;
    private int price;
    private String name;

    public ProductEntity(int imageRes, String name, int price, int category) {
        this.imageRes = imageRes;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }
}
