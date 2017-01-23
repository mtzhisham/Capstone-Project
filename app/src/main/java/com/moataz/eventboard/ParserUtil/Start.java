package com.moataz.eventboard.ParserUtil;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by moataz on 1/6/2017.
 */
public class Start implements Parcelable {


    public static final Creator<Start> CREATOR = new Creator<Start>() {
        @Override
        public Start createFromParcel(Parcel in) {
            return new Start(in);
        }

        @Override
        public Start[] newArray(int size) {
            return new Start[size];
        }
    };
    @SerializedName("timezone")
    @Expose
    private String timezone;
    @SerializedName("local")
    @Expose
    private String local;
    @SerializedName("utc")
    @Expose
    private String utc;

    protected Start(Parcel in) {
        timezone = in.readString();
        local = in.readString();
        utc = in.readString();
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getUtc() {
        return utc;
    }

    public void setUtc(String utc) {
        this.utc = utc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(timezone);
        parcel.writeString(local);
        parcel.writeString(utc);
    }
}
