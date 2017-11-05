package com.example.sunny.restaurantapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.UiThread;
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
                Intent intent1 = new Intent(Intent.ACTION_MAIN);
                intent1.addCategory(Intent.CATEGORY_HOME);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                break;
        }
    }
}
