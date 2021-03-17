package com.example.wasfah.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class IngredientModel implements Serializable , Parcelable {

    private String modelId;
    private String name;
    private double quantity;
    private String unitOfMeasure;

    public IngredientModel(String name, double quantity, String unitOfMeasure) {
        this.name = name;
        this.quantity = quantity;
        this.unitOfMeasure = unitOfMeasure;
    }

    public IngredientModel()
    {
        this.modelId = UUID.randomUUID().toString();
    }

    protected IngredientModel(Parcel in) {
        modelId = in.readString();
        name = in.readString();
        quantity = in.readDouble();
        unitOfMeasure = in.readString();
    }

    public static final Creator<IngredientModel> CREATOR = new Creator<IngredientModel>() {
        @Override
        public IngredientModel createFromParcel(Parcel in) {
            return new IngredientModel(in);
        }

        @Override
        public IngredientModel[] newArray(int size) {
            return new IngredientModel[size];
        }
    };

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IngredientModel)) return false;
        IngredientModel that = (IngredientModel) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(modelId);
        parcel.writeString(name);
        parcel.writeDouble(quantity);
        parcel.writeString(unitOfMeasure);
    }
}
