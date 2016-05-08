package com.ironfactory.first_express.entities;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by IronFactory on 2016. 1. 13..
 */
public class OptionEntity implements Parcelable {
    public static final String OFFICE = "사무실이사";
    public static final String RADDER = "사다리차/운반";
    public static final String AIR_CONDITIONAL = "에어컨";
    public static final String SYSTEM_HANGER = "시스템헹거";
    public static final String CLOSET = "붙박이장";
    public static final String BED = "흙/돌침대";
    public static final String BUSY = "손없는날/주말";
    public static final String SAVE_MOVE = "보관이사";
    public static final String FAR = "운행거리 10km 이상";

    public static final String PROPERTY = "property";
    public static final String PROPERTY_OFFICE = "office";
    public static final String PROPERTY_RADDER = "radder";
    public static final String PROPERTY_MOVE = "move";
    public static final String PROPERTY_AIR_CONDITIONAL = "airConditional";
    public static final String PROPERTY_SYSTEM_HANGER = "systemHanger";
    public static final String PROPERTY_CLOSET = "bigCloset";
    public static final String PROPERTY_BED = "earthBed";
    public static final String PROPERTY_BUSY = "busy";
    public static final String PROPERTY_SAVE_MOVE = "saveMoney";


    private String property;
    private String name;
    private boolean isSelected;

    private static final String PROPERTY_NAME = "name";
    private static final String PROPERTY_SELECTED = "selected";

    public static final Creator<OptionEntity> CREATOR = new Creator<OptionEntity>() {
        public OptionEntity createFromParcel(Parcel in) {
            return new OptionEntity(in);
        }

        public OptionEntity[] newArray(int size) {
            return new OptionEntity[size];
        }
    };

    public OptionEntity() {
        isSelected = false;
    }

    public OptionEntity(String name, boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
        setProperty();
    }

    public OptionEntity(Parcel parcel) {
        Bundle bundle = parcel.readBundle();
        name = bundle.getString(PROPERTY_NAME);
        isSelected = bundle.getBoolean(PROPERTY_SELECTED);
        property = bundle.getString(PROPERTY);
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty() {
        if (name == null)
            return;
        if (name.equals(OFFICE))
            property = PROPERTY_OFFICE;
        if (name.equals(RADDER))
            property = PROPERTY_RADDER;
        if (name.equals(AIR_CONDITIONAL))
            property = PROPERTY_AIR_CONDITIONAL;
        if (name.equals(SYSTEM_HANGER))
            property = PROPERTY_SYSTEM_HANGER;
        if (name.equals(CLOSET))
            property = PROPERTY_CLOSET;
        if (name.equals(BED))
            property = PROPERTY_BED;
        if (name.equals(BUSY))
            property = PROPERTY_BUSY;
        if (name.equals(SAVE_MOVE))
            property = PROPERTY_SAVE_MOVE;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        setProperty();
    }


    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(PROPERTY_NAME, name);
        bundle.putBoolean(PROPERTY_SELECTED, isSelected);
        bundle.putString(PROPERTY, property);
        return bundle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(getBundle());
    }
}
