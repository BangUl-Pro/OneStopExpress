package com.ironfactory.first_express.controllers.adapter;

import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ironfactory.first_express.entities.ProductEntity;
import com.ironfactory.first_express.R;
import com.ironfactory.first_express.Util;
import com.ironfactory.first_express.controllers.views.ProductGridView;

import java.util.ArrayList;

/**
 * Created by IronFactory on 2016. 1. 12..
 */
public class ProductAdapter extends BaseAdapter {

    private static final String TAG = "ProductAdapter";
    private ArrayList<ProductEntity> productEntities;
    private ArrayList<Integer> productCounts;
    private ProductGridView.OnAddProduct handler;

    private boolean isClickable;
    private int displayWidth;

    public ProductAdapter(ArrayList<ProductEntity> productEntities, int displayWidth, ProductGridView.OnAddProduct handler) {
        this.productEntities = productEntities;
        productCounts = new ArrayList<>();
        this.handler = handler;
        isClickable = true;
        this.displayWidth = displayWidth;
    }


    @Override
    public int getCount() {
        return productEntities.size() / 4 + 1;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
            Util.setGlobalFont(convertView.getContext(), convertView, "NanumGothic.otf");
        }


        TextView titleView = (TextView) convertView.findViewById(R.id.item_product_title);
        Button btn1 = (Button) convertView.findViewById(R.id.item_product_btn1);
        final TextView numView1 = (TextView) convertView.findViewById(R.id.item_product_num1);
        CardView cardView1 = (CardView) convertView.findViewById(R.id.item_product_card1);
        Button btn2 = (Button) convertView.findViewById(R.id.item_product_btn2);
        final TextView numView2 = (TextView) convertView.findViewById(R.id.item_product_num2);
        CardView cardView2 = (CardView) convertView.findViewById(R.id.item_product_card2);
        Button btn3 = (Button) convertView.findViewById(R.id.item_product_btn3);
        final TextView numView3 = (TextView) convertView.findViewById(R.id.item_product_num3);
        CardView cardView3 = (CardView) convertView.findViewById(R.id.item_product_card3);
        Button btn4 = (Button) convertView.findViewById(R.id.item_product_btn4);
        final TextView numView4 = (TextView) convertView.findViewById(R.id.item_product_num4);
        CardView cardView4 = (CardView) convertView.findViewById(R.id.item_product_card4);

        if (position == 0) {
            cardView1.setVisibility(View.GONE);
            cardView2.setVisibility(View.GONE);

            if (productCounts.get(0) == 0) {
                numView3.setVisibility(View.INVISIBLE);
            }
            if (productCounts.get(1) == 0) {
                numView4.setVisibility(View.INVISIBLE);
            }

            numView3.setText(String.valueOf(productCounts.get(0)));
            numView4.setText(String.valueOf(productCounts.get(1)));

            btn3.setText(productEntities.get(0).getName());
            btn4.setText(productEntities.get(1).getName());

            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClickable) {
                        handler.onAdd(productEntities.get(0));

                        if (productCounts.get(0) == 0) {
                            numView3.setVisibility(View.VISIBLE);
                        }
                        productCounts.set(0, productCounts.get(0) + 1);
                        numView3.setText(String.valueOf(productCounts.get(0)));
                    }
                }
            });

            btn4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClickable) {
                        handler.onAdd(productEntities.get(1));

                        if (productCounts.get(1) == 0) {
                            numView4.setVisibility(View.VISIBLE);
                        }
                        productCounts.set(1, productCounts.get(1) + 1);
                        numView4.setText(String.valueOf(productCounts.get(1)));
                    }
                }
            });


            titleView.setVisibility(View.VISIBLE);
            titleView.setGravity(Gravity.CENTER);
        } else {
            final int curPosition1 = position * 4 - 2;
            final int curPosition2 = position * 4 - 1;
            final int curPosition3 = position * 4;
            final int curPosition4 = position * 4 + 1;

            if (productCounts.get(curPosition1) == 0) {
                numView1.setVisibility(View.INVISIBLE);
            }
            numView1.setText(String.valueOf(productCounts.get(curPosition1)));
            btn1.setText(productEntities.get(curPosition1).getName());
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClickable) {
                        handler.onAdd(productEntities.get(curPosition1));

                        if (productCounts.get(curPosition1) == 0) {
                            numView1.setVisibility(View.VISIBLE);
                        }
                        productCounts.set(curPosition1, productCounts.get(curPosition1) + 1);
                        numView1.setText(String.valueOf(productCounts.get(curPosition1)));
                    }
                }
            });

            if (productCounts.get(curPosition2) == 0) {
                numView2.setVisibility(View.INVISIBLE);
            }
            numView2.setText(String.valueOf(productCounts.get(curPosition2)));
            btn2.setText(productEntities.get(curPosition2).getName());
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClickable) {
                        handler.onAdd(productEntities.get(curPosition2));

                        if (productCounts.get(curPosition2) == 0) {
                            numView2.setVisibility(View.VISIBLE);
                        }
                        productCounts.set(curPosition2, productCounts.get(curPosition2) + 1);
                        numView2.setText(String.valueOf(productCounts.get(curPosition2)));
                    }
                }
            });


            if (productCounts.get(curPosition3) == 0) {
                numView3.setVisibility(View.INVISIBLE);
            }
            numView3.setText(String.valueOf(productCounts.get(curPosition3)));
            btn3.setText(productEntities.get(curPosition3).getName());
            btn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClickable) {
                        handler.onAdd(productEntities.get(curPosition3));

                        if (productCounts.get(curPosition3) == 0) {
                            numView3.setVisibility(View.VISIBLE);
                        }
                        productCounts.set(curPosition3, productCounts.get(curPosition3) + 1);
                        numView3.setText(String.valueOf(productCounts.get(curPosition3)));
                    }
                }
            });


            if (productCounts.get(curPosition4) == 0) {
                numView4.setVisibility(View.INVISIBLE);
            }
            numView4.setText(String.valueOf(productCounts.get(curPosition4)));
            btn4.setText(productEntities.get(curPosition4).getName());
            btn4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClickable) {
                        handler.onAdd(productEntities.get(curPosition4));

                        if (productCounts.get(curPosition4) == 0) {
                            numView4.setVisibility(View.VISIBLE);
                        }
                        productCounts.set(curPosition4, productCounts.get(curPosition4) + 1);
                        numView4.setText(String.valueOf(productCounts.get(curPosition4)));
                    }
                }
            });

        }
        return convertView;
    }


    public void removeProduct(String name) {
        int index;
        for (index = 0; index < productEntities.size(); index++) {
            if (name.equals(productEntities.get(index).getName()))
                break;
        }
        Log.d(TAG, "index = " + index);
        productCounts.set(index, productCounts.get(index) - 1);
        handler.onRemove(productEntities.get(index), false);
        notifyDataSetChanged();
    }


    public void setProductEntities(ArrayList<ProductEntity> productEntities) {
        this.productEntities = productEntities;
        productCounts = new ArrayList<>();
        for (int i = 0; i < productEntities.size(); i++) {
            productCounts.add(0);
        }
        notifyDataSetChanged();
    }


    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }
}
