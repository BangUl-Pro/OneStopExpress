package com.ironfactory.first_express.controllers.activities;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.ironfactory.first_express.Global;
import com.ironfactory.first_express.Util;
import com.ironfactory.first_express.controllers.views.LocationGridView;
import com.ironfactory.first_express.entities.OptionEntity;
import com.ironfactory.first_express.entities.PersonEntity;
import com.ironfactory.first_express.entities.ProductEntity;
import com.ironfactory.first_express.networks.RequestListener;
import com.ironfactory.first_express.networks.SocketManager;
import com.ironfactory.first_express.R;

import java.util.ArrayList;
import java.util.Calendar;

public class CallActivity extends AppCompatActivity implements LocationGridView.OnLocation {

    private static final String TAG = "CallActivity";
//    private static final String PHONE = "01045329462";
    private static final String PHONE = "01033677355";

    private Button dateBtn;
    private LocationGridView locationGridView;
    private String location;
    private Activity activity = this;

    private String content;
    private String date;
    private int price;

    private PersonEntity personEntity;
    private ArrayList<ProductEntity> productEntities;
    private ArrayList<OptionEntity> optionEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        Util.setGlobalFont(this, getWindow().getDecorView(), "NanumGothic.otf");

        if (Global.preferences == null) {
            Global.preferences = getSharedPreferences(Global.APP_NAME, MODE_PRIVATE);
            Global.editor = Global.preferences.edit();
        }

        init();
    }


    private void getIntent(Intent intent) {
        price = intent.getIntExtra(Global.PRICE, -1);

        if ((personEntity = (PersonEntity) intent.getSerializableExtra(Global.CONTENT)) != null) {
            String phone = Global.preferences.getString(Global.MY_PHONE, null);
            Log.d(TAG, "phone = " + phone);
            Log.d(TAG, "name = " + personEntity.getName());
            Log.d(TAG, "num = " + personEntity.getNum());
            Log.d(TAG, "price = " + personEntity.getPrice());
            Log.d(TAG, "roomNum = " + personEntity.getRoomNum());
            SocketManager.insertMove(personEntity, phone, new RequestListener.OnInsertMove() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onException() {
                    Toast.makeText(getApplicationContext(), "DB 입력 에러", Toast.LENGTH_SHORT).show();
                }
            });
        } else if ((optionEntities = intent.getParcelableArrayListExtra(Global.OPTION)) != null &&
                (productEntities = (ArrayList) intent.getSerializableExtra(Global.PRODUCT)) != null) {
            if (Global.preferences == null) {
                Global.preferences = getSharedPreferences(Global.APP_NAME, MODE_PRIVATE);
            }
            String phone = Global.preferences.getString(Global.MY_PHONE, null);
            SocketManager.insertMove(productEntities, optionEntities, price, phone, new RequestListener.OnInsertMove() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onException() {
                    Toast.makeText(getApplicationContext(), "DB 입력 에러", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void init() {
        dateBtn = (Button) findViewById(R.id.activity_call_date);
        locationGridView = (LocationGridView) findViewById(R.id.activity_call_grid);
        locationGridView.setLocations(getLocations());

        setListener();
    }


    private ArrayList<String> getLocations() {
        ArrayList<String> locations = new ArrayList<>();
        locations.add("서울");
        locations.add("부산");
        locations.add("대구");
        locations.add("대전");
        locations.add("울산");
        locations.add("인천");
        locations.add("세종");
        locations.add("경기");
        locations.add("강원");
        locations.add("경남");
        locations.add("경북");
        locations.add("전남");
        locations.add("전북");
        locations.add("충남");
        locations.add("충북");
        locations.add("대구");
        locations.add("광주");
        locations.add("제주");
        return locations;
    }


    private void setListener() {
        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(year);
                        sb.append("년 ");
                        sb.append(monthOfYear + 1);
                        sb.append("월 ");
                        sb.append(dayOfMonth);
                        sb.append("일");
                        date = sb.toString();
                    }
                }, year, month, day);
                dialog.show();
            }
        });
    }


    @Override
    public void onSetLocation(String location) {
        this.location = location;

        if (date == null) {
            Toast.makeText(getApplicationContext(), "날짜를 선택해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        getIntent(getIntent());

        String name = Global.preferences.getString(Global.NAME, null);
        String phone = Global.preferences.getString(Global.MY_PHONE, null);

        StringBuilder sb = new StringBuilder();
        sb.append("이름 : " + name);
        sb.append("\n번호 : " + phone);

        if (personEntity != null) {
            sb.append(personEntity.getName() + " ");
            if (personEntity.getRoomNum() == 10) {
                sb.append("10평 미만");
            } else if (personEntity.getRoomNum() == 20) {
                sb.append("20평 미만 ");
            } else if (personEntity.getRoomNum() == 30) {
                sb.append("20평 미만 ");
            } else if (personEntity.getRoomNum() == 40) {
                sb.append("40평 미만 ");
            } else if (personEntity.getRoomNum() == 50) {
                sb.append("40평 이상 ");
            }

            price += personEntity.getPrice();
        }

        if (productEntities != null || optionEntities != null || personEntity != null)
            sb.append("\n총액 : " + price + "원\n");

        if (productEntities != null) {
            for (ProductEntity product :
                    productEntities) {
                sb.append(product.getName() + " ");
            }
        }

        if (optionEntities != null) {
            for (OptionEntity option :
                    optionEntities) {
                sb.append(option.getName() + " ");
            }
        }

        ArrayList<String> part = new ArrayList<String>();
        for (int i = 0; i < sb.length(); i = i + 39) {
            if (i + 39 > sb.length()) {
                part.add(sb.substring(i, sb.length()));
            } else {
                part.add(sb.substring(i, i + 39));
            }
        }

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendMultipartTextMessage(PHONE, null, part, null, null);
//                uri = Uri.parse("smsto:" + PHONE);
//                intent = new Intent(Intent.ACTION_SENDTO, uri);
//                intent.putExtra("sms_body", "body");
//                startActivity(intent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(CallActivity.this, Manifest.permission.CALL_PHONE)) {
                    Log.d(TAG, "설명 보여줘야만 한다");
                } else {
                    ActivityCompat.requestPermissions(CallActivity.this, new String[] {Manifest.permission.CALL_PHONE}, 1);
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                final ProgressDialog progressDialog = new ProgressDialog(activity);
                progressDialog.setTitle("잠시만 기다려주세요");
                progressDialog.show();

                new CountDownTimer(2000, 2000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        progressDialog.cancel();
                        Uri uri = Uri.parse("tel:" + PHONE);
                        Intent intent = new Intent(Intent.ACTION_CALL, uri);
                        try {
                            startActivity(intent);
                        } catch (SecurityException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                }.start();
            }
        }
    }
}
