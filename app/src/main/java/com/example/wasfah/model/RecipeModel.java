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



    private String currentUserId;



    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
<<<<<<< HEAD
    }

    protected RecipeModel(Parcel in) {
        recipeId = in.readString();
        title = in.readString();
        category = in.readString();
        createdBy = in.readString();
        likes = in.readLong();
        dislikes = in.readLong();
        picUri = in.readString();
        timestamp = in.readString();
        img = in.readString();
        isPublishedByUser = in.readByte() != 0;
        isProfile = in.readByte() != 0;
        user = in.readParcelable(FirebaseUser.class.getClassLoader());
        userID = in.readString();
=======
>>>>>>> 19a7570bd3a1c3dd635c6fe4feaf81c1ab6cf472
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
<<<<<<< HEAD


}
=======
}

>>>>>>> 19a7570bd3a1c3dd635c6fe4feaf81c1ab6cf472
