package com.example.samer.gpapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 *
 *
 *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link mainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link mainFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */

public class mainFragment extends Fragment implements RvAdapter.ItemClickCallback{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    final itemList_holder _itemList_holder = new itemList_holder();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<RowItem>> listDataChild;


    ArrayList<ItemModel> arrayList;
    private static RecyclerView recyclerView;
    RvAdapter adapter = null;
    LinearLayoutManager linearLayoutManager;



    @Override
    public void onResume() {
        super.onResume();

        if ( adapter.getItemCount() != itemList_holder.items.size())
            changeRecView();

        adapter.notifyDataSetChanged();
    }


    private void changeRecView() {
        arrayList = new ArrayList<>();

        for (int i = 0; i < itemList_holder.items.size(); i++) {

            arrayList.add(new ItemModel(itemList_holder.items.get(i).getTitle() ,
                    "10" ,itemList_holder.items.get(i).getImageId() ));
        }

        adapter = new RvAdapter(getActivity(), arrayList , this, false);
        recyclerView.setAdapter(adapter);// set adapter on recyclerview


        adapter.notifyDataSetChanged();// Notify the adapter


    }



    public mainFragment() {
        // Required empty public constructor
    }

    /**
     *
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment mainFrament.
     *
     *
     */

    public static mainFragment newInstance(String param1, String param2) {
        mainFragment fragment = new mainFragment();
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



        View view = inflater.inflate(R.layout.fragment_main, container, false);

        if (adapter != null)
            adapter.notifyDataSetChanged();
        // Inflate the layout for this fragment


        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);
        prepareListData();
        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view,
                                        int groupPosition, int childPosition, long l) {
                Toast.makeText(getActivity(), "this hte item index of " + childPosition , Toast.LENGTH_SHORT).show();

                return false;
            }
        });



//--------------------------------------------------------------------------------------------

        arrayList = new ArrayList<>();

        recyclerView = (RecyclerView)
                view.findViewById(R.id.recycler_view_main);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        //Set RecyclerView type according to intent value
        recyclerView
                .setLayoutManager(linearLayoutManager);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter = new RvAdapter(getActivity(), arrayList , this, true);
        recyclerView.setAdapter(adapter);// set adapter on recyclerview
        adapter.notifyDataSetChanged();// Notify the adapter



        return view;

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
    public void onItemClick(int p) {

    }

    public  void fab2Click() {

        linearLayoutManager.scrollToPosition(0);

        Intent myIntent = new Intent(getActivity() , purchase_itemActivity.class);



//        getActivity().getWindow().setEnterTransition(new Fade(Fade.OUT));
//        myIntent.putExtra("RVAdapter" , adapter);

//        Bundle bundle = new Bundle();
//        bundle.putSerializable("RVAdapter" , adapter);
//        myIntent.putExtras(bundle);

        ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                new Pair<View, String>(getView().findViewById(R.id.recycler_view_main), getString(R.string.transtion))
        );
        ActivityCompat.startActivity(getActivity(),myIntent,option.toBundle());
        //MainActivity.this.startActivity(myIntent);
  //      getActivity().overridePendingTransition(R.anim.slide_down, R.anim.slide_up);

    }

    /**
     *
     *
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }



    public void add_item(View view) {
        RelativeLayout vwParentRow = (RelativeLayout)view.getParent();

        TextView textView = (TextView) vwParentRow.findViewById(R.id.textView_item);
        TextView textView1 = (TextView) vwParentRow.findViewById(R.id.price_item);
        ImageView imageView = (ImageView) vwParentRow.findViewById(R.id.imageView_item);
        //FloatingActionButton btnChild = (FloatingActionButton)vwParentRow.findViewById(R.id.item_button);


        int drawableId = Integer.parseInt(imageView.getTag().toString() );
        itemList_holder.items.add(new RowItem(textView.getText().toString() ,  drawableId ,  textView1.getText().toString().substring(0 ,textView1.getText().toString().length() - 1 ) ));

        int postion = itemList_holder.items.size() - 1;
        String price = "10";
        ItemModel temp =   new ItemModel(itemList_holder.items.get( postion).getTitle() ,
                price ,itemList_holder.items.get(postion).getImageId() );

        arrayList.add(temp);

        adapter.notifyItemInserted(postion);

        linearLayoutManager.scrollToPosition(postion);


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
    }





    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<RowItem>>();

        // Adding child data
        listDataHeader.add("chips");
        listDataHeader.add("chocolate");
        listDataHeader.add("cans");
        listDataHeader.add("ToDo");

        // Adding child data
        List<RowItem> chips = new ArrayList<RowItem>();

        chips.add(new RowItem("lays-limon",R.drawable.lays_limon , "5"));
        chips.add(new RowItem("lays-classic", R.drawable.lays_classic , "5"));
        chips.add(new RowItem("chipsy_chess" , R.drawable.chipsy_chess , "5"));
        chips.add(new RowItem("chipsy_limon",R.drawable.download ,"5"));



        List<RowItem> chocolate  = new ArrayList<RowItem>();
        chocolate.add(new RowItem("Mars" , R.drawable.mars , "10"));
        chocolate.add(new RowItem("Dairy Milk", R.drawable.dairymilk , "10"));
        chocolate.add(new RowItem("Galaxy", R.drawable.galaxy , "10"));
        chocolate.add(new RowItem("Snickers" , R.drawable.snickers , "10"));
        chocolate.add(new RowItem("KitKat" , R.drawable.kitkat , "10"));
        chocolate.add(new RowItem("Twix" , R.drawable.twix , "10"));



        List<RowItem> cans = new ArrayList<RowItem>();
        cans.add(new RowItem("Coca Cola" , R.drawable.cocacola));
//      cans.add(new RowItem("Crush Orange" , R.drawable.crushorange));
//      cans.add(new RowItem("fanta Orange" , R.drawable.fantaorange));
//      cans.add(new RowItem("Mountain Dew" , R.drawable.mtndew));
        cans.add(new RowItem("Pepsi" , R.drawable.pepsi , "5"));
        cans.add(new RowItem("Pepsi Diet" , R.drawable.pepsidiet , "5"));
        cans.add(new RowItem("Red Bull" , R.drawable.redbull , "5"));
//      cans.add(new RowItem("sprite" , R.drawable.sprite));



        List<RowItem> ToDo = new ArrayList<RowItem>();
        ToDo.add(new RowItem("todo brownies",R.drawable.todo_brownies  , "3"));
//      ToDo.add(new RowItem("todo chocolate roll",R.drawable.todo_chocolate_roll));
        ToDo.add(new RowItem("todo bomb",R.drawable.todo_bomb , "3"));



        listDataChild.put(listDataHeader.get(0), chips); // Header, Child data
        listDataChild.put(listDataHeader.get(1), chocolate);
        listDataChild.put(listDataHeader.get(2), cans);
        listDataChild.put(listDataHeader.get(3), ToDo);

    }




}
