package com.ironfactory.first_express.entities;

import java.io.Serializable;

/**
 * Created by IronFactory on 2016. 1. 13..
 */
public class PersonEntity implements Serializable {
    private int num;
    private int price;
    private String name;
    private int roomNum;

    public PersonEntity() {
    }

    public PersonEntity(int num, int price, String name, int roomNum) {
        this.num = num;
        this.price = price;
        this.name = name;
        this.roomNum = roomNum;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
