package com.example.wasfah.database;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.wasfah.model.RecipeModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RecipeFirebaseManager {

    private static DatabaseReference db = FirebaseDatabase.getInstance("https://wasfah-126bf-default-rtdb.firebaseio.com").getReference().child("Recipes");

    public void SaveRecipe(RecipeModel recipe, Activity context)
    {
        db.child(recipe.getRecipeId()).setValue(recipe)
                .addOnCompleteListener(context, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(context,"Recipe Published Successfully..",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public boolean DeleteRecipe(RecipeModel recipe)
    {
        return false;
    }

    public RecipeModel findRecipe(String id)
    {
        return null;
    }

    public List<RecipeModel> findAll()
    {
        return null;
    }

    public List<RecipeModel> findAllByUser(String email)
    {
        return null;
    }
}
