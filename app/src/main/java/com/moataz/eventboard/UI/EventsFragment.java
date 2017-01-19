package com.moataz.eventboard.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.moataz.MultiDexApplication.eventboard.R;
import com.moataz.eventboard.DataUtil.EventsAdapter;
import com.moataz.eventboard.ParserUtil.EventResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
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

    public static final String ARG_PAGE = "ARG_PAGE";
    View view;
    private int mPage;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mPage = getArguments().getInt(ARG_PAGE);
        }


        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_events, container, false);
        TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
//        tvTitle.setText("Fragment #" + mPage);


        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);


            getEvents("Alexandria, Egypt");




        return view;
    }



    public void getEvents(String address){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.eventbriteapi.com/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        MyApiEndpointInterface apiService =
                retrofit.create(MyApiEndpointInterface.class);

        final RecyclerView rvContacts = (RecyclerView) view.findViewById(R.id.rvContacts);
        Call<EventResponse> call = apiService.getEvents("Bearer ##","2",address);
        Log.d("EventBoard","" + call.request().url().toString());
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                int statusCode = response.code();
                Log.d("EventBoard","" + statusCode);

                Log.d("EventBoard", " size: "+ response.body().events.size());

                Log.d("EventBoard", response.body().events.get(1).getName().getText());

                // Initialize contacts
//                contacts = Contact.createContactsList(20);
                // Create adapter passing in the sample user data
                EventsAdapter adapter = new EventsAdapter(getContext(), response.body().events);
                // Attach the adapter to the recyclerview to populate items
                rvContacts.setAdapter(adapter);
                // Set layout manager to position the items
                rvContacts.setLayoutManager(new LinearLayoutManager(getActivity()));
                // That's all!

            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Log.d("EventBoard",t.getMessage());
            }
        });

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

                getEvents(address);

                Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();



                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putFloat("lat", (float) lg.latitude);
                editor.putFloat("long", (float) lg.longitude);
                editor.apply();



            }
        }
}
}