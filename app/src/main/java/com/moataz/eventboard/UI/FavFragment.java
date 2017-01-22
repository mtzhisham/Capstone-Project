package com.moataz.eventboard.UI;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;


import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.moataz.MultiDexApplication.eventboard.R;
import com.moataz.eventboard.DataUtil.CustomCContactAdapter;
import com.moataz.eventboard.DataUtil.EventsAdapter;
import com.moataz.eventboard.ParserUtil.Event;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FavFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FavFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavFragment extends Fragment  implements
        LoaderManager.LoaderCallbacks<Cursor> {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    final Uri CONTENT_URL =
            Uri.parse("content://com.moataz.eventboard.DataUtil.EventsProvider/cpevents");
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    ContentResolver resolver;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;

    // If non-null, this is the current filter the user has provided.
    String mCurFilter;
    List<Event> events;
    CustomCContactAdapter mAdapter;
    public RecyclerView rvEvents;
    private OnFragmentInteractionListener mListener;

    public FavFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FavFragment newInstance(int page) {
        FavFragment fragment = new FavFragment();
        Bundle args = new Bundle();

        args.putInt(ARG_PAGE, page);

        fragment.setArguments(args);
        return fragment;
    }
    public static Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mPage = getArguments().getInt(ARG_PAGE);

        }
        context = getContext();

        resolver = getActivity().getContentResolver();


        getActivity().getSupportLoaderManager().initLoader(1, null, this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_fav, container, false);



        events = new ArrayList<Event>();

        rvEvents = (RecyclerView)  view.findViewById(R.id.rvFavEvents);
        rvEvents.setLayoutManager(new LinearLayoutManager(context));
        // Attach the adapter to the recyclerview to populate items
        rvEvents.setAdapter(mAdapter);






//        new loadEvents().execute();



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

    String[] projection = new String[]{"id", "eDBID","event"};

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getActivity(), CONTENT_URL, projection,
                null, null, null);
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {

        data.moveToFirst();
        mAdapter = new CustomCContactAdapter(context,data);
        mAdapter.setOnItemClickListener(new CustomCContactAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Toast.makeText(context," "+ position, Toast.LENGTH_LONG).show();
                Log.d("adapter",position+"");




                Intent intent1 = new Intent(getActivity(),Detail.class);
//                            intent1.putExtra("url",eResponse.events.get(position).getLogo().getOriginal().getUrl());
//                            intent1.putExtra("name",eResponse.events.get(position).getName().getText());
                data.moveToPosition(position);
                Gson gson = new Gson();
                Event event = gson.fromJson(data.getString(data.getColumnIndex("event")), Event.class );

                intent1.putExtra("Event",event);


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
        rvEvents.setAdapter(mAdapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



}
