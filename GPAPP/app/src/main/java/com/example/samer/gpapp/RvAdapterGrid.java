package com.example.samer.gpapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Samer on 3/14/2017.
 */

public class RvAdapterGrid extends
        RecyclerView.Adapter<RecyclerViewHolderGrid> {


    private Context context;
    private ArrayList<Boolean> arrayList;


    public RvAdapterGrid(Context context,
                     ArrayList<Boolean> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerViewHolderGrid onCreateViewHolder(ViewGroup viewGroup, int viewType) {


        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.grid_item, viewGroup, false);

        RecyclerViewHolderGrid listHolder = new RecyclerViewHolderGrid(mainGroup );

        return listHolder;

    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolderGrid holder, int position) {


        final boolean model
                = arrayList.get(position);






      holder.button.setText(String.valueOf(position + 1));

        if (model )
        holder.button.setBackgroundColor(Color.RED);


//        holder.button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (model)
//                    itemList_holder.location = holder.button.getText().toString();
//                else
//                    Toast.makeText(context,"select right cell of red color" , Toast.LENGTH_SHORT).show();
//
//            }
//        });



    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }
}
