package com.example.samer.gpapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Samer on 2/15/2017.
 */

public class RvAdapter extends
        RecyclerView.Adapter<RecyclerViewHolder>
        implements Serializable {

    // recyclerview adapter
    private ArrayList<ItemModel> arrayList;
    private Context context;
    private int lastPosition=-1;
    boolean animate;


    private ItemClickCallback itemClickCallback;



    public interface ItemClickCallback {
        void onItemClick(int p);
    }





    public RvAdapter(Context context,
                     ArrayList<ItemModel> arrayList , final ItemClickCallback itemClickCallback , boolean animate) {
        this.context = context;
        this.arrayList = arrayList;
        this.itemClickCallback = itemClickCallback;
        this.animate = animate;
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);

    }



    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        final ItemModel model = arrayList.get(position);

        RecyclerViewHolder mainHolder = (RecyclerViewHolder) holder;// holder



        Bitmap image = BitmapFactory.decodeResource(context.getResources(),
                model.getImage());// This will convert drawbale image into bitmap

        // setting title
        mainHolder.title.setText(model.getTitle());
        mainHolder.quantity.setText(model.getQuantity());
        mainHolder.imageview.setImageBitmap(image);

        //add anim to card view
        if (animate)
            setAnimation(holder.cardView, position);



    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(
                R.layout.card_item, viewGroup, false);

        RecyclerViewHolder listHolder = new RecyclerViewHolder(mainGroup );
        listHolder.setItemClickCallback(itemClickCallback);

        return listHolder;

    }



    //
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated

        if (position > lastPosition)
        {

            Animation animation = AnimationUtils.
                    loadAnimation(context, (position > lastPosition) ? R.anim.slide_down : R.anim.slide_up);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
//            if (position > 2)
//            {
//                animation = AnimationUtils.
//                        loadAnimation(context, R.anim.slide_up);
//                viewToAnimate.startAnimation(animation);
//            }
        }
        else
        {
            //    Animation animation = AnimationUtils.loadAnimation(context,
            //(position > lastPosition)? R.anim.slide_down : R.anim.slide_up);
            //viewToAnimate.startAnimation(animation);
            //lastPosition = position;
        }




    }
    //to solve the problem of fast scroll
    @Override
    public void onViewDetachedFromWindow(RecyclerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        ((RecyclerViewHolder)holder).clearAnimation();
    }
}
