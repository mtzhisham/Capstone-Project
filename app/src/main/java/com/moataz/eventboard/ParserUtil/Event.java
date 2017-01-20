package com.moataz.eventboard.ParserUtil;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.plus.model.people.Person;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by moataz on 1/6/2017.
 */

public class Event implements Parcelable {


    @SerializedName("name")
    @Expose
    private Name name;
    @SerializedName("description")
    @Expose
    private Description description;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("start")
    @Expose
    private Start start;
    @SerializedName("end")
    @Expose
    private End end;

    @SerializedName("logo_id")
    @Expose
    private String logoId;

    @SerializedName("venue_id")
    @Expose
    private String venueId;

    @SerializedName("resource_uri")
    @Expose
    private String resourceUri;
    @SerializedName("logo")
    @Expose
    private Logo logo;

    protected Event(Parcel in) {
        name = in.readParcelable(Name.class.getClassLoader());
        logo = in.readParcelable(Logo.class.getClassLoader());
        description = in.readParcelable(Description.class.getClassLoader());
        start = in.readParcelable(Start.class.getClassLoader());
        end = in.readParcelable(End.class.getClassLoader());
        id = in.readString();
        url = in.readString();
        logoId = in.readString();
        venueId = in.readString();
        resourceUri = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Start getStart() {
        return start;
    }

    public void setStart(Start start) {
        this.start = start;
    }

    public End getEnd() {
        return end;
    }

    public void setEnd(End end) {
        this.end = end;
    }


    public String getLogoId() {
        return logoId;
    }

    public void setLogoId(String logoId) {
        this.logoId = logoId;
    }


    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }


    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(name,i);
        parcel.writeParcelable(logo,i);
        parcel.writeParcelable(description,i);
        parcel.writeParcelable(start,i);
        parcel.writeParcelable(end,i);
        parcel.writeString(id);
        parcel.writeString(url);
        parcel.writeString(logoId);
        parcel.writeString(venueId);
        parcel.writeString(resourceUri);
    }
}
