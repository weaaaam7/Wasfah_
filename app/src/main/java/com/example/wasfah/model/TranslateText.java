package com.example.wasfah.model;

import com.google.gson.annotations.SerializedName;

public class TranslateText {
    @SerializedName("q")
    private String q;
    @SerializedName("target")
    private String target;
    @SerializedName("model")
    private String model;

    public TranslateText(String q, String target) {
        this.q = q;
        this.target = target;
        this.model = "base";
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
