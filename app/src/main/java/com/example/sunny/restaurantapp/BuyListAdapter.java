package com.example.sunny.restaurantapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sunny on 27/06/2017.
 */

public class BuyListAdapter extends RecyclerView.Adapter<BuyListAdapter.BuyListHolder> {

    private Context context;
    private ArrayList<ArmyItem> armyItems;
    private SharedPreferences sp;
    private int money = 0;


    public BuyListAdapter(Context context, ArrayList<ArmyItem> armyItems) {
        this.context = context;
        this.armyItems = armyItems;
    }

    public void setArmyItems(ArrayList<ArmyItem> armyItems) {
        this.armyItems = armyItems;
        notifyDataSetChanged();
    }

    @Override
    public BuyListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.buy_item_style, parent, false);

        sp = context.getSharedPreferences(context.getString(R.string.user_info), Context.MODE_PRIVATE);
        money = sp.getInt(context.getString(R.string.save_money), 0);

        return new BuyListHolder(view);
    }

    @Override
    public void onBindViewHolder(BuyListHolder holder, int position) {
        holder.bind(armyItems.get(position));
    }

    @Override
    public int getItemCount() {
        if (armyItems == null){
            return 0;
        }
        return armyItems.size();
    }

    public class BuyListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageItem;
        private TextView name;
        private TextView price;
        private TextView own;
        private TextView textBuyQuantity;
        private TextView total;
        private ArmyItem armyItem;
        private int buyQuantity = 0;
        private int totalPrice = 0;
        private int ownQuantity = 0;

        public BuyListHolder(View itemView) {
            super(itemView);

            imageItem = (ImageView) itemView.findViewById(R.id.imageItem);
            name = (TextView) itemView.findViewById(R.id.textName);
            price = (TextView) itemView.findViewById(R.id.textPrice);
            own = (TextView) itemView.findViewById(R.id.textOwn);
            textBuyQuantity = (TextView) itemView.findViewById(R.id.textBuyQuantity);
            total = (TextView) itemView.findViewById(R.id.textTotal);

            itemView.findViewById(R.id.buttonUp).setOnClickListener(this);
            itemView.findViewById(R.id.buttonDown).setOnClickListener(this);
            itemView.findViewById(R.id.buttonBuy).setOnClickListener(this);
        }

        public void bind(ArmyItem armyItem) {
            this.armyItem = armyItem;

            try{
                ownQuantity = getOwnQuantityItem(armyItem.getItemId());
                armyItem.setOwn(ownQuantity);
            }catch (NullPointerException e){
                Log.e("DB", "The quantity row in the database is empty");
            }
            own.setText(armyItem.getOwn() + "");

            Picasso
                    .with(context)
                    .load(armyItem.getImage())
                    .resize(80, 80)
                    .centerCrop()
                    .into(imageItem);

            name.setText(armyItem.getName());
            price.setText(armyItem.getPrice() + "M");
            own.setText(armyItem.getOwn() + "");
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){

                case R.id.buttonBuy:
                    if (totalPrice > money){
                        Toast.makeText(context, "Sorry you don't have enough money!", Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, money + "", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "You bought " + buyQuantity + " " + armyItem.getName() + "", Toast.LENGTH_SHORT).show();
                        money -= totalPrice;
                        sp.edit()
                                .putInt(context.getString(R.string.save_money), money)
                                .apply();

                        armyItem.setOwn(buyQuantity + armyItem.getOwn());
                        updateItemQuantityDB(armyItem.getItemId(), armyItem.getOwn());
                        own.setText(armyItem.getOwn() + "");
                        buyQuantity = 0;
                    }
                    break;

                case R.id.buttonUp:
                    if (buyQuantity <= 100) {
                        buyQuantity++;
                    }else {
                        Toast.makeText(context, "Sorry you can't buy more than 100 each time", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.buttonDown:
                    if (buyQuantity > 0) {
                        buyQuantity--;
                    }
                    break;
            }

            textBuyQuantity.setText(buyQuantity + "");
            totalPrice = buyQuantity * armyItem.getPrice();
            total.setText(totalPrice + "M");

        }
    }

    public void updateItemQuantityDB(int itemId, int quantity){

        ContentValues values = new ContentValues();
        values.put(UserArmyDBHelper.COL_QUANTITY, quantity);
        context.getContentResolver().update(ArmyProvider.CONTENT_URI, values, UserArmyDBHelper.COL_ID + "=" + itemId, null);
    }

    public int getOwnQuantityItem(int itemId){
        int own = 0;
        Cursor cursor = context.getContentResolver().query(ArmyProvider.CONTENT_URI, null, UserArmyDBHelper.COL_ID + "=" + itemId, null, null);

        while (cursor.moveToNext()) {
            own = cursor.getInt(cursor.getColumnIndex(UserArmyDBHelper.COL_QUANTITY));
        }

        return own;
    }
}
