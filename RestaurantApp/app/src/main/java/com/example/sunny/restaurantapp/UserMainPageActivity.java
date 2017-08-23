package com.example.sunny.restaurantapp;

import android.app.Application;
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

public class UserMainPageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "UserMainPageActivity";
    protected static final int STARTING_LAND = 20000;
    protected static final int STARTING_MONEY = 20;
    protected static final String STARTING_DATE = "23/07/2017";

    private Context context;
    private Intent intent;
    private FragmentManager manager;
    private SharedPreferences userInfoSP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_page);

        userInfoSP = getSharedPreferences(getString(R.string.user_info), MODE_PRIVATE);
        boolean firsTimeRun = userInfoSP.getBoolean(getString(R.string.first_time_run), false);

        if (firsTimeRun){
            userInfoSP.edit()
                    .putBoolean(getString(R.string.first_time_run), true)
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
                // TODO add the logout from google account
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(UserMainPageActivity.this, MainActivity.class);
//                startActivity(intent);
                break;
        }
    }

//    protected void signOut() {
//        Auth.GoogleSignInApi.signOut(userInfo.getGoogleApiClient()).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        // ...
//                    }
//                });
//    }
}
