package com.example.sunny.restaurantapp;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    public static final int DAILY_MONEY = 10;

    private FragmentManager manager;
    private ArmyProvider provider;
    private SharedPreferences sp;
    private TextView textLand;
    private TextView textMoney;
    private TextView textDate;
    private TextView textMilitarySize;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        provider = new ArmyProvider();

        textLand = (TextView) root.findViewById(R.id.textLand);
        textMoney = (TextView) root.findViewById(R.id.textMoney);
        textDate = (TextView) root.findViewById(R.id.textDate);
        textMilitarySize = (TextView) root.findViewById(R.id.textMilitarySize);

        sp = getActivity().getSharedPreferences(getString(R.string.user_info), MODE_PRIVATE);

        int land = sp.getInt(getString(R.string.save_land), UserMainPageActivity.STARTING_LAND);
        textLand.setText(land + "km");

        getUserInformation();

        manager = getFragmentManager();

        root.findViewById(R.id.buttonBuyArmy).setOnClickListener(this);
        root.findViewById(R.id.buttonSpy).setOnClickListener(this);
        root.findViewById(R.id.buttonTech).setOnClickListener(this);
        root.findViewById(R.id.buttonWar).setOnClickListener(this);
        root.findViewById(R.id.buttonNews).setOnClickListener(this);
        root.findViewById(R.id.buttonRank).setOnClickListener(this);
        root.findViewById(R.id.buttonDiplomacy).setOnClickListener(this);
        root.findViewById(R.id.buttonNext).setOnClickListener(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInformation();
    }

    public void getUserInformation(){

        int money = sp.getInt(getString(R.string.save_money), 0);

        textMilitarySize.setText(0 + "");

        try {
            textMilitarySize.setText(getMilitarySize() + "");
        }catch (NullPointerException e){
            Log.d("DB", "The database was empty");
        }

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String stringDate = sp.getString(getString(R.string.save_date), "");
        Date date;
        try {
            date = format.parse(stringDate);
            textDate.setText(format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        textMoney.setText(money + "M");

    }

    public long getMilitarySize() {

        String[] myProjection = {UserArmyDBHelper.COL_QUANTITY};
        Cursor cursor = getActivity().getContentResolver().query(ArmyProvider.CONTENT_URI, null, null, null, null);

        long militarySize = 0;

        while (cursor.moveToNext()){

            militarySize += cursor.getInt(cursor.getColumnIndex(UserArmyDBHelper.COL_QUANTITY));

        }
        return militarySize;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.buttonBuyArmy:
                manager.beginTransaction()
                        .replace(R.id.mainContainer, new BuyArmyFragment())
                        .addToBackStack("")
                        .commit();
                break;

            case R.id.buttonSpy:

                break;

            case R.id.buttonTech:

                break;

            case R.id.buttonWar:

                break;

            case R.id.buttonNews:

                break;

            case R.id.buttonRank:

                break;

            case R.id.buttonDiplomacy:

                break;

            case R.id.buttonNext:
                String sDate = sp.getString(getString(R.string.save_date), "");
//                String month = date.substring(3, 5);
//                Toast.makeText(getContext(), date + "", Toast.LENGTH_SHORT).show();

                int money = sp.getInt(getString(R.string.save_money), 0);
                money += DAILY_MONEY;
                sp.edit()
                        .putInt(getString(R.string.save_money), money)
                        .commit();
                break;

        }
    }
}
