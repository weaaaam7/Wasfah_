package com.example.wasfah.model;

import android.os.Parcel;

import com.example.wasfah.Steps;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class RecipeModel implements Serializable {

    private String recipeId;
    private String title;
    private String category;
    private List<IngredientModel> ingredients;
    private List<StepModel> preparationSteps;
    private String createdBy;
    //like
    private long likes;
    private long dislikes;
    private String picUri;
    private String timestamp;
    private String img;
    private List<Steps> steps;
    private boolean isPublishedByUser;
    private boolean isProfile;



    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userID = user.getUid();

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
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp() {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        this.timestamp = date;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
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

    public RecipeModel() {
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
        return this.picUri ;//= "https://realfood.tesco.com/media/images/RFO-October2020-65809-Tesco-LetsCook-Oct20-65850-SpicedChickenGreenBeans1400x919-38f3e9b0-7241-49a3-83fe-fcc38d2c24be-0-1400x919.jpg";
    }

    public String getPicUri2() {
        return "https://1.bp.blogspot.com/-eDCzOgLuiTg/XkMKlPN0hhI/AAAAAAAABJ8/aUWdGB_87EAagPAQhLvKs2RaICBjkasOwCLcBGAsYHQ/s1600/WhatsApp%2BImage%2B2020-02-11%2Bat%2B8.31.27%2BPM%2B%25281%2529.jpeg";
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;//"https://realfood.tesco.com/media/images/RFO-October2020-65809-Tesco-LetsCook-Oct20-65850-SpicedChickenGreenBeans1400x919-38f3e9b0-7241-49a3-83fe-fcc38d2c24be-0-1400x919.jpg";
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public FirebaseUser getUser() {
        return user;
    }

    public void setUser(FirebaseUser user) {
        this.user = user;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public long getDislikes() {
        return dislikes;
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }

    public RecipeModel(String recipeId, String title, String category, List<IngredientModel> ingredients, List<StepModel> preparationSteps, String createdBy, long likes, long dislikes, String picUri, String timestamp, String img, List<Steps> steps, boolean isPublishedByUser, boolean isProfile, FirebaseUser user, String userID) {
        this.recipeId = recipeId;
        this.title = title;
        this.category = category;
        this.ingredients = ingredients;
        this.preparationSteps = preparationSteps;
        this.createdBy = createdBy;
        this.likes = likes;
        this.dislikes = dislikes;
        this.picUri = picUri;
        this.timestamp = timestamp;
        this.img = img;
        this.steps = steps;
        this.isPublishedByUser = isPublishedByUser;
        this.isProfile = isProfile;
        this.user = user;
        this.userID = userID;
    }


}
