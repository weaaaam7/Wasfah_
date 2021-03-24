package com.example.wasfah.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
//json to pojo model
public class NotificationBody {

@SerializedName("myKey")
@Expose
private String myKey;
@SerializedName("name")
@Expose
private String name;
@SerializedName("type")
@Expose
private String type;
@SerializedName("title")
@Expose
private String title;
@SerializedName("body")
@Expose
private String body;
@SerializedName("image")
@Expose
private String image;

public String getMyKey() {
return myKey;
}

public void setMyKey(String myKey) {
this.myKey = myKey;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getType() {
return type;
}

public void setType(String type) {
this.type = type;
}

public String getTitle() {
return title;
}

public void setTitle(String title) {
this.title = title;
}

public String getBody() {
return body;
}

public void setBody(String body) {
this.body = body;
}

public String getImage() {
return image;
}

public void setImage(String image) {
this.image = image;
}

    public NotificationBody(String myKey, String name, String type, String title, String body, String image) {
        this.myKey = myKey;
        this.name = name;
        this.type = type;
        this.title = title;
        this.body = body;
        this.image = image;
    }
}
