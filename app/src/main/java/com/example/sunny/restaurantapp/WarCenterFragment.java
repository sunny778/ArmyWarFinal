package com.example.sunny.restaurantapp;


import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class WarCenterFragment extends Fragment implements View.OnClickListener {

    public static final int SOLDIER_POWER = 10;
    public static final int TANK_POWER = 40;
    public static final int ARTILLERY_POWER = 85;
    public static final int F_16_POWER = 200;
    public static final double ARMY_LOST = 0.75;

    private ImageButton imageButtonLebanon;
    private ImageButton imageButtonIran;
    private ArrayList<ArmyItem> userArmy;
    private ArrayList<ArmyItem> robotArmy;
    private double robotArmyPower;
    private double userArmyPower;


    public WarCenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_war_center, container, false);

        userArmy = new ArrayList<>();
        robotArmy = new ArrayList<>();

        imageButtonLebanon = (ImageButton) root.findViewById(R.id.imageButtonLebanon);
        imageButtonIran = (ImageButton) root.findViewById(R.id.imageButtonIran);

        imageButtonLebanon.setOnClickListener(this);
        imageButtonIran.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.imageButtonLebanon:
                robotArmy = getQuantity(ArmyProvider.CONTENT_ROBOT_URI);
                userArmy = getQuantity(ArmyProvider.CONTENT_URI);

                robotArmyPower = calcTheArmyPower(robotArmy);
                userArmyPower = calcTheArmyPower(userArmy);

                if (robotArmyPower >= userArmyPower){
                    showDialogResult("You LOST this battle!");
                    theLostArmy(ArmyProvider.CONTENT_URI, userArmy);
                }else{
                    showDialogResult("You WON this battle!!");
                    theLostArmy(ArmyProvider.CONTENT_ROBOT_URI, robotArmy);
                }
                break;

            case R.id.imageButtonIran:

                break;
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

    protected void theLostArmy(Uri uri, ArrayList<ArmyItem> armyItems){
        int quantity;
        int zeroCounter = 0;

        for (int i = 0; i < armyItems.size(); i++) {
            quantity = armyItems.get(i).getOwn();
            if (quantity > 0) {
                quantity *= ARMY_LOST;
                updateItemQuantityDB(uri, armyItems.get(i).getItemId(), quantity);
            }else {
                zeroCounter++;
            }
        }
        if (zeroCounter == armyItems.size()){
            if (uri == ArmyProvider.CONTENT_URI){
                showDialogResult("Game Over, Sorry but your army is destroyed");
            }else if (uri == ArmyProvider.CONTENT_ROBOT_URI){
                showDialogResult("Good Work, Your army occupied Lebanon");
            }
        }
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
}
