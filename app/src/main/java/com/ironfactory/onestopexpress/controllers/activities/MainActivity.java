package com.ironfactory.onestopexpress.controllers.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ironfactory.onestopexpress.R;
import com.ironfactory.onestopexpress.controllers.views.ProductGridView;
import com.ironfactory.onestopexpress.db.DBManager;
import com.ironfactory.onestopexpress.entities.ProductEntity;

public class MainActivity extends AppCompatActivity implements ProductGridView.OnAddProduct {

    private static final String TAG = "MainActivity";

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

        productGridView = (ProductGridView) findViewById(R.id.activity_main_product);
    }


    @Override
    public void onAdd(ProductEntity productEntity) {

    }
}
