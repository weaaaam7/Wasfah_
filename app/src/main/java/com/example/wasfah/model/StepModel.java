package com.example.wasfah.model;

import java.util.UUID;

public class StepModel {

    private String modelId;
    private int order;
    private String description;

    public StepModel()
    {
        modelId = UUID.randomUUID().toString();
    }
    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
