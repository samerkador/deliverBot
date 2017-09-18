package com.example.samer.gpapp;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Samer on 2/12/2017.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {



    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<RowItem>> _listDataChild;
    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<RowItem>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;

    }


    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this._listDataChild.get(this._listDataHeader.get(i))
                .size();
    }

    @Override
    public Object getGroup(int i) {
        return this._listDataHeader.get(i);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }
    final itemList_holder _itemList_holder = new itemList_holder();
    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final RowItem child = (RowItem) getChild(groupPosition, childPosition);
        final  String childText = child.getTitle();
        final int childImage =  child.getImageId();
        final String childPrice = child.getPrice();

        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.layout, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.textView_item);
        ImageView imageView = (ImageView) convertView
                .findViewById(R.id.imageView_item);
        TextView txtListChildPrice = (TextView) convertView.findViewById(R.id.price_item);

        txtListChild.setText(childText);
        imageView.setImageResource(childImage);
        imageView.setTag(childImage);
        txtListChildPrice.setText(childPrice + "$");


        return convertView;

    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
