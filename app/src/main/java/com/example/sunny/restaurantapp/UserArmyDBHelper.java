package com.example.sunny.restaurantapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

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

    // Lebanon army table
    protected static final String TABLE_NAME2 = "lebanon_army";

    // Iran army table
    protected static final String TABLE_NAME3 = "Iran_army";


    public UserArmyDBHelper(Context context) {
        super(context, "user_army.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create user army table
        String sql = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER)"
                , TABLE_NAME, COL_ID, COL_NAME, COL_QUANTITY, COL_IMAGE, COL_PRICE);

        // create Lebanon army table
        String sql2 = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER)"
                , TABLE_NAME2, COL_ID, COL_NAME, COL_QUANTITY, COL_IMAGE, COL_PRICE);

        // create Iran army table
        String sql3 = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER, %s TEXT, %s INTEGER)"
                , TABLE_NAME3, COL_ID, COL_NAME, COL_QUANTITY, COL_IMAGE, COL_PRICE);

        db.execSQL(sql);
        db.execSQL(sql2);
        db.execSQL(sql3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
