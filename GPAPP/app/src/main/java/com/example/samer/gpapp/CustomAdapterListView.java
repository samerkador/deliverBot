package com.example.samer.gpapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samer on 2/7/2017.
 */

public class CustomAdapterListView extends ArrayAdapter
        implements RvAdapter.ItemClickCallback{

    Context context;
    Activity activity;
    List<RowItem> rowItems;
    LayoutInflater mInflater;
    ArrayList<ItemModel> recyclerViews;
    RecyclerView countRV;
//
//    public CustomAdapterListView(@NonNull Context context, @LayoutRes int resource, @NonNull List<RowItem> objects) {
//
//        super(context, resource, objects);
//        this.context = context;
//        this.rowItems = objects;
//    }



    public CustomAdapterListView(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<ItemModel> objects, RecyclerView countRV) {

        super(context, resource, objects);
        this.context = context;
        this.recyclerViews = objects;
        this.countRV = countRV;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return countRV;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public void onItemClick(int p) {

    }

    private static class ViewHolder
    {
        RecyclerView recyclerView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if (mInflater == null)
            mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (view == null)
        {
            view = mInflater.inflate(R.layout.purchase_history_recycle_view, null);
        }
        viewHolder = new ViewHolder();

        viewHolder.recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_purchase_history);
        //RecyclerView rowItem = recyclerViews.get(i);


        viewHolder.recyclerView.setHasFixedSize(true);

        LinearLayoutManager
                linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);

        //Set RecyclerView type according to intent value
        viewHolder.recyclerView
                .setLayoutManager(linearLayoutManager);



        ArrayList<ItemModel> temp = creatRecyclerViews();
        RvAdapter adapter = new RvAdapter( context , temp , this , true);

        viewHolder.recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }



    private ArrayList<ItemModel> creatRecyclerViews() {
        ArrayList<ItemModel> recyclerViews = new ArrayList<>();


        for (int i = 0; i < itemList_holder.items.size(); i++) {

            recyclerViews.add(new ItemModel(itemList_holder.items.get(i).getTitle() ,
                    "10" ,itemList_holder.items.get(i).getImageId() ));
        }


        return  recyclerViews;
    }

}
