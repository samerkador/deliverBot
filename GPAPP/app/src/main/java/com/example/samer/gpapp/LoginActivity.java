package com.example.samer.gpapp;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    String id ;
    EditText editText_id , editText_name , editText_pass;

    SharedPreferences prefs = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);






        editText_id = (EditText) findViewById(R.id.login_id);
        editText_name = (EditText) findViewById(R.id.login_name);
        editText_pass = (EditText) findViewById(R.id.login_pass);



        prefs = getSharedPreferences("com.example.samer.gpapp", MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permission();
        }

    }


    public void TextCreatAccount(View view)
    {
        getID();
    }

    void getID()
    {
        String url = "http://gpapiandroid.000webhostapp.com/LoginFake.php   ";
        StringRequest strReq = new StringRequest(Request.Method.GET , url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            id = jsonObject.get("id").toString();

                            editText_id.setText(id);

                        }catch (JSONException e)
                        {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) ;

        AppController.getInstance().addToRequestQueue(strReq);
    }

    public  void LoginFab(View view)
    {

        String url = "http://gpapiandroid.000webhostapp.com/Login.php";
        StringRequest strreq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(getString(R.string.customers_id), id);
                editor.putString(getString(R.string.customers_name), editText_name.getText().toString());

                editor.commit();


                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();


                params.put("id",id);
                params.put("name",editText_name.getText().toString());
                params.put("pass",editText_pass.getText().toString());
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strreq);

        itemList_holder it = new itemList_holder();
        it.id = id;
        finish();
    }


    public void permission()
    {

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )
        {



            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA , Manifest.permission.INTERNET ,
                            Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE} ,2  );

        }

    }



}
