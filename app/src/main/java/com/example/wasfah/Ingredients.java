package com.example.wasfah;

import android.os.Parcelable;

import java.io.Serializable;

public class Ingredients implements Serializable {

    private String name;
    private Long quan;
    private String unit;

    public Ingredients(String name, Long quan, String unit) {
        this.name = name;
        this.quan = quan;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getQuan() {
        return quan;
    }

    public void setQuan(Long quan) {
        this.quan = quan;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFullName(){
        return name+"         "+quan+" "+unit;
    }
}
