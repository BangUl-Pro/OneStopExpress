package com.ironfactory.first_express.controllers.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ironfactory.first_express.entities.ProductEntity;
import com.ironfactory.first_express.Global;
import com.ironfactory.first_express.R;
import com.ironfactory.first_express.Util;
import com.ironfactory.first_express.controllers.adapter.RoomSizeAdapter;
import com.ironfactory.first_express.controllers.views.ProductGridView;
import com.ironfactory.first_express.db.DBManager;
import com.ironfactory.first_express.entities.OptionEntity;
import com.ironfactory.first_express.entities.PersonEntity;
import com.ironfactory.first_express.entities.RoomEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements ProductGridView.OnAddProduct, View.OnClickListener {

    private static final String TAG = "MainActivity";

    private ArrayList<ProductEntity> productEntities;
    private ArrayList<RoomEntity> roomEntities;
    private HashMap<String, OptionEntity> optionHash;
    private HashMap<String, ProductEntity> products;

    private int roomSize;
    private int price;
    private PersonEntity personEntity;
    private Stack<Object> cache;
    private int moveType = Global.APARTMENT_MOVE;
    private boolean isFar = false;

    private Spinner roomSizeSpinner;
    private EditText personNumView;
    private ToggleButton officeBtn;
    private ToggleButton radderBtn;
    private ToggleButton airConBtn;
    private ToggleButton sysHangBtn;
    private ToggleButton closetBtn;
    private ToggleButton saveMoveBtn;
    private ToggleButton bedBtn;
    private ToggleButton busyBtn;
    private TextView priceView;
    private TextView optionView;
    private Button farBtn;
    private Button detailBtn;
    private Button backBtn;
    private Button cancelBtn;
    private Button equalBtn;
    private ProductGridView productGridView;
    private DBManager dbManager;

    private int personNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }


    private void init() {
        dbManager = new DBManager(getApplicationContext(), "OneStopExpress", null, 1);
        if (!dbManager.checkDB()) {
            // db가 준비되지 않았을 때
            MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
            MaterialDialog dialog = builder.progress(true, 10, false)
                    .progressIndeterminateStyle(false)
                    .cancelable(false)
                    .content("DB 구성 중입니다")
                    .show();

            dbManager.deleteAll();
            dbManager.insertAll();

            dialog.cancel();
        }

        cache = new Stack<>();
        products = new HashMap<>();

        productEntities = dbManager.getProduct();
        roomEntities = dbManager.getRoom();
        optionHash = dbManager.getOptionToMap();

        productGridView = (ProductGridView) findViewById(R.id.activity_main_product);
        productGridView.setProductEntities(productEntities);

        roomSizeSpinner = (Spinner) findViewById(R.id.activity_main_room_size);
        RoomSizeAdapter adapter = new RoomSizeAdapter(convertToList(roomEntities));
        roomSizeSpinner.setAdapter(adapter);

        personNumView = (EditText) findViewById(R.id.activity_main_person_num);
        officeBtn = (ToggleButton) findViewById(R.id.activity_main_office);
        radderBtn = (ToggleButton) findViewById(R.id.activity_main_radder);
        airConBtn = (ToggleButton) findViewById(R.id.activity_main_air_conditional);
        sysHangBtn = (ToggleButton) findViewById(R.id.activity_main_system_hanger);
        closetBtn = (ToggleButton) findViewById(R.id.activity_main_closet);
        bedBtn = (ToggleButton) findViewById(R.id.activity_main_bed);
        busyBtn = (ToggleButton) findViewById(R.id.activity_main_busy);
        saveMoveBtn = (ToggleButton) findViewById(R.id.activity_main_save_move);
        priceView = (TextView) findViewById(R.id.activity_main_price);
        optionView = (TextView) findViewById(R.id.activity_main_option);
        detailBtn = (Button) findViewById(R.id.activity_main_detail);
        backBtn = (Button) findViewById(R.id.activity_main_back);
        cancelBtn = (Button) findViewById(R.id.activity_main_cancel);
        equalBtn = (Button) findViewById(R.id.activity_main_equal);
        farBtn = (Button) findViewById(R.id.activity_main_far);

        Util.setGlobalFont(this, getWindow().getDecorView(), "NanumGothic.otf");

        priceView.setTypeface(Typeface.createFromAsset(getAssets(), "NanumGothicBold.otf"));

        backBtn.setText("<-");
//        optionHash.get(OptionEntity.OFFICE).setIsSelected(true);

        setEditable(true);

        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkOption(OptionEntity.RADDER, optionHash.get(OptionEntity.RADDER).isSelected());
        checkOption(OptionEntity.AIR_CONDITIONAL, optionHash.get(OptionEntity.AIR_CONDITIONAL).isSelected());
        checkOption(OptionEntity.SYSTEM_HANGER, optionHash.get(OptionEntity.SYSTEM_HANGER).isSelected());
        checkOption(OptionEntity.CLOSET, optionHash.get(OptionEntity.CLOSET).isSelected());
        checkOption(OptionEntity.BED, optionHash.get(OptionEntity.BED).isSelected());
        checkOption(OptionEntity.BUSY, optionHash.get(OptionEntity.BUSY).isSelected());
        checkOption(OptionEntity.FAR, optionHash.get(OptionEntity.FAR).isSelected());
        checkOption(OptionEntity.SAVE_MOVE, optionHash.get(OptionEntity.SAVE_MOVE).isSelected());
        checkOption(OptionEntity.OFFICE, optionHash.get(OptionEntity.OFFICE).isSelected());
    }

    private List<String> convertToList(ArrayList<RoomEntity> roomEntities) {
        List<String> list = new ArrayList<>();
        for (RoomEntity room :
                roomEntities) {
            list.add(room.getName());
        }
        return list;
    }


    private void setListener() {
        roomSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 4) {
                    Intent intent = new Intent(getApplicationContext(), CallActivity.class);
                    PersonEntity personEntity = new PersonEntity(1, 0, "40평 이상", 50);
                    intent.putExtra(Global.CONTENT, personEntity);
                    startActivity(intent);
                }

                roomSize = roomEntities.get(position).getNum();

                if (personEntity != null) {
                    price -= personEntity.getPrice();
                    personEntity = dbManager.getPerson(roomSize, Integer.parseInt(personNumView.getText().toString()));
                    price += personEntity.getPrice();
                    priceView.setText(convertDotWon(price));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        officeBtn.setOnClickListener(this);
        radderBtn.setOnClickListener(this);
        airConBtn.setOnClickListener(this);
        sysHangBtn.setOnClickListener(this);
        closetBtn.setOnClickListener(this);
        bedBtn.setOnClickListener(this);
        busyBtn.setOnClickListener(this);
        detailBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        equalBtn.setOnClickListener(this);
        saveMoveBtn.setOnClickListener(this);
        farBtn.setOnClickListener(this);

        personNumView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (personEntity != null) {
                    price -= personEntity.getPrice();
                    priceView.setText(convertDotWon(price));
                    personEntity = null;
                }

                if (TextUtils.isEmpty(s)) {
                    setClickable(true);
                    setEditable(true);
                    personNum = 0;
                    return;
                }
                setClickable(false);

                personNum = Integer.parseInt(s.toString());
                if (personNum >= 5)
                    personNum = 5;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private String convertDotWon(int won) {
        StringBuilder builder = new StringBuilder();
        builder.append(won);
        builder.reverse();
        int j = 0;
        for (int i = 3; i < builder.length() - j; i = i + 3, j++) {
            builder.insert(i + j, ",");
        }
        builder.reverse();
        builder.append("원");
        return builder.toString();
    }


    @Override
    public void onAdd(ProductEntity productEntity) {
        productEntity.isSelected = true;
        price += productEntity.getPrice();
        priceView.setText(convertDotWon(price));

        productEntity.addCount();
        products.put(productEntity.getProperty(), productEntity);

        cache.push(productEntity.getName());
        cache.push(true);

        optionView.setVisibility(View.VISIBLE);
        setEditable(false);
    }

    @Override
    public void onRemove(ProductEntity productEntity, boolean cache) {
        productEntity.isSelected = false;
        price -= productEntity.getPrice();
        priceView.setText(convertDotWon(price));

        ProductEntity product = products.get(productEntity.getProperty());
        if (product.getCount() == 1) {
            products.remove(product.getProperty());
        } else {
            product.minusCount();
            products.put(productEntity.getProperty(), product);
        }
        if (cache) {
            this.cache.push(productEntity.getName());
            this.cache.push(true);
        }
        if (!isChecked()) {
            optionView.setVisibility(View.INVISIBLE);
            setEditable(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(officeBtn)) {
            checkOption(OptionEntity.OFFICE, officeBtn.isChecked());
            cache.push(OptionEntity.OFFICE);
            cache.push(officeBtn.isChecked());
        } else if (v.equals(radderBtn)) {
            checkOption(OptionEntity.RADDER, radderBtn.isChecked());
            cache.push(OptionEntity.RADDER);
            cache.push(radderBtn.isChecked());
        } else if (v.equals(airConBtn)) {
            checkOption(OptionEntity.AIR_CONDITIONAL, airConBtn.isChecked());
            cache.push(OptionEntity.AIR_CONDITIONAL);
            cache.push(airConBtn.isChecked());
        } else if (v.equals(sysHangBtn)) {
            checkOption(OptionEntity.SYSTEM_HANGER, sysHangBtn.isChecked());
            cache.push(OptionEntity.SYSTEM_HANGER);
            cache.push(sysHangBtn.isChecked());
        } else if (v.equals(closetBtn)) {
            checkOption(OptionEntity.CLOSET, closetBtn.isChecked());
            cache.push(OptionEntity.CLOSET);
            cache.push(closetBtn.isChecked());
        } else if (v.equals(bedBtn)) {
            checkOption(OptionEntity.BED, bedBtn.isChecked());
            cache.push(OptionEntity.BED);
            cache.push(bedBtn.isChecked());
        } else if (v.equals(busyBtn)) {
            checkOption(OptionEntity.BUSY, busyBtn.isChecked());
            cache.push(OptionEntity.BUSY);
            cache.push(busyBtn.isChecked());
//        } else if (v.equals(submitBtn)) {
//            if (check()) {
//                Intent intent = new Intent(getApplicationContext(), CallActivity.class);
//                startActivity(intent);
//            } else {
//                Toast.makeText(this, "평수 또는 가족 수를 선택해 주세요", Toast.LENGTH_SHORT).show();
//            }
        } else if (v.equals(detailBtn)) {
        } else if (v.equals(backBtn)) {
            if (cache.size() == 0) {
                return;
            }
            Object object = cache.pop();
            boolean isChecked = (boolean) object;
            String optionKey = cache.pop().toString();
            checkOption(optionKey, !isChecked);
            if (!isChecked()) {
                setClickable(true);
                setEditable(true);
                optionView.setVisibility(View.INVISIBLE);
            }
        } else if (v.equals(cancelBtn)) {
            cache.empty();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        } else if (v.equals(equalBtn)) {
            if (isChecked()) {
                Log.d(TAG, "1");
                Intent intent = new Intent(getApplicationContext(), CallActivity.class);
                intent.putExtra(Global.OPTION, getOptionEntities());
                intent.putExtra(Global.PRODUCT, getProductEntities());
                intent.putExtra(Global.PRICE, price);
                startActivity(intent);
            } else if (personNum != 0) {
                Log.d(TAG, "2");
                Intent intent = new Intent(getApplicationContext(), CallActivity.class);
                intent.putExtra(Global.CONTENT, dbManager.getPerson(roomSize, personNum));
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "가구 및 옵션을 선택해주세요", Toast.LENGTH_SHORT).show();
            }
        } else if (v.equals(saveMoveBtn)) {
            checkOption(OptionEntity.SAVE_MOVE, saveMoveBtn.isChecked());
            cache.push(OptionEntity.SAVE_MOVE);
            cache.push(saveMoveBtn.isChecked());
        } else if (v.equals(farBtn)) {
            isFar = !isFar;

            checkOption(OptionEntity.FAR, isFar);
            cache.push(OptionEntity.FAR);
            cache.push(isFar);
        }
    }


    private void checkOption(String optionKey, boolean isChecked) {
        if (optionHash.get(optionKey) != null)
            optionHash.get(optionKey).setIsSelected(isChecked);

        if (optionKey.equals(OptionEntity.RADDER)) {
            radderBtn.setChecked(isChecked);
        } else if (optionKey.equals(OptionEntity.AIR_CONDITIONAL)) {
            airConBtn.setChecked(isChecked);
        } else if (optionKey.equals(OptionEntity.SYSTEM_HANGER)) {
            sysHangBtn.setChecked(isChecked);
        } else if (optionKey.equals(OptionEntity.CLOSET)) {
            closetBtn.setChecked(isChecked);
        } else if (optionKey.equals(OptionEntity.BED)) {
            bedBtn.setChecked(isChecked);
        } else if (optionKey.equals(OptionEntity.BUSY)) {
            busyBtn.setChecked(isChecked);
        } else if (optionKey.equals(OptionEntity.SAVE_MOVE)) {
            saveMoveBtn.setChecked(isChecked);
        } else if (optionKey.equals(OptionEntity.FAR)) {
            isFar = isChecked;
            farBtn.setText((isFar ? OptionEntity.FAR : "거리"));
        } else if (optionKey.equals(OptionEntity.OFFICE)) {
            officeBtn.setChecked(isChecked);
        } else {
            productGridView.removeProduct(optionKey);
        }

        if (isChecked()) {
            setEditable(false);
            optionView.setVisibility(View.VISIBLE);
        } else {
            setEditable(true);
            optionView.setVisibility(View.INVISIBLE);
        }
    }


    private boolean isChecked() {
        Log.d(TAG, "productSize = " + getProductEntities().size());
        Log.d(TAG, "optionSize = " + getOptionEntities().size());

        if (getProductEntities().size() == 0 && getOptionEntities().size() == 0)
            return false;
        return true;
    }


    private ArrayList<ProductEntity> getProductEntities() {
        ArrayList<ProductEntity> productEntities = new ArrayList<>();

        Iterator<String> iterator = products.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            ProductEntity productEntity = products.get(key);
            productEntities.add(productEntity);
        }
        return productEntities;
    }


    private ArrayList<OptionEntity> getOptionEntities() {
        ArrayList<OptionEntity> optionEntities = new ArrayList<>();

        Iterator<String> iterator = optionHash.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String name = optionHash.get(key).getName();
            boolean isSelected = optionHash.get(key).isSelected();
            if (isSelected) {
                OptionEntity optionEntity = new OptionEntity(name, isSelected);
                optionEntities.add(optionEntity);
            }
        }

        return optionEntities;
    }


    private boolean check() {
        if (roomSize != 0 && personEntity != null) {
            // 이사평수, 가족 수 선택했을 때
            return true;
        }
        return false;
    }


    private void setClickable(boolean clickable) {
        radderBtn.setClickable(clickable);
        airConBtn.setClickable(clickable);
        sysHangBtn.setClickable(clickable);
        closetBtn.setClickable(clickable);
        bedBtn.setClickable(clickable);
        busyBtn.setClickable(clickable);
        saveMoveBtn.setClickable(clickable);

        productGridView.setClickable(clickable);
        productGridView.setFocusable(clickable);
        productGridView.setFocusableInTouchMode(clickable);
    }


    private void setEditable(boolean editable) {
        if (!editable) {
            personNumView.clearFocus();
            personNumView.setText("");
        }
        personNumView.setFocusableInTouchMode(editable);
        personNumView.setFocusable(editable);
        personNumView.setClickable(editable);
    }
}
