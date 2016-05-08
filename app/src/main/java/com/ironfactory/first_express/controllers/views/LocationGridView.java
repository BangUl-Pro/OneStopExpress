package com.ironfactory.first_express.controllers.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

import com.ironfactory.first_express.controllers.adapter.LocationAdapter;

import java.util.ArrayList;

/**
 * Created by IronFactory on 2016. 1. 19..
 */
public class LocationGridView extends GridView {

    private OnLocation handler;
    private ArrayList<String> locations;
    private LocationAdapter adapter;

    public LocationGridView(Context context) {
        super(context);
        init(context);
    }

    public LocationGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LocationGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        handler = (OnLocation) context;
        locations = new ArrayList<>();
        adapter = new LocationAdapter(locations, handler);

        setAdapter(adapter);
        setNumColumns(3);
    }


    public void setLocations(ArrayList<String> locations) {
        this.locations = locations;
        adapter.setLocations(locations);
    }


    public interface OnLocation {
        void onSetLocation(String location);
    }
}
