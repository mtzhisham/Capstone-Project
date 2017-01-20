package com.moataz.eventboard.ParserUtil;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by moataz on 1/6/2017.
 */
public class Original implements Parcelable {


    @SerializedName("url")
    @Expose
    private String url;


    protected Original(Parcel in) {
        url = in.readString();
    }

    public static final Creator<Original> CREATOR = new Creator<Original>() {
        @Override
        public Original createFromParcel(Parcel in) {
            return new Original(in);
        }

        @Override
        public Original[] newArray(int size) {
            return new Original[size];
        }
    };

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
    }
}
