package com.example.wasfah.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class RecipeModel {

    private String recipeId;
    private String title;
    private String category;
    private List<IngredientModel> ingredients;
    private List<StepModel> preparationSteps;
    private String createdBy;
    private String picUri;
    private String timestamp;
    private String currentUserId;



    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = userID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp() {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        this.timestamp =  date;
    }
    public RecipeModel()
    {
        this.recipeId = UUID.randomUUID().toString();
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<StepModel> getPreparationSteps() {
        return preparationSteps;
    }

    public void setPreparationSteps(List<StepModel> preparationSteps) {
        this.preparationSteps = preparationSteps;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<IngredientModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientModel> ingredients) {
        this.ingredients = ingredients;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getPicUri() {
        return picUri;
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }
}

