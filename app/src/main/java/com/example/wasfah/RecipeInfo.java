package com.example.wasfah;

import android.net.Uri;

public class RecipeInfo {

    private String title;
    private String category;
    private String img;
    private String email;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RecipeInfo(String title, String category, String img) {
        this.title = title;
        this.img = img;
        this.category=category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
