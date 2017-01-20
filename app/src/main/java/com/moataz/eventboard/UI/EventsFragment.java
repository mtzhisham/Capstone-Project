package com.moataz.eventboard.UI;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.moataz.MultiDexApplication.eventboard.R;
import com.moataz.eventboard.DataUtil.EventsAdapter;
import com.moataz.eventboard.ParserUtil.EventResponse;
import com.moataz.eventboard.Syncing.EventsIntentService;


import java.util.List;

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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    int PLACE_PICKER_REQUEST = 1;
    String SYNCING_STATUS = "SYNCING_STATUS";
    String RUNNING = "RUNNING";
    String STOPPING = "STOPPING";
    String ACTION = "SYNC";

    public static final String ACTION_RESP =
            "com.mamlambo.intent.action.MESSAGE_PROCESSED";
    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.moataz.eventboard.Syncing.EventsProvider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.moataz.eventboard";
    // The account name
    public static final String ACCOUNT = "default_account";
    // Instance fields
    Account mAccount;

    public static final String ADDRESS = "ADDRESS";
    public static final String PAGE = "PAGE";



    public static final String ARG_PAGE = "ARG_PAGE";
    public View view;
    public RecyclerView rvContacts;
    private int mPage;
    List list;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mPage = getArguments().getInt(ARG_PAGE);
        }
        context = getContext();
        activity = getActivity();

        setHasOptionsMenu(true);



        IntentFilter filter = new IntentFilter(ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new EventsResponseReciver(new Handler());
        getActivity().registerReceiver(receiver, filter);


    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_events, container, false);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
//        tvTitle.setText("Fragment #" + mPage);

        rvContacts = (RecyclerView) view.findViewById(R.id.rvContacts);
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


//            getEvents("Alexandria, Egypt");

//        onRefreshButtonClick(view);

            startService("Alexandria, Egypt","1");

        return view;
    }


    public void startService(String adress, String page){
        Intent msgIntent = new Intent(getContext(), EventsIntentService.class);
        msgIntent.putExtra(ADDRESS, adress);
        msgIntent.putExtra(PAGE, page);

        getActivity().startService(msgIntent);
    }

    public void updateView(){
//        Event event = new Event();
        EventResponse eventResponse = new EventResponse();
        EventsAdapter adapter = new EventsAdapter(getContext(), eventResponse.events);
                // Attach the adapter to the recyclerview to populate items
                rvContacts.setAdapter(adapter);
                // Set layout manager to position the items
                rvContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
                // That's all!
              adapter.setOnItemClickListener(new EventsAdapter.ClickListener() {
                  @Override
                  public void onItemClick(int position, View v) {
                      Toast.makeText(getContext()," "+ position, Toast.LENGTH_LONG).show();
                      Log.d("adapter",position+"");



                  }

                  @Override
                  public void onItemLongClick(int position, View v) {

                  }
              });
    }


    public void onRefreshButtonClick(View v) {

        // Pass the settings flags by inserting them in a bundle
        Bundle settingsBundle = new Bundle();
        settingsBundle.putString("address","Alexandria, Egypt");
        settingsBundle.putString("page","1");
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        ContentResolver.requestSync(mAccount, AUTHORITY, settingsBundle);


    }

    public class SyncReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                //do something


                Log.d("respons", extras.getParcelableArrayList("response").toString());
            }
        }
    }








//    public void getEvents(String address){
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://www.eventbriteapi.com/v3/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//
//        MyApiEndpointInterface apiService =
//                retrofit.create(MyApiEndpointInterface.class);
//
//
//        Call<EventResponse> call = apiService.getEvents("Bearer XWN6MIU4KRLP2ZTUGPBU","1",address);
//        Log.d("EventBoard","" + call.request().url().toString());
//        call.enqueue(new Callback<EventResponse>() {
//            @Override
//            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
//                int statusCode = response.code();
//                Log.d("EventBoard","" + statusCode);
//
//                Log.d("EventBoard", " size: "+ response.body().events.size());
//
//                Log.d("EventBoard", response.body().events.get(1).getName().getText());
//
//                // Initialize contacts
////                contacts = Contact.createContactsList(20);
//                // Create adapter passing in the sample user data
//                list = response.body().events;
//                EventsAdapter adapter = new EventsAdapter(getContext(), response.body().events);
//                // Attach the adapter to the recyclerview to populate items
//                rvContacts.setAdapter(adapter);
//                // Set layout manager to position the items
//                rvContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
//                // That's all!
//              adapter.setOnItemClickListener(new EventsAdapter.ClickListener() {
//                  @Override
//                  public void onItemClick(int position, View v) {
//                      Toast.makeText(getContext()," "+ position, Toast.LENGTH_LONG).show();
//                      Log.d("adapter",position+"");
//
//
//
//                  }
//
//                  @Override
//                  public void onItemLongClick(int position, View v) {
//
//                  }
//              });
//
//            }
//
//
//            @Override
//            public void onFailure(Call<EventResponse> call, Throwable t) {
//                Log.d("EventBoard",t.getMessage());
//            }
//        });
//
//    }

//    public void fire(String URL){
//
//        Intent intet = new Intent(getActivity(),FavFragment.class);
//        intet.putExtra("url",URL);
//        getActivity().startActivity(intet);
//
//    }



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


    public interface MyApiEndpointInterface {
        // Request method and URL specified in the annotation
        // Callback for the parsed response is the last parameter

        @GET("events/search/")
        Call<EventResponse> getEvents(@Header("Authorization") String apiKey, @Query("page") String page,
                                      @Query("location.address") String adress);
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
            case R.id.about:
                pickLocation();
                return true;
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
                String toastMsg = String.format("Place: %s %s", lg.latitude ,lg.longitude);

//                getEvents(address);
                onRefreshButtonClick(view);

                Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();



                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putFloat("lat", (float) lg.latitude);
                editor.putFloat("long", (float) lg.longitude);
                editor.apply();



            }
        }
}

    public class EventsResponseReciver extends BroadcastReceiver {


        public static final String ACTION_RESP =
                "com.mamlambo.intent.action.MESSAGE_PROCESSED";

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

                    final EventResponse text = intentt.getParcelableExtra("response");
                    Log.d("response",text.events.get(1).getId() + "");


                    EventsAdapter adapter = new EventsAdapter(context, text.events);
                    rvContacts = (RecyclerView)  view.findViewById(R.id.rvContacts);
                    // Attach the adapter to the recyclerview to populate items
                    rvContacts.setAdapter(adapter);
                    // Set layout manager to position the items
                   rvContacts.setLayoutManager(new LinearLayoutManager(context));
                    // That's all!
                    adapter.setOnItemClickListener(new EventsAdapter.ClickListener() {
                        @Override
                        public void onItemClick(int position, View v) {
                            Toast.makeText(context," "+ position, Toast.LENGTH_LONG).show();
                            Log.d("adapter",position+"");


                            Intent intent1 = new Intent(getActivity(),Detail.class);
                            intent1.putExtra("url",text.events.get(position).getLogo().getOriginal().getUrl());
                            intent1.putExtra("name",text.events.get(position).getName().getText());
                            startActivity(intent1);


                        }

                        @Override
                        public void onItemLongClick(int position, View v) {

                        }
                    });

                }
            });








        }
    }

}