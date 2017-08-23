package com.example.sunny.restaurantapp;

/**
 * Created by Sunny on 27/06/2017.
 */

public class ArmyItem {

    private int itemId;
    private String image;
    private String name;
    private int price;
    private int own;

    public ArmyItem(String image, String name, int price) {
        this.image = image;
        this.name = name;
        this.price = price;
        this.own = 0;
    }

    public ArmyItem(int itemId, String image, String name, int price, int own) {
        this.itemId = itemId;
        this.image = image;
        this.name = name;
        this.price = price;
        this.own = own;
    }

    public String getImage() {
        return image;
    }

    public int getItemId() {
        return itemId;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getOwn() {
        return own;
    }

    public void setOwn(int own) {
        this.own = own;
    }
}
