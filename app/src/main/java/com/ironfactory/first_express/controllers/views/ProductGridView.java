package com.ironfactory.first_express.controllers.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

import com.ironfactory.first_express.controllers.adapter.ProductAdapter;
import com.ironfactory.first_express.entities.ProductEntity;

import java.util.ArrayList;

/**
 * Created by IronFactory on 2016. 1. 12..
 */
public class ProductGridView extends ListView {

    private ArrayList<ProductEntity> productEntities;
    private OnAddProduct handler;
    private ProductAdapter adapter;

    public ProductGridView(Context context) {
        super(context);
        init(context);
    }

    public ProductGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ProductGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    //    public ProductGridView(Context context, MainActivity.OnAddProduct handler) {
//        super(context);
//        init(handler);
//    }
//
//    public ProductGridView(Context context, AttributeSet attrs, MainActivity.OnAddProduct handler) {
//        super(context, attrs);
//        init(handler);
//    }
//
//    public ProductGridView(Context context, AttributeSet attrs, int defStyleAttr, MainActivity.OnAddProduct handler) {
//        super(context, attrs, defStyleAttr);
//        init(handler);
//    }
//
//    public ProductGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        this.handler = handler;
//    }


    private void init(Context context) {
        handler = (OnAddProduct) context;

        productEntities = new ArrayList<>();
        DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
        int width = metrics.widthPixels / 5;

        adapter = new ProductAdapter(productEntities, width, handler);
        setAdapter(adapter);

        setDividerHeight(0);
    }


    public void removeProduct(String name) {
        adapter.removeProduct(name);
    }


    public void setProductEntities(ArrayList<ProductEntity> productEntities) {
        this.productEntities = productEntities;
        adapter.setProductEntities(productEntities);
    }


    @Override
    public void setClickable(boolean clickable) {
        super.setClickable(clickable);
        if (adapter != null)
            adapter.setClickable(clickable);
    }

    public interface OnAddProduct {
        void onAdd(ProductEntity productEntity);
        void onRemove(ProductEntity productEntity, boolean cache);
    }
}
