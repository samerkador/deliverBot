package com.example.samer.gpapp;

/**
 * Created by Samer on 2/15/2017.
 */

public class ItemModel {
    // Getter and Setter model for recycler view items
    private String title, quantity;
    private int image;

    public ItemModel(String title, String quantity, int image) {

        this.title = title;
        this.quantity = quantity;
        this.image = image;
    }

    public ItemModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    public int getImage() {
        return image;
    }

    public String getQuantity() {
        return quantity;
    }

}
