package com.example.samer.gpapp;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Slide;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class purchase_itemActivity extends AppCompatActivity
        implements Spinner.OnItemSelectedListener , RvAdapter.ItemClickCallback
{

    private static RecyclerView recyclerView;
    private  itemList_holder itemList_holder = new itemList_holder();
    List<RowItem> attachedList;
    TextView user_id,numberOFitems,user_name , total_price , dept;
    SharedPreferences prefs = null;
    RvAdapter adapter;
    ArrayList<ItemModel> arrayList;

    LinearLayoutManager linearLayoutManager;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_purchase_item);
//
//        Slide slide = new Slide(Gravity.BOTTOM);
//        slide.addTarget(R.id.slid_animation);
//        slide.setInterpolator(AnimationUtils.loadInterpolator(this,android.R.interpolator.linear_out_slow_in));
//        slide.setDuration(600);
//        getWindow().setEnterTransition(slide);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_purchase_item);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });


        initViews();

        Spinner spinner = (Spinner) findViewById(R.id.spinner_location);
        spinner.setOnItemSelectedListener(this);
        if (itemList_holder.location != "")
        {
            selectSpinnerItemByValue(spinner , "cell number " + itemList_holder.location);

        }
        populatRecyclerView();



    }


    public static void selectSpinnerItemByValue(Spinner spnr, String value) {
        for (int position = 0; position < spnr.getCount(); position++) {
            String debug = (String)spnr.getItemAtPosition(position);
            if(debug.compareTo(value) == 0) {
                spnr.setSelection(position);
                return;
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                linearLayoutManager.scrollToPosition(0);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        linearLayoutManager.scrollToPosition(0);
        super.onBackPressed();
    }

    private void initViews() {


        progressDialog = new ProgressDialog(this);

        recyclerView = (RecyclerView)
                findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);


        linearLayoutManager = new LinearLayoutManager(purchase_itemActivity.this, LinearLayoutManager.HORIZONTAL, false);

        //Set RecyclerView type according to intent value
        recyclerView
                .setLayoutManager(linearLayoutManager);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        user_id = (TextView)findViewById(R.id.user_id);
        user_name = (TextView)findViewById(R.id.user_name);
        numberOFitems = (TextView)findViewById(R.id.numberOfitem);
        total_price = (TextView)findViewById(R.id.total_price);
        dept = (TextView) findViewById(R.id.dept);

        int num = itemList_holder.items.size();
        numberOFitems.setText(String.valueOf(num));


        prefs = getSharedPreferences("com.example.samer.gpapp", MODE_PRIVATE);
        user_id.setText( prefs.getString(getString(R.string.customers_id), "0"));
        user_name.setText(prefs.getString(getString(R.string.customers_name),"samer"));
        total_price.setText( String.valueOf(getPrice() + "$") );
        getDept();
    }

    private String getDept() {
        String url = "http://gpapiandroid.000webhostapp.com/getjsondeptbyuserid.php";

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonarray = new JSONArray(response);
                    JSONObject jsonObject = jsonarray.getJSONObject(0);
                    dept.setText(jsonObject.getString("Dept") + "$");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();

                params.put("ID",user_id.getText().toString());

                return params;


            }

        };

        AppController.getInstance().addToRequestQueue(strReq);
        return "???";
    }


    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.UP | ItemTouchHelper.DOWN) {


                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        deleteItem(viewHolder.getAdapterPosition());
                    }
                };
        return simpleItemTouchCallback;
    }


    private void moveItem(int oldPos, int newPos) {

        RowItem item = itemList_holder.items.get(oldPos);
        itemList_holder.items.remove(oldPos);
        itemList_holder.items.add(newPos,item);


        ItemModel temp = arrayList.get(oldPos);
        arrayList.remove(oldPos);
        arrayList.add(newPos,temp);

        adapter.notifyItemMoved(oldPos, newPos);

    }

    private void deleteItem(final int position) {


        itemList_holder.items.remove(position);
        arrayList.remove(position);
        adapter.notifyItemRemoved(position);
        total_price.setText(getPrice());
        numberOFitems.setText(itemList_holder.items.size());
    }


    private void populatRecyclerView() {
        arrayList = new ArrayList<>();

        for (int i = 0; i < itemList_holder.items.size(); i++) {

            arrayList.add(new ItemModel(itemList_holder.items.get(i).getTitle() ,
                    itemList_holder.items.get(i).getPrice() + "$" ,itemList_holder.items.get(i).getImageId() ));
        }

        adapter = new RvAdapter(purchase_itemActivity.this, arrayList , this , false);
        recyclerView.setAdapter(adapter);// set adapter on recyclerview
        adapter.notifyDataSetChanged();// Notify the adapter


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String[] loc = purchase_itemActivity.this.getResources().getStringArray(R.array.array_locations_value);
        itemList_holder.location = loc[i];

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public void onItemClick(int p) {
        // Toast.makeText(this,"hello touches" + p, Toast.LENGTH_LONG).show();
    }



    public void makeOreder(View view ) {
        if (itemList_holder.location != "" && itemList_holder.items.size() != 0) {
            make_order();
        }else
        {
            if (itemList_holder.location == "")
                Snackbar.make(view, "Select your location first ....", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            else
                Snackbar.make(view, "Select some items first ....", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


        }
    }

    private void make_order() {


        new BackGroundTask(purchase_itemActivity.this).execute();

        progressDialog.setMessage("Requsting Ordere" );
        progressDialog.show();


        String url = "http://gpapiandroid.000webhostapp.com/orderID.php";
        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                String id_order = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    id_order = jsonObject.getString("id");


                    Toast.makeText(getApplication(), "the id of order is equle to "+ id_order , Toast.LENGTH_SHORT);
                } catch (JSONException e) {



                    Toast.makeText(getApplication(), e.getMessage() , Toast.LENGTH_SHORT);

                }


                make_order_item(id_order);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplication(), error.getMessage() , Toast.LENGTH_SHORT);


            }
        }){


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();

                params.put("id",itemList_holder.id);
                params.put("location",itemList_holder.location);
                return params;


            }
        };

        AppController.getInstance().addToRequestQueue(strReq);
    }

    private void make_order_item(final String id_order) {

        String url = "http://gpapiandroid.000webhostapp.com/order_item.php";
        for (int i = 0 ; i < itemList_holder.items.size();i++)
        {

            final int finalI = i;
            StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    String debug ="";

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {



                    String debug ="";
                }
            }){


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();

                    params.put("id",id_order);
                    params.put("name",itemList_holder.items.get(finalI).getTitle());
                    params.put("price",  itemList_holder.items.get(finalI).getPrice() );


                    return params;


                }
            };


            AppController.getInstance().addToRequestQueue(strReq);
        }
        progressDialog.dismiss();

    }


    public int getPrice()
    {
        int price = 0;
        for (int i = 0 ; i < itemList_holder.items.size();i++)
        {
          price += Integer.valueOf(itemList_holder.items.get(i).getPrice());
        }
        return price;
    }



}

