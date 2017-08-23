package com.example.sunny.restaurantapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sunny on 05/07/2017.
 */

public class UserArmyDBHelper extends SQLiteOpenHelper {

    // user army table
    protected static final String TABLE_NAME = "user_army";
    protected static final String COL_ID = "id";
    protected static final String COL_NAME = "name";
    protected static final String COL_QUANTITY = "quantity";
    protected static final String COL_IMAGE = "image";
    protected static final String COL_PRICE = "price";

    // user info table



    public UserArmyDBHelper(Context context) {
        super(context, "user_army.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create user armt table
        String sql = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER)"
                , TABLE_NAME, COL_ID, COL_NAME, COL_QUANTITY, COL_IMAGE, COL_PRICE);

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
