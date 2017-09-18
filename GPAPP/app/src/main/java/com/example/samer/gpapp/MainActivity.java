package com.example.samer.gpapp;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Fade;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,mainFragment.OnFragmentInteractionListener,
        purchaseHistoryFragment.OnFragmentInteractionListener, locationFragment.OnFragmentInteractionListener {

    final itemList_holder _itemList_holder = new itemList_holder();
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    // index to identify current nav menu item
    public static int navItemIndex = 0;


    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_PHOTOS = "location";
    private static final String TAG_MOVIES = "history";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    SharedPreferences prefs = null;
    ProgressDialog progressDialog;
    mainFragment homeFragment;
    locationFragment locationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        prefs = getSharedPreferences("com.example.samer.gpapp", MODE_PRIVATE);
        progressDialog = new ProgressDialog(this);


        String customers_id = prefs.getString(getString(R.string.customers_id), "0");

        _itemList_holder.id = customers_id;
        Toast.makeText(getApplication(), "this is the user id " + customers_id , Toast.LENGTH_LONG).show();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (navItemIndex == 0)
                {
                    if (_itemList_holder.location != "" && _itemList_holder.items.size() != 0 )
                        make_order();
                    else
                    {
                        if (_itemList_holder.location == "")
                            Snackbar.make(view, "Select your location first ....", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        else
                            Snackbar.make(view, "Select some items first ....", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();

                    }


                }

            }
        });



//
//        FloatingActionButton fab2 = (FloatingActionButton)findViewById(R.id.fab2);
//        fab2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//
//
//                mainFragment.fab2Click();
//
//            }
//        });
//

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //------------------------------------------------------------------------------------------------------



//--------------------------------------------------------------------------------------------------------
        mHandler = new Handler();
        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

//--------------------------------------------------------------------------------------------------------------





    }

    private void make_order() {


        new BackGroundTask(MainActivity.this).execute();


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

                params.put("id",_itemList_holder.id);
                params.put("location",_itemList_holder.location);
                return params;


            }
        };

        AppController.getInstance().addToRequestQueue(strReq);
    }

    private void make_order_item(final String id_order) {

        String url = "http://gpapiandroid.000webhostapp.com/order_item.php";
        for (int i = 0 ; i < _itemList_holder.items.size();i++)
        {

            final int finalI = i;
            StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();

                    params.put("id",id_order);
                    params.put("name",_itemList_holder.items.get(finalI).getTitle());
                    params.put("price",_itemList_holder.items.get(finalI).getPrice());


                    return params;


                }
            };


            AppController.getInstance().addToRequestQueue(strReq);
        }
        progressDialog.dismiss();

    }


    public void add_item(View view)
    {
        homeFragment.add_item(view);
    }

    public void setLocationGrid(View view)
    {
        locationFragment.setLocationGrid(view);

        changeToolBarTitileBylocation();
    }

    private void changeToolBarTitileBylocation() {

        toolbar.setTitle("your cell is " + _itemList_holder.location);
    }

    public void fab2Click(View view)
    {
        homeFragment.fab2Click();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }


        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
          return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
        } else if (id == R.id.nav_gallery) {
            navItemIndex = 1;
            CURRENT_TAG = TAG_PHOTOS;
        } else if (id == R.id.nav_slideshow) {
            navItemIndex = 2;
            CURRENT_TAG = TAG_MOVIES;
//        } else if (id == R.id.nav_manage) {
//            navItemIndex = 3;
//            CURRENT_TAG = TAG_NOTIFICATIONS;
//
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
        }
        loadHomeFragment();
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        return true;
    }







    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website

        // loading header background image

        // showing dot next to notifications label
        //navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                if (navItemIndex == 0) {
                    if (_itemList_holder.location != "" && _itemList_holder.items.size() != 0) {
                        make_order();
                    }else
                    {
                        if (_itemList_holder.location == "")
                            Snackbar.make(view, "Select your location first ....", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        else
                            Snackbar.make(view, "Select some items first ....", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();


                    }
                }
            }
        });


        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false


            Intent myIntent = new Intent(MainActivity.this, RegistrationActivity.class);
            MainActivity.this.startActivity(myIntent);

            myIntent = new Intent(MainActivity.this, LoginActivity.class);
            MainActivity.this.startActivity(myIntent);


            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();



            String customers_id = prefs.getString(getString(R.string.customers_id), "0");

            _itemList_holder.id = customers_id;
            Toast.makeText(getApplication(), "this is the user id " + customers_id , Toast.LENGTH_LONG).show();
        }



    }


    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in,
                                android.R.anim.fade_out).replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                homeFragment = new mainFragment();
                return homeFragment;
            case 1:
                // photos
                 locationFragment = new locationFragment();
                return locationFragment;
            case 2:
                purchaseHistoryFragment purchaseHistoryFragment = new purchaseHistoryFragment();
                return purchaseHistoryFragment;
            case 3:

            default:
                return new mainFragment();
        }
    }
    private void setToolbarTitle() {
        //       getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    // show or hide the fab
    private void toggleFab() {


        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        FloatingActionButton fab2 = (FloatingActionButton)findViewById(R.id.fab2);

        toolbar.setTitle(getResources().getString(R.string.app_name));
        if (navItemIndex == 0) {
            fab.show();
            fab2.show();
        }
        else {
            fab.hide();
            fab2.hide();
            if (_itemList_holder.location.compareTo("") == 0)
            toolbar.setTitle("Select Cell");
            else
                toolbar.setTitle("your cell is " + _itemList_holder.location);

        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


}
