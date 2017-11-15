package com.example.sunny.restaurantapp;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class UserMainPageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "UserMainPageActivity";
    protected static final int STARTING_LAND = 20000;
    protected static final int STARTING_MONEY = 20;
    protected static final String STARTING_DATE = "23/07/2017";

    private FragmentManager manager;
    private SharedPreferences userInfoSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_page);

        userInfoSP = getSharedPreferences(getString(R.string.user_info), MODE_PRIVATE);
        boolean firsTimeRun = userInfoSP.getBoolean(getString(R.string.first_time_run), true);

        if (firsTimeRun){
            userInfoSP.edit()
                    .putBoolean(getString(R.string.first_time_run), false)
                    .putInt(getString(R.string.save_land), STARTING_LAND)
                    .putInt(getString(R.string.save_money), STARTING_MONEY)
                    .putString(getString(R.string.save_date), STARTING_DATE)
                    .apply();
        }

        manager = getSupportFragmentManager();

        manager.beginTransaction()
                .add(R.id.mainContainer, new MainFragment())
                .commit();

        findViewById(R.id.buttonLogout).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent = new Intent(UserMainPageActivity.this, MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    protected void insertItemsIntoDB(ArrayList<ArmyItem> armyItems){

        for (int i = 0; i < armyItems.size(); i++) {
            ArmyItem armyItem = armyItems.get(i);

            ContentValues values = new ContentValues();

            values.put(UserArmyDBHelper.COL_QUANTITY, armyItem.getOwn());
            values.put(UserArmyDBHelper.COL_NAME, armyItem.getName());
            values.put(UserArmyDBHelper.COL_IMAGE, armyItem.getImage());
            values.put(UserArmyDBHelper.COL_PRICE, armyItem.getPrice());

            getContentResolver().insert(ArmyProvider.CONTENT_LEBANON_URI, values);
            getContentResolver().insert(ArmyProvider.CONTENT_URI, values);
        }
    }
}
