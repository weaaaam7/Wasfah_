package com.example.wasfah.model;

import com.google.gson.annotations.SerializedName;


public class TranslationResponse {

    @SerializedName("data")
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}

