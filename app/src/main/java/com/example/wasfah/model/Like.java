package com.example.wasfah.model;

//like model
public class Like {

    private String uid;
    private boolean like;

    public Like(String uid, boolean like) {
        this.uid = uid;
        this.like = like;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
