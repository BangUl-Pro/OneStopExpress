package com.ironfactory.first_express;

import android.content.SharedPreferences;

/**
 * Created by IronFactory on 2016. 1. 12..
 */
public class Global {

    public static final String APP_NAME = "이사통";

    public static final int MODE_ROOM_SIZE = 1;
    public static final int MODE_PRODUCT = 2;
    public static final int MODE_OPTION = 3;

    public static final String OPTION = "option";
    public static final String PRODUCT = "product";

    public static final String PHONE = "010-4532-9462";

    public static final String NAME = "name";
    public static final String MY_PHONE = "myPhone";

    public static final int OFFICE_MOVE = 1;
    public static final int APARTMENT_MOVE = 2;

    public static final String CONTENT = "content";
    public static final String PRICE = "price";


    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
}
