package com.example.wasfah.model;

public class MyMessages {
    private String from,pid,status;
    public MyMessages()
    {

    }

    public MyMessages(String from, String pid, String status) {
        this.from = from;
        this.pid = pid;
        this.status = status;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
