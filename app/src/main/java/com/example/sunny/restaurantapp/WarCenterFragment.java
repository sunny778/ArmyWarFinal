package com.example.sunny.restaurantapp;


import android.app.Dialog;
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

    private ImageButton imageButtonLebanon;
    private ImageButton imageButtonIran;
    private double armyQuantity;


    public WarCenterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_war_center, container, false);

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
                double robotArmyQuantity = getQuantity(ArmyProvider.CONTENT_ROBOT_URI);
                double userArmyQuantity = getQuantity(ArmyProvider.CONTENT_URI);

                if (robotArmyQuantity > userArmyQuantity){
                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle("War Result!")
                            .setMessage("You LOST in this battle!!")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                }else{
                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle("War Result!")
                            .setMessage("You WON in this battle!!")
                            .setNegativeButton("OK", null)
                            .create();
                    dialog.show();
                }
                break;

            case R.id.imageButtonIran:

                break;
        }
    }

    protected double getQuantity(Uri uri){

        armyQuantity = 0;

        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);

        while (cursor.moveToNext()) {
            armyQuantity += cursor.getInt(cursor.getColumnIndex(UserArmyDBHelper.COL_QUANTITY));
        }

        return armyQuantity;
    }
}
