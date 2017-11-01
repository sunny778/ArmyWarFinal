package com.example.sunny.restaurantapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class StartGameActivity extends Activity implements View.OnClickListener {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        sp = getSharedPreferences(getString(R.string.user_info), MODE_PRIVATE);
        findViewById(R.id.buttonNewGame).setOnClickListener(this);
        findViewById(R.id.buttonExit).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.buttonNewGame:
                sp.edit()
                        .putBoolean(getString(R.string.first_time_run), true)
                        .apply();
                Intent intent = new Intent(this, UserMainPageActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.buttonExit:
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
                break;
        }
    }
}
