package com.moataz.eventboard.UI;

import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.astuetz.PagerSlidingTabStrip;
import com.moataz.eventboard.ParserUtil.EventResponse;
import com.moataz.eventboard.R;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

public class MainActivity extends AppCompatActivity implements EventsFragment.OnFragmentInteractionListener,
FavFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new mFragmentPagerAdapter(getSupportFragmentManager()));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.eventbriteapi.com/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        MyApiEndpointInterface apiService =
                retrofit.create(MyApiEndpointInterface.class);


        Call<EventResponse> call = apiService.getEvents("Bearer ##");
        Log.d("EventBoard","" + call.request().url().toString());
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                int statusCode = response.code();
                Log.d("EventBoard","" + statusCode);

                Log.d("EventBoard", " size: "+ response.body().events.size());

                Log.d("EventBoard", response.body().events.get(1).getName().getText());

            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Log.d("EventBoard",t.getMessage());
            }
        });

            }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    public interface MyApiEndpointInterface {
        // Request method and URL specified in the annotation
        // Callback for the parsed response is the last parameter

        @GET("events/search/")
        Call<EventResponse> getEvents(@Header("Authorization") String apiKey);
    }





}
