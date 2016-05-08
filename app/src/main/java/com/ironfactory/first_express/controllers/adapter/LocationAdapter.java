package com.ironfactory.first_express.controllers.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.ironfactory.first_express.R;
import com.ironfactory.first_express.Util;
import com.ironfactory.first_express.controllers.views.LocationGridView;

import java.util.ArrayList;

/**
 * Created by IronFactory on 2016. 1. 12..
 */
public class LocationAdapter extends BaseAdapter {

    private static final String TAG = "ProductAdapter";
    private ArrayList<String> locations;
    private LocationGridView.OnLocation handler;

    private boolean isClickable;

    public LocationAdapter(ArrayList<String> locations, LocationGridView.OnLocation handler) {
        this.locations = locations;
        this.handler = handler;
        isClickable = true;
    }


    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public Object getItem(int position) {
        return locations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
            Util.setGlobalFont(convertView.getContext(), convertView, "NanumGothic.otf");
        }
        Button btn = (Button) convertView.findViewById(R.id.item_location_btn);
        btn.setText(locations.get(position));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.onSetLocation(locations.get(position));
            }
        });
        return convertView;
    }


    public void setLocations(ArrayList<String> locations) {
        this.locations = locations;
        notifyDataSetChanged();
    }
}
