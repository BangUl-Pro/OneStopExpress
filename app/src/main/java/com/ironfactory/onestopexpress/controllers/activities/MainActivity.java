package com.ironfactory.onestopexpress.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ironfactory.onestopexpress.Global;
import com.ironfactory.onestopexpress.R;
import com.ironfactory.onestopexpress.controllers.views.ProductGridView;
import com.ironfactory.onestopexpress.db.DBManager;
import com.ironfactory.onestopexpress.entities.OptionEntity;
import com.ironfactory.onestopexpress.entities.PersonEntity;
import com.ironfactory.onestopexpress.entities.ProductEntity;
import com.ironfactory.onestopexpress.entities.RoomEntity;

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


    private ArrayList<OptionEntity> selectOptions;
    private ArrayList<ProductEntity> selectProducts;

    private int roomSize;
    private int price;
    private PersonEntity personEntity;
    private Stack<Object> cache;
    private int moveType = Global.APARTMENT_MOVE;

    private Spinner roomSizeSpinner;
    private EditText personNumView;
    private ToggleButton officeBtn;
    private ToggleButton apartBtn;
    private ToggleButton radderBtn;
    private ToggleButton moveBtn;
    private ToggleButton airConBtn;
    private ToggleButton sysHangBtn;
    private ToggleButton closetBtn;
    private ToggleButton bedBtn;
    private ToggleButton busyBtn;
    private TextView priceView;
    private Button optionBtn;
    private Button submitBtn;
    private Button detailBtn;
    private Button backBtn;
    private Button cancelBtn;
    private Button equalBtn;
    private ProductGridView productGridView;
    private DBManager dbManager;

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
        selectOptions = new ArrayList<>();
        selectProducts = new ArrayList<>();

        productEntities = dbManager.getProduct();
        roomEntities = dbManager.getRoom();
        optionHash = dbManager.getOptionToMap();

        productGridView = (ProductGridView) findViewById(R.id.activity_main_product);
        productGridView.setProductEntities(productEntities);

        roomSizeSpinner = (Spinner) findViewById(R.id.activity_main_room_size);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, convertToList(roomEntities));
        roomSizeSpinner.setAdapter(adapter);

        personNumView = (EditText) findViewById(R.id.activity_main_person_num);
        officeBtn = (ToggleButton) findViewById(R.id.activity_main_office);
        apartBtn = (ToggleButton) findViewById(R.id.activity_main_apartment);
        radderBtn = (ToggleButton) findViewById(R.id.activity_main_radder);
        moveBtn = (ToggleButton) findViewById(R.id.activity_main_move);
        airConBtn = (ToggleButton) findViewById(R.id.activity_main_air_conditional);
        sysHangBtn = (ToggleButton) findViewById(R.id.activity_main_system_hanger);
        closetBtn = (ToggleButton) findViewById(R.id.activity_main_closet);
        bedBtn = (ToggleButton) findViewById(R.id.activity_main_bed);
        busyBtn = (ToggleButton) findViewById(R.id.activity_main_busy);
        priceView = (TextView) findViewById(R.id.activity_main_price);
        optionBtn = (Button) findViewById(R.id.activity_main_option);
        submitBtn = (Button) findViewById(R.id.activity_main_submit);
        detailBtn = (Button) findViewById(R.id.activity_main_detail);
        backBtn = (Button) findViewById(R.id.activity_main_back);
        cancelBtn = (Button) findViewById(R.id.activity_main_cancel);
        equalBtn = (Button) findViewById(R.id.activity_main_equal);

        setListener();
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
                roomSize = roomEntities.get(position).getNum();

                if (personEntity != null) {
                    price -= personEntity.getPrice();
                    personEntity = dbManager.getPerson(roomSize, Integer.parseInt(personNumView.getText().toString()));
                    price += personEntity.getPrice();
                    priceView.setText(String.valueOf(price));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        officeBtn.setOnClickListener(this);
        apartBtn.setOnClickListener(this);
        radderBtn.setOnClickListener(this);
        moveBtn.setOnClickListener(this);
        airConBtn.setOnClickListener(this);
        sysHangBtn.setOnClickListener(this);
        closetBtn.setOnClickListener(this);
        bedBtn.setOnClickListener(this);
        busyBtn.setOnClickListener(this);
        optionBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        detailBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        equalBtn.setOnClickListener(this);

        personNumView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (personEntity != null) {
                    price -= personEntity.getPrice();
                    priceView.setText(String.valueOf(price));
                    personEntity = null;
                }

                if (TextUtils.isEmpty(s))
                    return;

                int personNum = Integer.parseInt(s.toString());
                if (personNum >= 5)
                    personNum = 5;

                if (roomSize < 50) {
                    personEntity = dbManager.getPerson(roomSize, personNum);
                    price += personEntity.getPrice();
                    priceView.setText(String.valueOf(price));
                } else {
                    Toast.makeText(getApplicationContext(), "40평 이상은 별도 협의입니다", Toast.LENGTH_SHORT).show();
                    personNumView.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onAdd(ProductEntity productEntity) {
        price += productEntity.getPrice();
        priceView.setText(String.valueOf(price));

        selectProducts.add(productEntity);
        cache.push(productEntity);
    }

    @Override
    public void onRemove(ProductEntity productEntity) {
        price -= productEntity.getPrice();
        priceView.setText(String.valueOf(price));

        selectProducts.remove(productEntity);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(officeBtn)) {
            if (officeBtn.isChecked()) {
                optionHash.get(Global.OFFICE).setIsSelected(true);
                apartBtn.setChecked(false);
            } else {
                optionHash.get(Global.OFFICE).setIsSelected(false);
                apartBtn.setChecked(true);
            }
            String cacheOptionKey = Global.OFFICE;
            cache.push(cacheOptionKey);
        } else if (v.equals(apartBtn)) {
            if (apartBtn.isChecked()) {
                optionHash.get(Global.APARTMENT).setIsSelected(true);
                officeBtn.setChecked(false);
            } else {
                optionHash.get(Global.APARTMENT).setIsSelected(false);
                officeBtn.setChecked(true);
            }
            String cacheOptionKey = Global.APARTMENT;
            cache.push(cacheOptionKey);
        } else if (v.equals(radderBtn)) {
            if (radderBtn.isChecked()) {
                optionHash.get(Global.RADDER).setIsSelected(true);
            } else {
                optionHash.get(Global.RADDER).setIsSelected(false);
            }
            String cacheOptionKey = Global.RADDER;
            cache.push(cacheOptionKey);
        } else if (v.equals(moveBtn)) {
            if (moveBtn.isChecked()) {
                optionHash.get(Global.MOVE).setIsSelected(true);
            } else {
                optionHash.get(Global.MOVE).setIsSelected(false);
            }
            String cacheOptionKey = Global.MOVE;
            cache.push(cacheOptionKey);
        } else if (v.equals(airConBtn)) {
            if (airConBtn.isChecked()) {
                optionHash.get(Global.AIR_CONDITIONAL).setIsSelected(true);
            } else {
                optionHash.get(Global.AIR_CONDITIONAL).setIsSelected(false);
            }
            String cacheOptionKey = Global.AIR_CONDITIONAL;
            cache.push(cacheOptionKey);
        } else if (v.equals(sysHangBtn)) {
            if (sysHangBtn.isChecked()) {
                optionHash.get(Global.SYSTEM_HANGER).setIsSelected(true);
            } else {
                optionHash.get(Global.SYSTEM_HANGER).setIsSelected(false);
            }
            String cacheOptionKey = Global.SYSTEM_HANGER;
            cache.push(cacheOptionKey);
        } else if (v.equals(closetBtn)) {
            if (closetBtn.isChecked()) {
                optionHash.get(Global.CLOSET).setIsSelected(true);
            } else {
                optionHash.get(Global.CLOSET).setIsSelected(false);
            }
            String cacheOptionKey = Global.CLOSET;
            cache.push(cacheOptionKey);
        } else if (v.equals(bedBtn)) {
            if (bedBtn.isChecked()) {
                optionHash.get(Global.BED).setIsSelected(true);
            } else {
                optionHash.get(Global.BED).setIsSelected(false);
            }
            String cacheOptionKey = Global.BED;
            cache.push(cacheOptionKey);
        } else if (v.equals(busyBtn)) {
            if (busyBtn.isChecked()) {
                optionHash.get(Global.BUSY).setIsSelected(true);
            } else {
                optionHash.get(Global.BUSY).setIsSelected(false);
            }
            String cacheOptionKey = Global.BUSY;
            cache.push(cacheOptionKey);
        } else if (v.equals(submitBtn)) {
            if (check()) {
                Intent intent = new Intent(getApplicationContext(), CallActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "평수 또는 가족 수를 선택해 주세요", Toast.LENGTH_SHORT).show();
            }
        } else if (v.equals(detailBtn)) {
            Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
            startActivity(intent);
        } else if (v.equals(backBtn)) {
            Object object = cache.pop();
            if (object instanceof String) {
                String optionKey = object.toString();
                OptionEntity optionEntity = optionHash.get(optionKey);
                optionHash.get(optionKey).setIsSelected(!optionEntity.isSelected());
            } else if (object instanceof ProductEntity) {

            }
        } else if (v.equals(cancelBtn)) {

        } else if (v.equals(equalBtn)) {

        }
    }


    private ArrayList<OptionEntity> getOptionEntities() {
        ArrayList<OptionEntity> optionEntities = new ArrayList<>();

        Iterator<String> iterator = optionHash.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String name = optionHash.get(key).getName();
            boolean isSelected = optionHash.get(key).isSelected();
            OptionEntity optionEntity = new OptionEntity(name, isSelected);
            optionEntities.add(optionEntity);
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
}
