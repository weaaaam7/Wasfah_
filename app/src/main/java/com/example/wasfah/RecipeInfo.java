package com.example.wasfah;

import com.example.wasfah.model.IngredientModel;

import java.util.List;

public class RecipeInfo {

    private String title;
    private String category;
    private String img;
    private String email;
    private List<IngredientModel> ingredients;
    private List<Steps> steps;
    private String recipeId;
    private String timestamp;
    private long likes;
    private long dislikes;
    private String name;
    private boolean isPublishedByUser;
    private boolean isProfile;

//    public RecipeInfo(String title, String category, String img, String email, List<IngredientModel> ingredients, List<Steps> steps, String recipeId, String timestamp, long likes, long dislikes, String name, boolean isPublishedByUser, boolean isProfile) {
//        this.title = title;
//        this.category = category;
//        this.img = img;
//        this.email = email;
//        this.ingredients = ingredients;
//        this.steps = steps;
//        this.recipeId = recipeId;
//        this.timestamp = timestamp;
//        this.likes = likes;
//        this.dislikes = dislikes;
//        this.name = name;
//        this.isPublishedByUser = isPublishedByUser;
//        this.isProfile = isProfile;
//    }

    public boolean isProfile() {
        return isProfile;
    }

    public void setProfile(boolean profile) {
        isProfile = profile;
    }

    public void setPublishedByUser(boolean publishedByUser) {
        isPublishedByUser = publishedByUser;
    }

    public boolean isPublishedByUser() {
        return isPublishedByUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RecipeInfo(String title, String category, String img, List<IngredientModel>  ingredients,List<Steps> steps,String recipeId,String timestamp) {
     this.title = title;
        this.img = img;
        this.category = category;
        this.ingredients = ingredients;
        this.steps = steps;
        this.recipeId = recipeId;
        this.timestamp = timestamp;
        this.isPublishedByUser = true;
        this.isProfile = true;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public RecipeInfo(String title, String category, String img, List<IngredientModel>  ingredients,List<Steps> steps,String recipeId,String timestamp,String name,boolean isPublishedByUser,boolean isProfile, long likes, long dislikes) {
    this.title = title;
        this.img = img;
        this.category = category;
        this.ingredients = ingredients;
        this.steps = steps;
        this.recipeId = recipeId;
        this.timestamp = timestamp;
        this.name = name;
        this.isPublishedByUser = isPublishedByUser;
        this.isProfile = isProfile;
        this.likes = likes;
        this.dislikes = dislikes;


    }


    public RecipeInfo getRecipeInfo() {
        return this;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }

    public List<IngredientModel> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientModel> ingredients) {
        this.ingredients = ingredients;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public long getDislikes() {
        return dislikes;
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }
}
