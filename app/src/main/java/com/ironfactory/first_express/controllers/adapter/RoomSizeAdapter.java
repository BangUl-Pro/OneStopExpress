package com.ironfactory.first_express.controllers.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ironfactory.first_express.R;
import com.ironfactory.first_express.Util;

import java.util.List;

/**
 * Created by IronFactory on 2016. 1. 12..
 */
public class RoomSizeAdapter extends BaseAdapter {

    private static final String TAG = "RoomSizeAdapter";
    private List<String> roomSizes;

    public RoomSizeAdapter(List<String> roomSizes) {
        this.roomSizes = roomSizes;
    }


    @Override
    public int getCount() {
        return roomSizes.size();
    }

    @Override
    public Object getItem(int position) {
        return roomSizes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_spinner, parent, false);
            Util.setGlobalFont(convertView.getContext(), convertView, "NanumGothic.otf");
        }
        TextView textView = (TextView) convertView.findViewById(R.id.item_room_text);
        textView.setText(roomSizes.get(position));
        return convertView;
    }
}
