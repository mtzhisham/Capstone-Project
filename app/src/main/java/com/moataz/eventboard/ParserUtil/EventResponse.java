package com.moataz.eventboard.ParserUtil;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moataz on 1/6/2017.
 */
public class EventResponse implements Parcelable {

   public List<Event> events;

    public  EventResponse(){
        events = new ArrayList<Event>();
    }


    protected EventResponse(Parcel in) {
        events = in.createTypedArrayList(Event.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(events);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EventResponse> CREATOR = new Creator<EventResponse>() {
        @Override
        public EventResponse createFromParcel(Parcel in) {
            return new EventResponse(in);
        }

        @Override
        public EventResponse[] newArray(int size) {
            return new EventResponse[size];
        }
    };
}
