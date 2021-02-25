package com.example.wasfah.model;

import java.util.List;
import java.util.UUID;

public class RecipeModel {

    private String recipeId;
    private String title;
    private String category;
    private List<IngredientModel> ingredients;
    private List<StepModel> preparationSteps;
    private String createdBy;
    private String picUri;

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
    public String getPicUri2() {
        return "https://1.bp.blogspot.com/-eDCzOgLuiTg/XkMKlPN0hhI/AAAAAAAABJ8/aUWdGB_87EAagPAQhLvKs2RaICBjkasOwCLcBGAsYHQ/s1600/WhatsApp%2BImage%2B2020-02-11%2Bat%2B8.31.27%2BPM%2B%25281%2529.jpeg";
    }
    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }
}

