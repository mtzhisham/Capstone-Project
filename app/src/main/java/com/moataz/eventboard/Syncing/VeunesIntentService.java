package com.moataz.eventboard.Syncing;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.moataz.MultiDexApplication.eventboard.BuildConfig;
import com.moataz.eventboard.ParserUtil.VenuesResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by moataz on 22/01/17.
 */
public class VeunesIntentService extends IntentService {

    public static final String ACTION_RESP =
            "com.moataz.intent.action.VENUE_PROCESSED";

    public static final String VID = "VID";

    Intent broadcastIntent;
    private Context mContext;

    public VeunesIntentService() {super("VeunesIntentService");}


    @Override
    protected void onHandleIntent(Intent intent) {


        if (mContext == null){
            mContext = this;
        }


        String vid = intent.getStringExtra(VID);


        broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION_RESP);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.eventbriteapi.com/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        MyApiEndpointInterface apiService =
                retrofit.create(MyApiEndpointInterface.class);

        Call<VenuesResponse> call = apiService.getEvents("Bearer " + BuildConfig.EVENTBRITE_APIKEY, vid );
        call.enqueue(new Callback<VenuesResponse>() {
            @Override
            public void onResponse(Call<VenuesResponse> call, Response<VenuesResponse> response) {
                int statusCode = response.code();

                broadcastIntent.putExtra("response",response.body());
                sendBroadcast(broadcastIntent);

            }

            @Override
            public void onFailure(Call<VenuesResponse> call, Throwable t) {
                Log.d("EventBoard",t.getMessage());
            }
        });



    }


    public interface MyApiEndpointInterface {
        // Request method and URL specified in the annotation
        // Callback for the parsed response is the last parameter

        @GET("venues/{id}")
        Call<VenuesResponse> getEvents(@Header("Authorization") String apiKey, @Path("id") String id);
    }
}
