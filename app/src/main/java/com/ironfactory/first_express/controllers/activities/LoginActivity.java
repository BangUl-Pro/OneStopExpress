package com.ironfactory.first_express.controllers.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ironfactory.first_express.Global;
import com.ironfactory.first_express.networks.RequestListener;
import com.ironfactory.first_express.networks.SocketManager;
import com.ironfactory.first_express.R;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private String name;
    private String phone;

    private EditText nameEdit;
    private Button submitBtn;

    private RequestListener.OnLogin onLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SocketManager.createInstance(getApplicationContext());
        init();
    }


    private void init() {
        nameEdit = (EditText) findViewById(R.id.activity_login_name);
        submitBtn = (Button) findViewById(R.id.activity_login_submit);

        setListener();

        onLogin = new RequestListener.OnLogin() {
            @Override
            public void onSuccess(String name, String phoneNum) {
                Global.editor.putString(Global.NAME, name);
                Global.editor.putString(Global.MY_PHONE, phoneNum);
                Global.editor.commit();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(Global.NAME, name);
                intent.putExtra(Global.MY_PHONE, phoneNum);
                startActivity(intent);
                finish();
            }

            @Override
            public void onException() {
                Toast.makeText(getApplicationContext(), "로그인 도중 에러가 발생했습니다", Toast.LENGTH_SHORT).show();
            }
        };
    }


    private void setListener() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // 번호 받아오기 권한
                    int permissionCheck  = ContextCompat.checkSelfPermission(
                            getApplicationContext(),
                            Manifest.permission.READ_SMS
                    );

                    // 허용이 아니라면
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(
                                LoginActivity.this,
                                Manifest.permission.READ_SMS
                        )) {
                            Log.d(TAG, "설명 보여줘야만 한다");
                        } else {
                            ActivityCompat.requestPermissions(
                                    LoginActivity.this,
                                    new String[] {Manifest.permission.READ_SMS},
                                    1);
                        }
                    }
                } else {
                    login();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            // 성공
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                login();
            } else {
                // 거절
                Toast.makeText(getApplicationContext(), "권한 없이는 사용할 수 없는 기능입니다", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void login() {
        TelephonyManager manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        phone = manager.getLine1Number();
        name = nameEdit.getText().toString();

        SocketManager.signUp(name, phone, new RequestListener.OnSignUp() {
            @Override
            public void onSuccess(String name, String phoneNum) {
                Global.editor.putString(Global.NAME, name);
                Global.editor.putString(Global.MY_PHONE, phoneNum);
                Global.editor.commit();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(Global.NAME, name);
                intent.putExtra(Global.MY_PHONE, phoneNum);
                startActivity(intent);
                finish();
            }

            @Override
            public void onException(int code) {
                if (code == 302) {
                    SocketManager.login(name, phone, onLogin);
                } else {
                    Toast.makeText(getApplicationContext(), "회원가입 중 오류가 발생했습니다", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
