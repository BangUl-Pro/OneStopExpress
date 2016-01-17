package com.ironfactory.onestopexpress.entities;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by IronFactory on 2016. 1. 13..
 */
public class OptionEntity implements Parcelable {
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
    }

    public OptionEntity(Parcel parcel) {
        Bundle bundle = parcel.readBundle();
        name = bundle.getString(PROPERTY_NAME);
        isSelected = bundle.getBoolean(PROPERTY_SELECTED);
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


    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(PROPERTY_NAME, name);
        bundle.putBoolean(PROPERTY_SELECTED, isSelected);
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
