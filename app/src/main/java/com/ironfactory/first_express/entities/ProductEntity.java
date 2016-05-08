package com.ironfactory.first_express.entities;

import java.io.Serializable;

/**
 * Created by IronFactory on 2016. 1. 12..
 */
public class ProductEntity implements Serializable {

    public static final String WARDROBE = "장롱 * 3";
    public static final String CLOSET = "옷장 * 1";
    public static final String BED = "침대";
    public static final String DRESSING_TABLE = "화장대";
    public static final String DESK = "책상";
    public static final String LIVING_ROOM_DRESSING = "거실장";
    public static final String SOFA = "쇼파";
    public static final String REFRIGERATOR = "냉장고";
    public static final String WASHER = "세탁기";
    public static final String TABLE = "식탁";
    public static final String DRAWER = "서랍장";
    public static final String CHAIR = "의자";
    public static final String POT = "화분";
    public static final String WATER_PURIFIER = "정수기";
    public static final String BOOK_CASE = "책장";


    public static final String PROPERTY_WARDROBE = "wardrobe";
    public static final String PROPERTY_CLOSET = "closet";
    public static final String PROPERTY_BED = "bed";
    public static final String PROPERTY_DRESSING_TABLE = "dressingTable";
    public static final String PROPERTY_DESK = "desk";
    public static final String PROPERTY_LIVING_ROOM_DRESSING = "livingRoomDressing";
    public static final String PROPERTY_SOFA = "sofa";
    public static final String PROPERTY_REFRIGERATOR = "refrigerator";
    public static final String PROPERTY_WASHER = "washer";
    public static final String PROPERTY_TABLE = "_table";
    public static final String PROPERTY_DRAWER = "drawer";
    public static final String PROPERTY_CHAIR = "chair";
    public static final String PROPERTY_POT = "pot";
    public static final String PROPERTY_WATER_PURIFIER = "waterPurifier";
    public static final String PROPERTY_BOOK_CASE = "bookCase";

    private int price;
    private String name;
    private String property;
    public boolean isSelected;
    private int count;

    public ProductEntity(String name, int price, boolean isSelected) {
        this.name = name;
        this.price = price;
        this.isSelected = isSelected;
        count = 0;
        setProperty();
    }

    public ProductEntity(String name, int price) {
        this.name = name;
        this.price = price;
        isSelected = false;
        count = 0;
        setProperty();
    }

    private void setProperty() {
        if (name == null)
            return;

        if (name.equals(BED))
            property = PROPERTY_BED;
        if (name.equals(CHAIR))
            property = PROPERTY_CHAIR;
        if (name.equals(WARDROBE))
            property = PROPERTY_WARDROBE;
        if (name.equals(CLOSET))
            property = PROPERTY_CLOSET;
        if (name.equals(DRESSING_TABLE))
            property = PROPERTY_DRESSING_TABLE;
        if (name.equals(DESK))
            property = PROPERTY_DESK;
        if (name.equals(LIVING_ROOM_DRESSING))
            property = PROPERTY_LIVING_ROOM_DRESSING;
        if (name.equals(SOFA))
            property = PROPERTY_SOFA;
        if (name.equals(REFRIGERATOR))
            property = PROPERTY_REFRIGERATOR;
        if (name.equals(WASHER))
            property = PROPERTY_WASHER;
        if (name.equals(TABLE))
            property = PROPERTY_TABLE;
        if (name.equals(DRAWER))
            property = PROPERTY_DRAWER;
        if (name.equals(POT))
            property = PROPERTY_POT;
        if (name.equals(WATER_PURIFIER))
            property = PROPERTY_WATER_PURIFIER;
        if (name.equals(BOOK_CASE))
            property = PROPERTY_BOOK_CASE;
    }

    public int addCount() {
        return ++count;
    }

    public int minusCount() {
        return --count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ProductEntity() {
        isSelected = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setProperty();
    }

    public String getProperty() {
        return property;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
