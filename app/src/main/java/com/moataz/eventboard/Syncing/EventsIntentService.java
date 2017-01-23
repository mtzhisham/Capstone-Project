package com.moataz.eventboard.Syncing;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.moataz.MultiDexApplication.eventboard.BuildConfig;
import com.moataz.MultiDexApplication.eventboard.R;
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
    public static final String LAT = "LAT";
    public static final String LOG = "LOG";
    public static final String ACTION_RESP =
            "com.moataz.intent.action.EVENTS_PROCESSED";
    Intent broadcastIntent;
    private Context mContext;


    public EventsIntentService() {
        super("EventsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (mContext == null) {
            mContext = this;
        }

        String address = intent.getStringExtra(ADDRESS);
        String page = intent.getStringExtra(PAGE);
        String lat = intent.getStringExtra(LAT);
        String log = intent.getStringExtra(LOG);

        broadcastIntent = new Intent();
        broadcastIntent.setAction(ACTION_RESP);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.eventbriteapi.com/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        MyApiEndpointInterface apiService =
                retrofit.create(MyApiEndpointInterface.class);

        Call<EventResponse> call = apiService.getEvents("Bearer " + BuildConfig.EVENTBRITE_APIKEY, page, address, lat, log);

        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
//                int statusCode = response.code();


                try {

                    if (response.body().events.size() != 0) {

                        broadcastIntent.putExtra("response", response.body());
                        sendBroadcast(broadcastIntent);

                    } else {

                        broadcastIntent.putExtra("response", "FAIL");
                        sendBroadcast(broadcastIntent);


                    }

                } catch (Exception e) {
                    Toast.makeText(EventsIntentService.this, getResources().getString(R.string.conn_toast), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {

            }
        });


    }


    public interface MyApiEndpointInterface {
        // Request method and URL specified in the annotation
        // Callback for the parsed response is the last parameter

        @GET("events/search/")
        Call<EventResponse> getEvents(@Header("Authorization") String apiKey, @Query("page") String page,
                                      @Query("location.address") String address, @Query("location.latitude") String lat, @Query("location.longitude") String log);
    }
}
