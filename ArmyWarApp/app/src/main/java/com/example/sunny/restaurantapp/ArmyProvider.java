package com.example.sunny.restaurantapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Sunny on 06/07/2017.
 */

public class ArmyProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.sunny.restaurantapp.authority.army";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + UserArmyDBHelper.TABLE_NAME);

    private UserArmyDBHelper helper;

    @Override
    public boolean onCreate() {

        helper = new UserArmyDBHelper(getContext());

        if (helper != null){
            return true;
        }
        return false;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = helper.getWritableDatabase();
        long rowId;

        rowId = db.insert(UserArmyDBHelper.TABLE_NAME, null, values);

        getContext().getContentResolver().notifyChange(uri, null);

        return Uri.withAppendedPath(uri, rowId + "");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor;

        cursor = db.query(UserArmyDBHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = helper.getWritableDatabase();
        int count;

        count = db.delete(UserArmyDBHelper.TABLE_NAME, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        SQLiteDatabase db = helper.getWritableDatabase();
        int count;

        count = db.update(UserArmyDBHelper.TABLE_NAME, values, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
