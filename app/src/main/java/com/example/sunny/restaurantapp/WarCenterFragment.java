package com.example.sunny.restaurantapp;


import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.HandlerThread;
import android.os.MessageQueue;
import android.support.annotation.MainThread;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


/**
 * A simple {@link Fragment} subclass.
 */
public class WarCenterFragment extends Fragment implements View.OnClickListener {

    private static final int DELAY = 2500;
    public static final int SOLDIER_POWER = 10;
    public static final int TANK_POWER = 40;
    public static final int ARTILLERY_POWER = 85;
    public static final int F_16_POWER = 200;
    public static final double ARMY_LOST = 0.75;
    public static final int DESTROYED_QUANTITY = 3;

    private boolean lebanonIsOccupied = false;
    private boolean iranIsOccupied = false;
    private ImageButton imageButtonLebanon;
    private ImageButton imageButtonIran;
    private ArrayList<ArmyItem> userArmy;
    private ArrayList<ArmyItem> robotArmy;
    private double robotArmyPower;
    private double userArmyPower;
    private Handler handler;

    private SharedPreferences sp;


    public WarCenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_war_center, container, false);

        sp = getActivity().getSharedPreferences(getString(R.string.user_info), getActivity().MODE_PRIVATE);
        userArmy = new ArrayList<>();
        robotArmy = new ArrayList<>();

        imageButtonLebanon = (ImageButton) root.findViewById(R.id.imageButtonLebanon);
        imageButtonIran = (ImageButton) root.findViewById(R.id.imageButtonIran);
        handler = new ConsoleHandler();

        imageButtonLebanon.setOnClickListener(this);
        imageButtonIran.setOnClickListener(this);
        imageButtonLebanon.setVisibility(View.VISIBLE);
        imageButtonIran.setVisibility(View.VISIBLE);

        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.imageButtonLebanon:
                robotArmy = getQuantity(ArmyProvider.CONTENT_LEBANON_URI);
                userArmy = getQuantity(ArmyProvider.CONTENT_URI);

                robotArmyPower = calcTheArmyPower(robotArmy);
                userArmyPower = calcTheArmyPower(userArmy);

                if (robotArmyPower > userArmyPower){
                    showDialogResult("You LOST this battle!");
                    calcArmiesAfterBattle(ArmyProvider.CONTENT_URI, userArmy);
                }else{
                    showDialogResult("You WON this battle!!");
                    calcArmiesAfterBattle(ArmyProvider.CONTENT_LEBANON_URI, robotArmy);
                }
                winTheGameChecker();
                break;

            case R.id.imageButtonIran:
                robotArmy = getQuantity(ArmyProvider.CONTENT_IRAN_URI);
                userArmy = getQuantity(ArmyProvider.CONTENT_URI);

                robotArmyPower = calcTheArmyPower(robotArmy);
                userArmyPower = calcTheArmyPower(userArmy);

                if (robotArmyPower > userArmyPower){
                    showDialogResult("You LOST this battle!");
                    calcArmiesAfterBattle(ArmyProvider.CONTENT_URI, userArmy);
                }else{
                    showDialogResult("You WON this battle!!");
                    calcArmiesAfterBattle(ArmyProvider.CONTENT_IRAN_URI, robotArmy);
                }
                winTheGameChecker();
                break;
        }
    }

    protected void winTheGameChecker(){
        if ((iranIsOccupied == true) && (lebanonIsOccupied == true)){

            showDialogResult("Congratulations, you won the game!!");
            sleeping();
            sp.edit()
                    .putBoolean(getString(R.string.first_time_run), true)
                    .apply();
            deleteAllDb();
            new CountDownTimer(3000, 1000){

                @Override
                public void onTick(long l) {

                }

                @Override
                public void onFinish() {
                    Intent intent = new Intent(getContext(), StartGameActivity.class);
                    startActivity(intent);
                }
            }.start();
        }
    }

    protected void showDialogResult(String message){
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("War Result!")
                .setMessage(message)
                .setNegativeButton("OK", null)
                .create();
        dialog.show();
    }

    //TODO check if need to add army lost for the winner army too!!
    protected void calcArmiesAfterBattle(Uri uri, ArrayList<ArmyItem> armyItems){
        int quantity;
        int totalQuantity = 0;

        for (int i = 0; i < armyItems.size(); i++) {
            quantity = armyItems.get(i).getOwn();
            if (quantity > 0) {
                quantity *= ARMY_LOST;
                totalQuantity += quantity;
                updateItemQuantityDB(uri, armyItems.get(i).getItemId(), quantity);
            }
        }
        // TODO add Land space if user army occupied some army
        if (totalQuantity < DESTROYED_QUANTITY){
            if (uri == ArmyProvider.CONTENT_URI){
                showDialogResult("Game Over, Sorry but your army is destroyed");
                sp.edit()
                        .putBoolean(getString(R.string.first_time_run), true)
                        .apply();
                sleeping();
                deleteAllDb();
                Intent intent = new Intent(getContext(), StartGameActivity.class);
                startActivity(intent);
            }else if (uri == ArmyProvider.CONTENT_LEBANON_URI){
                showDialogResult("Good Work, Your army occupied Lebanon");
                imageButtonLebanon.setVisibility(View.INVISIBLE);
                lebanonIsOccupied = true;
            }else if (uri == ArmyProvider.CONTENT_IRAN_URI) {
                showDialogResult("Good Work, Your army occupied Iran");
                imageButtonIran.setVisibility(View.INVISIBLE);
                iranIsOccupied = true;
            }
        }
    }

    private void sleeping() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loading...");
        dialog.setMessage("Please wait.");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.show();

        long delayInMillis = DELAY;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, delayInMillis);
    }

    protected double calcTheArmyPower(ArrayList<ArmyItem> armyItems){

        double armyPower = 0;
        int own;

        for (int i = 0; i < armyItems.size(); i++) {

            own = armyItems.get(i).getOwn();

            switch (armyItems.get(i).getItemId()) {

                case MainFragment.SOLDIER:
                    armyPower += SOLDIER_POWER * own;
                    break;

                case MainFragment.TANK:
                    armyPower += TANK_POWER * own;
                    break;

                case MainFragment.ARTILLERY:
                    armyPower += ARTILLERY_POWER * own;
                    break;

                case MainFragment.F_16:
                    armyPower += F_16_POWER * own;
                    break;
            }
        }

        return armyPower;
    }

    protected ArrayList<ArmyItem> getQuantity(Uri uri){

        ArrayList<ArmyItem> armyItems = new ArrayList<>();

        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(UserArmyDBHelper.COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(UserArmyDBHelper.COL_NAME));
            String image = cursor.getString(cursor.getColumnIndex(UserArmyDBHelper.COL_IMAGE));
            int quantity = cursor.getInt(cursor.getColumnIndex(UserArmyDBHelper.COL_QUANTITY));
            int price = cursor.getInt(cursor.getColumnIndex(UserArmyDBHelper.COL_PRICE));

            armyItems.add(new ArmyItem(id, image, name, price, quantity));
        }

        return armyItems;
    }

    public void updateItemQuantityDB(Uri uri, int itemId, int quantity){

        ContentValues values = new ContentValues();
        values.put(UserArmyDBHelper.COL_QUANTITY, quantity);
        getActivity().getContentResolver().update(uri, values, UserArmyDBHelper.COL_ID + "=" + itemId, null);
    }

    public void deleteAllDb(){
        getActivity().getContentResolver().delete(ArmyProvider.CONTENT_URI, null, null);
        getActivity().getContentResolver().delete(ArmyProvider.CONTENT_IRAN_URI, null, null);
        getActivity().getContentResolver().delete(ArmyProvider.CONTENT_LEBANON_URI, null, null);
    }
}
