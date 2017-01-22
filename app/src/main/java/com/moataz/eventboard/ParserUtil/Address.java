package com.moataz.eventboard.ParserUtil;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by moataz on 22/01/17.
 */
public class Address implements Parcelable {


    @SerializedName("localized_multi_line_address_display")
    @Expose
    private List<String> localizedMultiLineAddressDisplay = null;

    protected Address(Parcel in) {
        localizedMultiLineAddressDisplay = in.createStringArrayList();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public List<String> getLocalizedMultiLineAddressDisplay() {
        return localizedMultiLineAddressDisplay;
    }

    public void setLocalizedMultiLineAddressDisplay(List<String> localizedMultiLineAddressDisplay) {
        this.localizedMultiLineAddressDisplay = localizedMultiLineAddressDisplay;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(localizedMultiLineAddressDisplay);
    }
}
