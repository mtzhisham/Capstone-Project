package com.moataz.eventboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.moataz.eventboard.ParserUtil.Event;
import com.moataz.eventboard.ParserUtil.EventResponse;


import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.eventbriteapi.com/v3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        MyApiEndpointInterface apiService =
                retrofit.create(MyApiEndpointInterface.class);


        Call<EventResponse> call = apiService.getEvents("Bearer OAK");
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
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                int statusCode = response.code();
//
//                Log.d("EventBoard","" + statusCode);
//                String events = null;
//                try {
//
//                    events = response.body().string();
//
//                    Log.d("EventBoard", events);
//                    Event event = new Event();
//                    Log.d("EventBoard", event.getName());
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
            }



    public interface MyApiEndpointInterface {
        // Request method and URL specified in the annotation
        // Callback for the parsed response is the last parameter

        @GET("events/search/")
        Call<EventResponse> getEvents(@Header("Authorization") String apiKey);
    }
}
