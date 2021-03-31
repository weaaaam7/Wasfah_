package com.example.wasfah;

import java.io.Serializable;

public class Steps implements Serializable {

    private String description;


    public Steps(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
