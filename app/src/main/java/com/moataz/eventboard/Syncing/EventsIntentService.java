package com.moataz.eventboard.Syncing;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.moataz.MultiDexApplication.eventboard.BuildConfig;
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
 * Created by moataz on 20/01/17.
 */
public class EventsIntentService extends IntentService {

    public static final String ADDRESS = "ADDRESS";
    public static final String PAGE = "PAGE";
    Intent broadcastIntent;
    private Context mContext;



    public static final String ACTION_RESP =
            "com.moataz.intent.action.EVENTS_PROCESSED";



    public EventsIntentService(){
        super("EventsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (mContext == null){
            mContext = this;
        }

        String address = intent.getStringExtra(ADDRESS);
        String page = intent.getStringExtra(PAGE);

        broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION_RESP);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.eventbriteapi.com/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        MyApiEndpointInterface apiService =
                retrofit.create(MyApiEndpointInterface.class);

        Call<EventResponse> call = apiService.getEvents("Bearer " + BuildConfig.EVENTBRITE_APIKEY,page, address);
        Log.d("EventBoard","" + call.request().url().toString());
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                int statusCode = response.code();
                Log.d("EventBoard","" + statusCode);

                if (response.body().events.size() !=0){



                    Log.d("EventBoard", " size: "+ response.body().events.size());

//                Log.d("EventBoard", response.body().events.get(1).getName().getText());

                    Log.d("EventBoard", "Hello from intentservice");


                    broadcastIntent.putExtra("response",response.body());
                    sendBroadcast(broadcastIntent);

                } else {

                    broadcastIntent.putExtra("response","FAIL");
                    sendBroadcast(broadcastIntent);



                }

            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                Log.d("EventBoard",t.getMessage());
            }
        });




    }




    public interface MyApiEndpointInterface {
        // Request method and URL specified in the annotation
        // Callback for the parsed response is the last parameter

        @GET("events/search/")
        Call<EventResponse> getEvents(@Header("Authorization") String apiKey, @Query("page") String page,
                                      @Query("location.address") String adress);
    }
}
