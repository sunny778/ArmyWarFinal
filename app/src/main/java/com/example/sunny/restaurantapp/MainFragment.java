package com.example.sunny.restaurantapp;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.attr.handle;
import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    private static final int DELAY_IN_MILLIS = 2500;
    public static final int USER_DAILY_MONEY = 10;
    public static final int ROBOT_DAILY_MONEY = 7;
    public static final int SOLDIER = 1;
    public static final int TANK = 2;
    public static final int ARTILLERY = 3;
    public static final int F_16 = 4;

    private FragmentManager manager;
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
        String stringDate = sp.getString(getString(R.string.save_date), UserMainPageActivity.STARTING_DATE);
        Date date;
        try {
            date = format.parse(stringDate);
            textDate.setText(format.format(date));
            sp.edit()
                    .putString(getString(R.string.save_date), stringDate)
                    .apply();
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

                manager.beginTransaction()
                        .replace(R.id.mainContainer, new WarCenterFragment())
                        .addToBackStack("")
                        .commit();
                break;

            case R.id.buttonNews:

                break;

            case R.id.buttonRank:

                break;

            case R.id.buttonDiplomacy:

                break;

            case R.id.buttonNext:

                String sDate = sp.getString(getString(R.string.save_date), "");
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                Calendar c = Calendar.getInstance();
                try {
                    c.setTime(sdf.parse(sDate));
                    c.add(Calendar.DATE, 1);
                    sDate = sdf.format(c.getTime());

                    sp.edit()
                            .putString(getString(R.string.save_date), sDate)
                            .apply();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                int money = sp.getInt(getString(R.string.save_money), 0);
                money += USER_DAILY_MONEY;
                sp.edit()
                        .putInt(getString(R.string.save_money), money)
                        .apply();

                sleepTime();
                new RobotBuyArmyTask().execute();
                getUserInformation();

                break;
        }

    }

    public void sleepTime(){
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setTitle("Loading...");
        dialog.setMessage("Please wait.");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, DELAY_IN_MILLIS);
    }

    public class RobotBuyArmyTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            int money = ROBOT_DAILY_MONEY;
            int own;
            int price;
            int[] res;

            while (money > 0){

                Random random = new Random();
                int randBuy = random.nextInt(4) + 1;

                switch (randBuy){

                    case SOLDIER:
                        res = getOwnQuantityItem(SOLDIER);
                        own = res[0] + 1;
                        price = res[1];
                        money -= price;
                        updateItemQuantityDB(SOLDIER, own);
                        break;

                    case TANK:
                        res = getOwnQuantityItem(TANK);
                        own = res[0] + 1;
                        price = res[1];
                        money -= price;
                        updateItemQuantityDB(TANK, own);
                        break;

                    case ARTILLERY:
                        res = getOwnQuantityItem(ARTILLERY);
                        own = res[0] + 1;
                        price = res[1];
                        money -= price;
                        updateItemQuantityDB(ARTILLERY, own);
                        break;

                    case F_16:
                        res = getOwnQuantityItem(F_16);
                        own = res[0] + 1;
                        price = res[1];
                        money -= price;
                        updateItemQuantityDB(F_16, own);
                        break;
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }



    protected int[] getOwnQuantityItem(int itemId){

        int[] res = new int[2];

        Cursor cursor = getActivity().getContentResolver().query(ArmyProvider.CONTENT_ROBOT_URI, null, UserArmyDBHelper.COL_ID + "=" + itemId, null, null);

        while (cursor.moveToNext()) {
            res[0] = cursor.getInt(cursor.getColumnIndex(UserArmyDBHelper.COL_QUANTITY));
            res[1] = cursor.getInt(cursor.getColumnIndex(UserArmyDBHelper.COL_PRICE));
        }

        return res;
    }

    protected void updateItemQuantityDB(int itemId, int quantity){

        ContentValues values = new ContentValues();
        values.put(UserArmyDBHelper.COL_QUANTITY, quantity);
        getContext().getContentResolver().update(ArmyProvider.CONTENT_ROBOT_URI, values, UserArmyDBHelper.COL_ID + "=" + itemId, null);
    }


}
