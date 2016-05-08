package com.ironfactory.first_express.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.ironfactory.first_express.Global;
import com.ironfactory.first_express.R;

public class SplashActivity extends AppCompatActivity {

    private String name;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Global.preferences = getSharedPreferences(Global.APP_NAME, MODE_PRIVATE);
        Global.editor = Global.preferences.edit();


        CountDownTimer timer = new CountDownTimer(2000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (isRegisted()) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }.start();
    }


    private boolean isRegisted() {
        name = Global.preferences.getString(Global.NAME, null);
        phone = Global.preferences.getString(Global.MY_PHONE, null);

        if (name == null)
            return false;
        if (phone == null)
            return false;
        return true;
    }
}
