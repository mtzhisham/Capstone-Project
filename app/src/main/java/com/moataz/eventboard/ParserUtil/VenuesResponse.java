package com.moataz.eventboard.ParserUtil;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by moataz on 22/01/17.
 */

public class VenuesResponse implements Parcelable {


    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("resource_uri")
    @Expose
    private String resourceUri;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    protected VenuesResponse(Parcel in) {
        address = in.readParcelable(Address.class.getClassLoader());
        resourceUri = in.readString();
        id = in.readString();
        name = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }

    public static final Creator<VenuesResponse> CREATOR = new Creator<VenuesResponse>() {
        @Override
        public VenuesResponse createFromParcel(Parcel in) {
            return new VenuesResponse(in);
        }

        @Override
        public VenuesResponse[] newArray(int size) {
            return new VenuesResponse[size];
        }
    };

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(address,i);
        parcel.writeString(resourceUri);
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(latitude);
        parcel.writeString(longitude);
    }
}
