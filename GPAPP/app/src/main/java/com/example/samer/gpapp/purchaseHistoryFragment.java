package com.example.samer.gpapp;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link purchaseHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link purchaseHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class purchaseHistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    List<RowItem> rowItems;
    ListView listView;



    public purchaseHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment getDataTestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static purchaseHistoryFragment newInstance(String param1, String param2) {
        purchaseHistoryFragment fragment = new purchaseHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_purchase_history, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rowItems = new ArrayList<>();

        ArrayList<ItemModel> recyclerViews = creatRecyclerViews();
        listView = (ListView) getView().findViewById(R.id.listView_get);

        RecyclerView recyclerView = new RecyclerView(getActivity());


        CustomAdapterListView adapterListView =
                new CustomAdapterListView(getActivity(),R.layout.purchase_history_recycle_view ,recyclerViews , recyclerView);
        listView.setAdapter(adapterListView);
        //listView.setOnItemClickListener(this);
        make_request();

    }

    private void make_request() {


        String url = "http://gpapiandroid.000webhostapp.com/getorderbyuserid.php";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {


                String debug = response;

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Posting params to register url
                itemList_holder it = new itemList_holder();

                Map<String, String> params = new HashMap<String, String>();

                params.put("customers_id","34");
                return params;
            }

        };

    }

    private ArrayList<ItemModel> creatRecyclerViews() {
        ArrayList<ItemModel> recyclerViews = new ArrayList<>();


        for (int i = 0; i < itemList_holder.items.size(); i++) {

            recyclerViews.add(new ItemModel(itemList_holder.items.get(i).getTitle() ,
                    "10" ,itemList_holder.items.get(i).getImageId() ));
        }


        return  recyclerViews;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    //void jsonArrayRequestdata(){
    //JsonArrayRequest jsonArrayRequest = new JsonArrayRequest("http://gpapiandirid.890m.com/getJson.php",
//        new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//
//                try {
//
//
//
//                    JSONArray products =  (JSONArray) response.getJSONArray(1);
//
//                    for (int i = 0; i < products.length(); i++) {
//
//                        JSONObject item = (JSONObject) products.get(i);
//
//                        String name = item.getString("name");
//                        String price = item.getString("price");
//                        String quantity = item.getString("quantity");
//                        String category_name = item.getString("category_name");
//
//                        rowItems.add(new RowItem(name, price,quantity,category_name));
//                    }
//                    ((ArrayAdapter)listView.getAdapter()).notifyDataSetChanged();
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(getActivity(),
//                            "Error: " + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                }
//
//
//
//            }
//        }, new Response.ErrorListener() {
//          @Override
//          public void onErrorResponse(VolleyError error) {
//
//              Toast.makeText(getActivity(),
//                      error.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//        });
//
//
//     AppController.getInstance().addToRequestQueue(jsonArrayRequest);
//}


}
