package com.moataz.eventboard.ParserUtil;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by moataz on 1/6/2017.
 */
public class EventResponse implements Parcelable {

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
    public List<Event> events;
    @SerializedName("pagination")
    @Expose
    private Pagination pagination;

    public EventResponse() {
        events = new ArrayList<Event>();
    }

    protected EventResponse(Parcel in) {
        events = in.createTypedArrayList(Event.CREATOR);
        pagination = in.readParcelable(Pagination.class.getClassLoader());
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(events);
        dest.writeParcelable(pagination, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
