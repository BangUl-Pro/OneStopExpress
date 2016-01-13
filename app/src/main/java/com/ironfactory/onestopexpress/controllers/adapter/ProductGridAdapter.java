package com.ironfactory.onestopexpress.controllers.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironfactory.onestopexpress.R;
import com.ironfactory.onestopexpress.controllers.views.ProductGridView;
import com.ironfactory.onestopexpress.entities.ProductEntity;

import java.util.ArrayList;

/**
 * Created by IronFactory on 2016. 1. 12..
 */
public class ProductGridAdapter extends BaseAdapter {

    private ArrayList<ProductEntity> productEntities;
    private ProductGridView.OnAddProduct handler;

    public ProductGridAdapter(ArrayList<ProductEntity> productEntities, ProductGridView.OnAddProduct handler) {
        this.productEntities = productEntities;
        this.handler = handler;
    }


    @Override
    public int getCount() {
        return productEntities.size();
    }

    @Override
    public Object getItem(int position) {
        return productEntities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.item_product_image);
        TextView nameView = (TextView) convertView.findViewById(R.id.item_product_name);
        Button addView = (Button) convertView.findViewById(R.id.item_product_add);

        imageView.setImageResource(productEntities.get(position).getImageRes());
        nameView.setText(productEntities.get(position).getName());
        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.onAdd(productEntities.get(position));
            }
        });

        return convertView;
    }
}
