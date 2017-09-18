package com.example.samer.gpapp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Samer on 2/15/2017.
 */

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
    // View holder for gridview recycler view as we used in listview
    public TextView title;
    public TextView quantity;
    public ImageView imageview;
    //add anim to card view
    public CardView cardView;

    private RvAdapter.ItemClickCallback itemClickCallback;



    public void setItemClickCallback(final RvAdapter.ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    @Override
    public void onClick(View view) {
        itemClickCallback.onItemClick(getAdapterPosition());

    }



    public RecyclerViewHolder(View view ) {
        super(view);

        // Find all views ids

        this.title = (TextView) view
                .findViewById(R.id.title_card);
        this.quantity = (TextView) view.findViewById(R.id.quantity_purchase_card);

        this.imageview = (ImageView) view
                .findViewById(R.id.image_card);

        this.cardView= (CardView) view.findViewById(R.id.card_view);
        cardView.setOnClickListener(this);

    }

    public void clearAnimation()
    {
        cardView.clearAnimation();
    }

}
