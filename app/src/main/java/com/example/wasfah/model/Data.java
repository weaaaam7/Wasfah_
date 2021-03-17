package com.example.wasfah.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("translations")
    private List<Translation> translations = null;

    public List<Translation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }
}
