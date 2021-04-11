package model;

public class MyMessages {
    private String from,pid,status,time;
    public MyMessages()
    {

    }

    public MyMessages(String from, String pid, String status, String time) {
        this.from = from;
        this.pid = pid;
        this.status = status;
        this.time = time;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
