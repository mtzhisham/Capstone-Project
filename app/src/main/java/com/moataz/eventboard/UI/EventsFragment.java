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
    int PLACE_PICKER_REQUEST = 1;


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

    private static IntentFilter filter;



    public static final String ARG_PAGE = "ARG_PAGE";
    public View view;
    public RecyclerView rvContacts;
    private int mPage;
    List list;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_events, container, false);
//        TextView tvTitle = (TextView) view.findViewById(R.id.tv);
//        tvTitle.setText("Fragment #" + mPage);

        rvContacts = (RecyclerView) view.findViewById(R.id.rvEvents);
        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

            startService("Alexandria, Egypt","1");

        return view;
    }


    public void startService(String adress, String page){
        Intent msgIntent = new Intent(getContext(), EventsIntentService.class);
        msgIntent.putExtra(ADDRESS, adress);
        msgIntent.putExtra(PAGE, page);

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
                    rvContacts = (RecyclerView)  view.findViewById(R.id.rvEvents);
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

                            Log.d("response",text.events.get(position).getLogo().getOriginal().getUrl());
                            Log.d("response",text.events.get(position).getName().getText());

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