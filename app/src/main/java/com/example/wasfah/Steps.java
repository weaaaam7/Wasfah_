package com.example.wasfah;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Steps implements Serializable, Parcelable {

    private String description;


    public Steps(String description) {
        this.description = description;
    }

    protected Steps(Parcel in) {
        description = in.readString();
    }

    public static final Creator<Steps> CREATOR = new Creator<Steps>() {
        @Override
        public Steps createFromParcel(Parcel in) {
            return new Steps(in);
        }

        @Override
        public Steps[] newArray(int size) {
            return new Steps[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(description);
    }
}
