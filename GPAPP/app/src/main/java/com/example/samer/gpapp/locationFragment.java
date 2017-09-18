package com.example.samer.gpapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link locationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link locationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class locationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView rView;
    ArrayList<Boolean> rowListItem;
    public locationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment locationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static locationFragment newInstance(String param1, String param2) {
        locationFragment fragment = new locationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    private GridLayoutManager lLayout;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {



        rowListItem = getMap();
        lLayout = new GridLayoutManager(getActivity(), 5);

        rView = (RecyclerView)getView().findViewById(R.id.recycler_view_location);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);

        RvAdapterGrid rcAdapter = new RvAdapterGrid(getActivity(), rowListItem);
        rView.setAdapter(rcAdapter);


        super.onViewCreated(view, savedInstanceState);

       }


    public void setLocationGrid(View view)
    {
        LinearLayout linearLayout = (LinearLayout)view.getParent();

        Button locationGridButton = (Button)linearLayout.findViewById(R.id.button_grid_loaction);

        int pos = rView.getChildLayoutPosition(linearLayout);


        if (rowListItem.get(pos))
            itemList_holder.location = locationGridButton.getText().toString();
        else
            Toast.makeText(getActivity(),"please select one of red cells color" , Toast.LENGTH_SHORT).show();


    }

    public ArrayList<Boolean> getMap() {

        ArrayList<Boolean> map = new ArrayList(){{
            add(false);add(false);add(true);add(true);add(true);
            add(false);add(true);add(true);add(true);add(true);
            add(false);add(false);add(true);add(true);add(true);
            add(false);add(false);add(true);add(true);add(true);
            add(false);add(false);add(false);add(false);add(true);
            add(false);add(false);add(false);add(false);add(true);
            add(false);add(false);add(false);add(false);add(true);

        }};


        return map;
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
        return inflater.inflate(R.layout.fragment_location, container, false);
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


}
