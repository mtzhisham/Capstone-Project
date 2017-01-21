package com.moataz.eventboard.UI;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.moataz.MultiDexApplication.eventboard.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment {
    Cursor mCursor;
    // The URL used to target the content provider
    static final Uri CONTENT_URL =
            Uri.parse("content://com.moataz.eventboard.DataUtil.EventsProvider/cpevents");
    ContentValues values;

    ContentResolver resolver;
    String name;

    public DetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resolver = getActivity().getContentResolver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.photo);


        Intent intent = getActivity().getIntent();
        String url = intent.getStringExtra("url");
         name = intent.getStringExtra("name");

        Log.d("detail",name);

        Glide.with(getContext())
                .load(url)
                .centerCrop()
                .crossFade()
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);


        Button fav = (Button) view.findViewById(R.id.fav);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                values = new ContentValues();
                values.put("eDBID", name);
                values.put("event", name);
                resolver.insert(CONTENT_URL, values);
                Log.d("URI",CONTENT_URL.toString());

            }
        });


        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMoviesFromDB();
            }
        });




        return view;
    }


    public void getMoviesFromDB(){

        // Projection contains the columns we want
        String[] projection = new String[]{"id", "eDBID","event"};

        // Pass the URL, projection and I'll cover the other options below
        Cursor cursor = resolver.query(CONTENT_URL, projection, null, null, null);



        // Cycle through and display every row of data
        if(cursor.moveToFirst()){

            do{

                String movie = cursor.getString(cursor.getColumnIndex("event"));
                String idfromdb = cursor.getString(cursor.getColumnIndex("eDBID"));


                try {

                    Log.d("fromDB", movie);


                } catch (Exception e)
                {e.printStackTrace();}



            }while (cursor.moveToNext());

        }

        cursor.close();



    }






}
