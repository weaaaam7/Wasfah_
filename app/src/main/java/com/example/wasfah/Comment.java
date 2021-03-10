package com.example.wasfah;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.ServerValue;

import java.io.Serializable;

public class Comment implements Serializable, Parcelable {

    private String content,uid,uimg,uname;
    private Object timestamp;

    public Comment() {
    }

    public Comment(String content, String uid, String uname) {
        this.content = content;
        this.uid = uid;
        this.uname = uname;
        this.timestamp = ServerValue.TIMESTAMP;
    }

    public Comment(String content, String uid, String uname, Object timestamp) {
        this.content = content;
        this.uid = uid;
        this.uname = uname;
        this.timestamp = timestamp;
    }

    protected Comment(Parcel in) {
        content = in.readString();
        uid = in.readString();
        uimg = in.readString();
        uname = in.readString();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUimg() {
        return uimg;
    }

    public void setUimg(String uimg) {
        this.uimg = uimg;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(content);
        parcel.writeString(uid);
        parcel.writeString(uimg);
        parcel.writeString(uname);
    }
}
