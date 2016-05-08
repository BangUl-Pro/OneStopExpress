package com.ironfactory.first_express.entities;

/**
 * Created by IronFactory on 2016. 1. 13..
 */
public class RoomEntity {
    private String name;
    private int num;

    public RoomEntity(String name, int num) {
        this.name = name;
        this.num = num;
    }

    public RoomEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
