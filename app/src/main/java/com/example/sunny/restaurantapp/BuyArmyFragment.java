package com.example.sunny.restaurantapp;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuyArmyFragment extends Fragment {

    private BuyListAdapter adapter;
    private ArrayList<ArmyItem> armyItems;
    private RecyclerView recyclerView;
    private SharedPreferences sp;
    private int money;

    public BuyArmyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_buy_army, container, false);

        armyItems = new ArrayList<>();
        sp = getActivity().getPreferences(Context.MODE_PRIVATE);
        money = sp.getInt(getString(R.string.save_money), UserMainPageActivity.STARTING_MONEY);

        recyclerView = (RecyclerView) root.findViewById(R.id.buyList);

        Cursor cursor = getActivity().getContentResolver().query(ArmyProvider.CONTENT_URI, null, null, null, null);

        if(cursor.moveToNext()){
            getDBItems();
        }else {
            armyItems.add(new ArmyItem("https://www.municak.sk/images/decoy_soldier.png", "Soldier", 1));
            armyItems.add(new ArmyItem("http://www.freeiconspng.com/uploads/tank-icon-30.png", "Tank", 3));
            armyItems.add(new ArmyItem("http://vignette4.wikia.nocookie.net/armedassault/images/8/82/Arma3-render-sochor.png/revision/latest?cb=20140211061238", "Artillery", 5));
            armyItems.add(new ArmyItem("http://www.countercurrents.org/f15.png", "F-16", 10));
            insertItemsIntoDB(armyItems);
        }

        adapter = new BuyListAdapter(getContext(), armyItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return root;
    }


    protected void insertItemsIntoDB(ArrayList<ArmyItem> armyItems){

        for (int i = 0; i < armyItems.size(); i++) {
            ArmyItem armyItem = armyItems.get(i);

            ContentValues values = new ContentValues();

            values.put(UserArmyDBHelper.COL_QUANTITY, armyItem.getOwn());
            values.put(UserArmyDBHelper.COL_NAME, armyItem.getName());
            values.put(UserArmyDBHelper.COL_IMAGE, armyItem.getImage());
            values.put(UserArmyDBHelper.COL_PRICE, armyItem.getPrice());

            getContext().getContentResolver().insert(ArmyProvider.CONTENT_ROBOT_URI, values);
            getContext().getContentResolver().insert(ArmyProvider.CONTENT_URI, values);
        }
    }

    protected void getDBItems(){

        armyItems.clear();

        Cursor cursor = getActivity().getContentResolver().query(ArmyProvider.CONTENT_URI, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(UserArmyDBHelper.COL_ID));
            String name = cursor.getString(cursor.getColumnIndex(UserArmyDBHelper.COL_NAME));
            String image = cursor.getString(cursor.getColumnIndex(UserArmyDBHelper.COL_IMAGE));
            int quantity = cursor.getInt(cursor.getColumnIndex(UserArmyDBHelper.COL_QUANTITY));
            int price = cursor.getInt(cursor.getColumnIndex(UserArmyDBHelper.COL_PRICE));

            armyItems.add(new ArmyItem(id, image, name, price, quantity));
        }
    }

//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        return new CursorLoader(getContext(), ArmyProvider.CONTENT_URI, null, null, null, null);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        armyItems.clear();
//
//        while (data.moveToNext()){
//            int id = data.getInt(data.getColumnIndex(UserArmyDBHelper.COL_ID));
//            String name = data.getString(data.getColumnIndex(UserArmyDBHelper.COL_NAME));
//            String image = data.getString(data.getColumnIndex(UserArmyDBHelper.COL_IMAGE));
//            int quantity = data.getInt(data.getColumnIndex(UserArmyDBHelper.COL_QUANTITY));
//            int price = data.getInt(data.getColumnIndex(UserArmyDBHelper.COL_PRICE));
//
//            armyItems.add(new ArmyItem(id, image, name, price, quantity));
//        }
//
//        adapter.setArmyItems(armyItems);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//
//    }
}
