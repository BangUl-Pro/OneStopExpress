package com.ironfactory.onestopexpress.entities;

/**
 * Created by IronFactory on 2016. 1. 13..
 */
public class OptionEntity {
    private String name;
    private boolean isSelected;

    public OptionEntity(String name, int price) {
        this.name = name;
    }

    public OptionEntity() {
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
