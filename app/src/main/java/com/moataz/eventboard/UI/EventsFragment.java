package com.moataz.eventboard.UI;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.moataz.MultiDexApplication.eventboard.R;
import com.moataz.eventboard.DataUtil.EventsAdapter;
import com.moataz.eventboard.ParserUtil.Event;
import com.moataz.eventboard.ParserUtil.EventResponse;
import com.moataz.eventboard.Syncing.EventsIntentService;


import java.util.ArrayList;
import java.util.List;

import icepick.Icepick;
import icepick.State;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    int PLACE_PICKER_REQUEST = 1;

     EventResponse eResponse;

    public static final String ACTION_RESP =
            "com.moataz.intent.action.EVENTS_PROCESSED";

    public static final String ADDRESS = "ADDRESS";
    public static final String PAGE = "PAGE";
    public static final String LAT = "LAT";
    public static final String LOG = "LOG";
    @State
    public Integer page = 1;

    private static IntentFilter filter;


   List<Event> events;

    @State
    public boolean Firsttime = true;

    @State
     boolean init = true;

    public static final String ARG_PAGE = "ARG_PAGE";
    public View view;
    public RecyclerView rvEvents;


    EventsAdapter adapter;

    SharedPreferences sharedPref;

    @State
     int mPage;


    // TODO: Rename and change types of parameters


    private OnFragmentInteractionListener mListener;

    public EventsFragment() {
        // Required empty public constructor
    }



    public static EventsFragment newInstance(int page) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_PAGE, page);

        fragment.setArguments(args);
        return fragment;
    }
    private EventsResponseReciver receiver;

    public static Context context;
    public static Activity activity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
        context = getContext();
        activity = getActivity();

        setHasOptionsMenu(true);


        filter = new IntentFilter(ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new EventsResponseReciver(new Handler());
        getActivity().registerReceiver(receiver, filter);

if (savedInstanceState != null) {
    if (savedInstanceState.containsKey("events")) {
        if (events == null) {
            events = savedInstanceState.getParcelableArrayList("events");
        }
    }
}
        Icepick.restoreInstanceState(this, savedInstanceState);

    }

    boolean mDualPane;



    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList("events", (ArrayList<? extends Parcelable>) events);
        Icepick.saveInstanceState(this, outState);
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }
    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_events, container, false);
//        TextView tvTitle = (TextView) view.findViewById(R.id.tv);
//        tvTitle.setText("Fragment #" + mPage);
        if (events == null){
            events = new ArrayList<Event>();
        }

        adapter = new EventsAdapter(context, events);
        rvEvents = (RecyclerView)  view.findViewById(R.id.rvEvents);
        // Attach the adapter to the recyclerview to populate items
        rvEvents.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvEvents.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {



            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

               if(page < mPage) {

                   loadNextDataFromApi(page);
               }

               else{
                   Snackbar snackbar = Snackbar
                           .make(view, "No More Events", Snackbar.LENGTH_LONG);

                   snackbar.show();
               }
            }
        };


        rvEvents.addOnScrollListener(scrollListener);


       sharedPref =getActivity().getPreferences(Context.MODE_PRIVATE);

        Float lat = sharedPref.getFloat("lat",5);
        Float log = sharedPref.getFloat("log",5);

        if (init) {
            startService(sharedPref.getString("place", "NY, USA"), "1",lat.toString(),log.toString());
        init = false;
        }


        return view;
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    public void loadNextDataFromApi(int offset) {



        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyDataSetChanged()`

       page = offset+1;
        Float lat = sharedPref.getFloat("lat",5);
        Float log = sharedPref.getFloat("log",5);

        startService(sharedPref.getString("place","NY, USA"), lat.toString(), log.toString(), page.toString());


    }



    public void startService(String address, String page, String lat, String log){
        Intent msgIntent = new Intent(getContext(), EventsIntentService.class);
        msgIntent.putExtra(ADDRESS, address);
        msgIntent.putExtra(PAGE, page);
        msgIntent.putExtra("lat", page);
        msgIntent.putExtra("log", page);


        getActivity().startService(msgIntent);
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


    @Override
    public void setHasOptionsMenu(boolean hasMenu) {
        super.setHasOptionsMenu(hasMenu);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.place:
                pickLocation();
                return true;
            case R.id.about:
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void pickLocation() {


        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == getActivity().RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                String address = place.getAddress().toString();
                LatLngBounds latLng = PlacePicker.getLatLngBounds(data);
                LatLng lg = latLng.getCenter();




                scrollListener.resetState();
                Firsttime = true;
                Double lat = lg.latitude;
                Double log = lg.longitude;
                startService(address,lat.toString(), log.toString(),"1");



                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putFloat("lat", (float) lg.latitude);
                editor.putFloat("long", (float) lg.longitude);
                editor.putString("place",address);
                editor.apply();




            }
        }
}

    public class EventsResponseReciver extends BroadcastReceiver {



        Intent intentt;
        private final Handler handler;

        public EventsResponseReciver(Handler handler) {
            this.handler = handler;
        }



        @Override
        public void onReceive(final Context context, final Intent intent) {
            this.intentt = intent;

            handler.post(new Runnable() {
                @Override
                public void run() {

                    eResponse = intentt.getParcelableExtra("response");


                    if (eResponse != null){
                        mPage = eResponse.getPagination().getPageCount();
                        Log.d("adapter", eResponse.getPagination().getPageCount().toString());



                    if(Firsttime){
                        events.clear();
                        events.addAll(eResponse.events);
                        Firsttime = false;

                    adapter.setOnItemClickListener(new EventsAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Toast.makeText(context," "+ position, Toast.LENGTH_LONG).show();

                            Intent intent1 = new Intent(getActivity(),Detail.class);

                            intent1.putExtra("Event",events.get(position));


                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    // the context of the activity
                                    getActivity(),

                                    // For each shared element, add to this method a new Pair item,
                                    // which contains the reference of the view we are transitioning *from*,
                                    // and the value of the transitionName attribute
                                    new Pair<View, String>(v.findViewById(R.id.imageView),
                                            getString(R.string.transition_name_image))
                                    ,
                                    new Pair<View, String>(view.findViewById(R.id.event_name),
                                            getString(R.string.transition_name_name)),
                                    new Pair<View, String>(view.findViewById(R.id.event_id),
                                            getString(R.string.transition_name_date))
                            );


                            startActivity(intent1,options.toBundle());


                        }

                        @Override
                        public void onItemLongClick(int position, View v) {

                        }
                    });

                } else{

                        events.addAll(eResponse.events);
                        int curSize = adapter.getItemCount();


                        adapter.notifyItemRangeInserted(curSize, events.size() - 1);


                    }




                }

                    else{
                        Snackbar snackbar = Snackbar
                                .make(view, "No Events", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                }

            });





            adapter.notifyDataSetChanged();


        }
    }

}