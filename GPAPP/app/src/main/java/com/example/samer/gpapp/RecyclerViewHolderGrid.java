package com.example.samer.gpapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

/**
 *
 * Created by Samer on 3/14/2017.
 */

public class RecyclerViewHolderGrid extends RecyclerView.ViewHolder {

    public Button button;
    public RecyclerViewHolderGrid(View itemView) {
        super(itemView);


        this.button = (Button)itemView.findViewById(R.id.button_grid_loaction);




    }
}
