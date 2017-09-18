package com.example.samer.gpapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by My lap on 4/29/2017.
 */

public class BackGroundTask extends AsyncTask<Void, Void, Void> {


    Context context;
    Notification noti;

    public BackGroundTask(Context context)
    {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {


        Toast.makeText(context , "your order is on the way" , Toast.LENGTH_SHORT).show();
        setOrderFlage();

        super.onPreExecute();
    }

    private void setOrderFlage() {
        String url = "http://gpapiandroid.000webhostapp.com/setOrderState0.php";
        StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(strReq);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        String url = "http://gpapiandroid.000webhostapp.com/testbackgroundtask.php";
        final Boolean[] stateCheck = {true};
        while (stateCheck[0])
        {
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {

                    try {
                        JSONObject jsonObject =  (JSONObject)response.getJSONObject(0);
                        String state = jsonObject.getString("orderState");


                        if (state.compareTo("1") == 0)
                            stateCheck[0] = false;

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            AppController.getInstance().addToRequestQueue(jsonArrayRequest);

            if (!stateCheck[0])
            break;

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        return  null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        setOrderFlage();
        Toast.makeText(context , "your order has been arrived " , Toast.LENGTH_SHORT).show();
        notification();

    }

    private void notification() {


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        Intent intent = new Intent();
        PendingIntent pIntent = PendingIntent.getActivity(context,0,intent,0);
        Notification noti = new Notification.Builder(context)
                .setTicker("this my ticker")
                .setContentTitle("the order")
                .setSmallIcon(R.drawable.ic_menu_slideshow)
                .setContentText("your order has reach to your location")
                .setSound(alarmSound)
                .setContentIntent(pIntent).getNotification();

        noti.flags = Notification.FLAG_AUTO_CANCEL;





        NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,noti);

    }


}
