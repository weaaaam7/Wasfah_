package com.example.wasfah;

public class Comment {

    private String content,uid,uimg,uname;
    private String timestamp;
    private String date;
    private boolean isCommentedByUser;
    public Comment() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Comment(String content, String uid, String uname,String date, boolean isCommentedByUser,String timestamp) {
        this.content = content;
        this.uid = uid;
        this.uname = uname;
        this.date = date;
        this.timestamp=timestamp;
        this.isCommentedByUser = isCommentedByUser;
    }


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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setCommentedByUser(boolean commentedByUser) { isCommentedByUser = commentedByUser; }

    public boolean isCommentedByUser() { return isCommentedByUser; }
}
