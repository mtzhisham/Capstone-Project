package com.moataz.eventboard.Syncing;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;

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

    public static final String ACTION_RESP =
            "com.mamlambo.intent.action.MESSAGE_PROCESSED";

    public EventsIntentService(){
        super("EventsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

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


        Call<EventResponse> call = apiService.getEvents("Bearer XWN6MIU4KRLP2ZTUGPBU",page, address);
        Log.d("EventBoard","" + call.request().url().toString());
        call.enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                int statusCode = response.code();
                Log.d("EventBoard","" + statusCode);

                Log.d("EventBoard", " size: "+ response.body().events.size());

                Log.d("EventBoard", response.body().events.get(1).getName().getText());

                Log.d("EventBoard", "Hello from intentservice");


                broadcastIntent.putExtra("response",response.body());
                sendBroadcast(broadcastIntent);


//                Log.d("EventBoard", syncResult.toString());

//                Intent intent = new Intent();
//                intent.setAction("response");
//                intent.putParcelableArrayListExtra("response", (ArrayList<? extends Parcelable>) response.body().events);
//                context.sendBroadcast(intent);

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
//                adapter.setOnItemClickListener(new EventsAdapter.ClickListener() {
//                    @Override
//                    public void onItemClick(int position, View v) {
//                        Toast.makeText(getContext()," "+ position, Toast.LENGTH_LONG).show();
//                        Log.d("adapter",position+"");
//
//
//
//                    }
//
//                    @Override
//                    public void onItemLongClick(int position, View v) {
//
//                    }
//                });
//
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
