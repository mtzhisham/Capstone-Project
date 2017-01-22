package com.moataz.eventboard.UI;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.moataz.MultiDexApplication.eventboard.R;
import com.moataz.eventboard.DataUtil.EventsAdapter;
import com.moataz.eventboard.ParserUtil.Event;
import com.moataz.eventboard.ParserUtil.VenuesResponse;
import com.moataz.eventboard.Syncing.EventsIntentService;
import com.moataz.eventboard.Syncing.VeunesIntentService;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {
    Cursor mCursor;
    // The URL used to target the content provider


    public static final String ACTION_RESP =
            "com.moataz.intent.action.VENUE_PROCESSED";

    final Uri CONTENT_URL =
            Uri.parse("content://com.moataz.eventboard.DataUtil.EventsProvider/cpevents");

   VenuesResponse vResponse;
    TextView venue_tv;
    public static final String VID = "VID";
    ContentValues values;
    private static IntentFilter filter;
    ContentResolver resolver;
    String name;
    View view;
    String eventID;
     FloatingActionButton programFab1;

    public DetailFragment() {
    }

    private VenueResponseReciver receiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resolver = getActivity().getContentResolver();

        filter = new IntentFilter(ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);

        receiver = new VenueResponseReciver(new Handler());
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.photo);
        TextView name_tv = (TextView) view.findViewById(R.id.event_name);
        TextView date_tv = (TextView) view.findViewById(R.id.event_date);
        TextView desc_tv = (TextView) view.findViewById(R.id.event_disc);
        venue_tv = (TextView) view.findViewById(R.id.venue_tv);




        Intent intent = getActivity().getIntent();
        final Event event =  intent.getParcelableExtra("Event");

        String url = event.getLogo().getUrl();
        startService(event.getVenueId());
        name_tv.setText(event.getName().getText());
        date_tv.setText(event.getStart().getLocal() + " to " +event.getEnd().getLocal());
        desc_tv.setText(event.getDescription().getText());
        eventID = event.getId();
        Log.d("EventDetail_URL",event.getUrl());
        Log.d("EventDetail_VenueID",event.getVenueId());
        Log.d("EventDetail_URI",event.getResourceUri());

        Glide.with(getContext())
                .load(url)
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);


//        Button fav = (Button) view.findViewById(R.id.fav);
//        fav.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//

//
//            }
//        });


//        Button button = (Button) view.findViewById(R.id.button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getMoviesFromDB();
//            }
//        });





        final FloatingActionMenu menu = (FloatingActionMenu) view.findViewById(R.id.menu);

        programFab1 = new FloatingActionButton(getActivity());
        programFab1.setButtonSize(FloatingActionButton.SIZE_MINI);
        programFab1.setImageResource(R.drawable.ic_star_grey600_24dp);
        menu.addMenuButton(programFab1);
        new lookOnlyEvent().execute(eventID);

        programFab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                values = new ContentValues();
                Gson gson = new Gson();

                values.put("eDBID", event.getId());
                values.put("event", gson.toJson(event));

//                new AddEvent().execute(values);
                new lookNCREvent().execute(eventID);




            }
        });


        final FloatingActionButton programFab2 = new FloatingActionButton(getActivity());
        programFab2.setButtonSize(FloatingActionButton.SIZE_MINI);
        programFab2.setImageResource(R.drawable.ic_share);
        menu.addMenuButton(programFab2);

        programFab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//               Boolean state = new lookupEvent().execute(eventID);



            }
        });







        return view;
    }

    public void startService(String vid){
        Intent msgIntent = new Intent(getContext(), VeunesIntentService.class);
        msgIntent.putExtra(VID, vid);
        getActivity().startService(msgIntent);
    }





    public class VenueResponseReciver extends BroadcastReceiver {



        Intent intentt;
        private final Handler handler;

        public VenueResponseReciver(Handler handler) {
            this.handler = handler;
        }



        @Override
        public void onReceive(final Context context, final Intent intent) {
            this.intentt = intent;

            handler.post(new Runnable() {
                @Override
                public void run() {

                    vResponse = intentt.getParcelableExtra("response");

                    Log.d("fromFragment",vResponse.getName());

                    venue_tv.setText(vResponse.getName());


                }
                    });

                }








        }



    private class AddEvent extends AsyncTask<ContentValues, Void, Boolean> {
         final Uri CONTENT_URL =
                Uri.parse("content://com.moataz.eventboard.DataUtil.EventsProvider/cpevents");

        @Override
        protected Boolean doInBackground(ContentValues... params) {


            Log.d("stuff",params[0].get("eDBID").toString());
            return resolver.insert(CONTENT_URL, params[0]) != null;


        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result){

                Snackbar snackbar = Snackbar
                        .make(view, "No More Events", Snackbar.LENGTH_LONG);

                snackbar.show();
            }

        }
    }
    private class DeleteEvent extends AsyncTask<String, Void, Boolean> {
        final Uri CONTENT_URL =
                Uri.parse("content://com.moataz.eventboard.DataUtil.EventsProvider/cpevents");

        @Override
        protected Boolean doInBackground(String... params) {

            Log.d("stuff",params[0]);
            return resolver.delete(CONTENT_URL, "eDBID = ? ", new String[]{params[0]}) == 1;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result){

                Snackbar snackbar = Snackbar
                        .make(view, "deleted", Snackbar.LENGTH_LONG);

                snackbar.show();
            }

        }
    }


    public boolean lookupEvent(String id){

        String[] projection = new String[]{"id", "eDBID","event"};


        Cursor cursor = resolver.query(CONTENT_URL,
                projection, "eDBID = ? ", new String[]{id}, null);


        assert cursor != null;
        if(cursor.moveToFirst()){
            String moviefromdb = cursor.getString(cursor.getColumnIndex("event"));
            Log.d("lookup", moviefromdb);

            cursor.close();
            return true;
        }else{
            cursor.close();
            return false;
        }

    }



    private class lookNCREvent extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... params) {

                return lookupEvent(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result){

                Snackbar snackbar = Snackbar
                        .make(view, "found it", Snackbar.LENGTH_LONG);

                snackbar.show();
                new DeleteEvent().execute(eventID);

                programFab1.setImageResource(R.drawable.ic_star_grey600_24dp);
            } else {
                new AddEvent().execute(values);
                programFab1.setImageResource(R.drawable.ic_star_white_24dp);
            }


        }
    }


    private class lookOnlyEvent extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... params) {

            return lookupEvent(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result){


                programFab1.setImageResource(R.drawable.ic_star_white_24dp);


            } else {
                programFab1.setImageResource(R.drawable.ic_star_grey600_24dp);
            }


        }
    }



}




