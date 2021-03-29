package com.example.wasfah;

import android.net.Uri;

import java.util.List;

public class RecipeInfo {

    private String title;
    private String currentUserId;





    private String category;
    private String img;
    private String email;
    private List<Ingredients> ingredients;
    private List<Steps> steps;
    private String recipeId;
    private String timestamp;
    private String name;
    private boolean isPublishedByUser;
    private boolean isProfile;

//    public String getCurrentUserId() {
//        return currentUserId;
//    }
//
//    public void setCurrentUserId(String currentUserId) {
//        this.currentUserId = currentUserId;
//    }
//
//    public boolean isProfile() {
//        return isProfile;
//    }
//
//    public void setProfile(boolean profile) {
//        isProfile = profile;
//    }
//
//    public void setPublishedByUser(boolean publishedByUser) {
//        isPublishedByUser = publishedByUser;
//    }
//
//    public boolean isPublishedByUser() {
//        return isPublishedByUser;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(String timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public RecipeInfo(String title, String category, String img, List<Ingredients>  ingredients,List<Steps> steps,String recipeId,String timestamp,String currentUserId) {
//        this.title = title;
//        this.img = img;
//        this.category=category;
//        this.ingredients=ingredients;
//        this.steps=steps;
//        this.recipeId=recipeId;
//        this.timestamp=timestamp;
//        this.isPublishedByUser=true;
//        this.isProfile=true;
//        this.currentUserId = currentUserId;
//    }
//
//
//    public String getRecipeId() {
//        return recipeId;
//    }
//
//    public void setRecipeId(String recipeId) {
//        this.recipeId = recipeId;
//    }
//
//    public List<Steps> getSteps() {
//        return steps;
//    }
//
//    public void setSteps(List<Steps> steps) {
//        this.steps = steps;
//    }
//
//    public List<Ingredients> getIngredients() {
//        return ingredients;
//    }
//
//    public void setIngredients(List<Ingredients> ingredients) {
//        this.ingredients = ingredients;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getImg() {
//        return img;
//    }
//
//    public void setImg(String img) {
//        this.img = img;
//    }
//
//    public String getCategory() {
//        return category;
//    }
//
//    public void setCategory(String category) {
//        this.category = category;
//    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPublishedByUser() {
        return isPublishedByUser;
    }

    public void setPublishedByUser(boolean publishedByUser) {
        isPublishedByUser = publishedByUser;
    }

    public boolean isProfile() {
        return isProfile;
    }

    public void setProfile(boolean profile) {
        isProfile = profile;
    }

    public RecipeInfo(String title, String currentUserId, String category, String img, String email, List<Ingredients> ingredients, List<Steps> steps, String recipeId, String timestamp, String name, boolean isPublishedByUser, boolean isProfile) {
        this.title = title;
        this.currentUserId = currentUserId;
        this.category = category;
        this.img = img;
        this.email = email;
        this.ingredients = ingredients;
        this.steps = steps;
        this.recipeId = recipeId;
        this.timestamp = timestamp;
        this.name = name;
        this.isPublishedByUser = isPublishedByUser;
        this.isProfile = isProfile;
    }



    public RecipeInfo(String title, String category, String img, List<Ingredients>  ingredients,List<Steps> steps,String recipeId,String timestamp) {
        this.title = title;
        this.img = img;
        this.category=category;
        this.ingredients=ingredients;
        this.steps=steps;
        this.recipeId=recipeId;
        this.timestamp=timestamp;
        this.isPublishedByUser=true;
        this.isProfile=true;
    }
}
