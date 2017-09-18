package com.example.samer.gpapp;

/**
 * Created by Samer on 2/7/2017.
 */

public class RowItem {
    private String title ,quantity ,price ,category ;
    private int imageId;

    public RowItem(String title , int imageId , String price) {
        this.title = title;
        this.imageId = imageId;
        this.price = price;
    }
    public RowItem(String title , int imageId ) {
        this.title = title;
        this.imageId = imageId;
    }

    public RowItem(String title , String quantity , String price , String  category)
    {
        this.title = title;
        this.price = price;
        this.quantity  = quantity;
        this.category = category;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
